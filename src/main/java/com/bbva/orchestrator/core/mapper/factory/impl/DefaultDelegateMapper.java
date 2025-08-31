package com.bbva.orchestrator.core.mapper.factory.impl;

import com.bbva.gateway.dto.iso20022.*;
import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.gateway.utils.LogsTraces;
import com.bbva.gui.dto.Metadata;
import com.bbva.orchestrator.core.builders.ISO8583;
import com.bbva.orchestrator.core.commons.ContextData;
import com.bbva.orchestrator.core.commons.ProcessingResult;
import com.bbva.orchestrator.core.exception.MapperFlowAsynchronousException;
import com.bbva.orchestrator.core.exception.MapperLocalException;
import com.bbva.orchestrator.core.mapper.factory.ISO20022DelegateMapper;
import com.bbva.orchestrator.core.mapper.iso20022.strategy.impl.*;
import com.bbva.orchestrator.core.utils.FieldUtils;
import com.bbva.orchestrator.core.utils.MonitoringService;
import com.bbva.orchestrator.validations.FieldLocalCodeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DefaultDelegateMapper implements ISO20022DelegateMapper {

    private final ContextData contextData;
    private final EnvironmentMappingStrategy environmentStrategy;
    private final TransactionMappingStrategy transactionStrategy;
    private final ContextMappingStrategy contextStrategy;
    private final SupplementaryDataMappingStrategy supplementaryDataStrategy;
    private final SecurityTrailerMappingStrategy securityTrailerStrategy;
    private final ProtectedDataMappingStrategy protectedDataStrategy;
    private final TraceDataMappingStrategy traceDataStrategy;
    private final ProcessingResultMappingStrategy processingResultMappingStrategy;
    private final AddendumDataMappingStrategy addendumDataStrategy;
    private final CustomDataLocalMappingStrategy customDataLocalStrategy;
    private final MonitoringService monitoringService;


    /**
     * Mapea un objeto ISO8583 a un objeto ISO20022.
     * Utiliza estrategias de mapeo para convertir cada parte del mensaje.
     *
     * @param input El objeto ISO8583 a mapear.
     * @param subFields Campos adicionales que pueden ser necesarios para el mapeo.
     * @return Un objeto ISO20022 construido con los datos del mensaje ISO8583.
     */
    @Override
    public ISO20022 mapper(ISO8583 input, Map<String, String> subFields) {
        try {
            //TODO Si typeMessage es de respuesta solo seria necesaria crear el objeto processingResult ya que  es un flujo host
            //Colocar todos los mensajes que son devueltos por HOST como condicion
            if(isResponseHost(input.getMessageType())){
                throw new MapperFlowAsynchronousException("Flow Asynchronous");
            }

            // Mapeo paralelo (opcional, si necesitas alto rendimiento)
            EnvironmentDTO environment = environmentStrategy.mapper(input, subFields);
            TransactionDTO transaction = transactionStrategy.mapper(input, subFields);
            ContextDTO context = contextStrategy.mapper(input, subFields);
            List<SupplementaryDataDTO> supplementaryData = supplementaryDataStrategy.mapper(input, subFields);
            List<TraceDataDTO> traceData = traceDataStrategy.mapper(input, subFields);
            List<ProtectedDataDTO> protectedData = protectedDataStrategy.mapper(input, subFields);
            SecurityTrailerDTO securityTrailer = securityTrailerStrategy.mapper(input, subFields);
            AddendumDataDTO addendumData = addendumDataStrategy.mapper(input, subFields);
            CustomDataLocalDTO customDataLocal = customDataLocalStrategy.mapper(input, subFields);

            LogsTraces.writeInfo("requestMessage %s|%s"
                    .formatted(contextData.getISO8583(GrpcHeadersInfo.getTraceId()),
                            transaction.getTransactionId().getTransactionReference()));

            // Construir el objeto ISO20022 final usando su builder, solo con objetos no nulos
            ISO20022.ISO20022Builder iso20022Builder = ISO20022.builder()
                    .networkName(GrpcHeadersInfo.getNetwork())
                    .messageFunction(FieldLocalCodeMapper.getCode(FieldUtils.TYPE_MESSAGE, "in", input.getMessageType(), GrpcHeadersInfo.getNetwork()))
                    .socketPort(GrpcHeadersInfo.getPort())
                    .traceData(traceData)
                    .transaction(transaction)
                    .environment(environment)
                    .addendumData(addendumData)
                    .customDataLocal(customDataLocal)//se deberia eliminar este bloque en el sensitiveData del flowhandler
                    .monitoring(monitoringService.insertMonitoring(input, environment, transaction))
                    .mappingMetadata(Metadata.createMetadata(input));//Esto se deberia de setear en el mapper de la respuesta

            if (Objects.nonNull(context)) { // Asumiendo que context es un objeto DTO
                iso20022Builder.context(context);
            }
            if (Objects.nonNull(protectedData)) {
                iso20022Builder.protectedData(protectedData);
            }
            if (Objects.nonNull(securityTrailer)) {
                iso20022Builder.securityTrailer(securityTrailer);
            }
            if (Objects.nonNull(supplementaryData)) {
                iso20022Builder.supplementaryData(supplementaryData);
            }

            //  Solo agregar processingResult si es un mensaje de respuesta
            if (input.getMessageType().equals("0120") || input.getMessageType().equals("0420")) {
                iso20022Builder.processingResult(ProcessingResult.createProcessingResult(input));
            }

            return iso20022Builder.build();

        } catch (MapperFlowAsynchronousException e) {
            return flowAsynchronousToHost(input,subFields);
        }catch (MapperLocalException e) {
            //TODO: Que hacer si hay una excepcion de mapeo local ¿Que deberiamos hacer como flujo funcional?
            return buildFallbackResponse(input); //Esto no deberia devolverse, lo correcto es lanzar una excepcion
        } catch (Exception e) {
            //TODO: Error no contemplado o controlados por temas de null pointer ¿Que deberiamos hacer como flujo funcional?
            return buildFallbackResponse(input); //Esto no deberia devolverse, lo correcto es lanzar una excepcion
        }
    }

    private ISO20022 flowAsynchronousToHost(ISO8583 input, Map<String, String> subFields) {
        //TODO: Validar si es necesario llamar al uncrypto en el flujo HOST
        //TODO: Construir los objetoo mandatorios del iso200022 segun el catalogo para no tener error de nullPointer
        EnvironmentDTO environment = EnvironmentDTO.builder().build();
        AddendumDataDTO addendumData = addendumDataStrategy.mapper(input, subFields);
        ProcessingResultDTO processingResultDTO = ProcessingResult.createProcessingResult(input);
        return ISO20022.builder()
                .networkName(GrpcHeadersInfo.getNetwork())
                .environment(environment)
                .addendumData(addendumData)//Contiene el mensaje de respuesta del host
                .processingResult(processingResultDTO)
                .monitoring(monitoringService.updateMonitoring(input))
                .mappingMetadata(Metadata.createMetadata(input))
                .build();
    }

    /* Este método es un placeholder, ya que el mapeo inverso no está implementado.
     * En una implementación real, deberías convertir los campos de ISO20022 a ISO8583.
     */
    @Override
    public Map<String, String> unMapper(ISO20022 input) {
        Map<String,String> addendumData = addendumDataStrategy.unMapper(input.getAddendumData());
        Map<String,String> environment = environmentStrategy.unMapper(input.getEnvironment());
        Map<String,String> transaction = transactionStrategy.unMapper(input.getTransaction());
        Map<String,String> context = contextStrategy.unMapper(input.getContext());
        Map<String,String> protectedData = protectedDataStrategy.unMapper(input.getProtectedData());
        Map<String,String> securityTrailer = securityTrailerStrategy.unMapper(input.getSecurityTrailer());
        Map<String,String> traceData = traceDataStrategy.unMapper(input.getTraceData());
        Map<String,String> suplementaryData = supplementaryDataStrategy.unMapper(input.getSupplementaryData());
        Map<String,String> processingResult = processingResultMappingStrategy.unMapper(input.getProcessingResult());


        Map<String,String> mapValues=new HashMap<>();

        mapValues.putAll(addendumData);
        mapValues.putAll(environment);
        mapValues.putAll(transaction);
        mapValues.putAll(context);
        mapValues.putAll(protectedData);
        mapValues.putAll(securityTrailer);
        mapValues.putAll(traceData);
        mapValues.putAll(suplementaryData);
        mapValues.putAll(processingResult);

        // momentaneo para limpiar los en blanco
        mapValues.values().removeIf(String::isEmpty);

        mapValues.put("messageType", "0110"); // Mientras no nos den la marca para saber que tipo de respuesta es.

        return mapValues;
    }


    // Metodo auxiliar para verificar si es un mensaje de respuesta
    private boolean isResponseHost(String messageType) {
        if (messageType == null || messageType.length() < 4) return false;
        return Set.of("0110", "0130", "0410", "0430", "0210").contains(messageType); //Validar si hay que incluir el 0130
    }


    //Esto solo aplica para el passtrhoug luego se debe de evaluar si se debe de eliminar
    private ISO20022 buildFallbackResponse(ISO8583 input) {
        return ISO20022.builder()
                .networkName(GrpcHeadersInfo.getNetwork())
                .messageFunction(FieldLocalCodeMapper.getCode(FieldUtils.TYPE_MESSAGE, "in", input.getMessageType(), GrpcHeadersInfo.getNetwork()))
                .socketPort(GrpcHeadersInfo.getPort())
                .environment(EnvironmentDTO.builder()
                        .card(CardDTO.builder()
                                .pan(input.getPrimaryAccountNumber())
                                .build())
                        .build())
                .addendumData(AddendumDataDTO.builder()
                        .additionalData(List.of(
                                AdditionalDataDTO.builder()
                                        .key("ISO8583_HOST")
                                        .value(input.getOriginalMessage())
                                        .build(),
                                AdditionalDataDTO.builder()
                                        .key("MSGTYPE")
                                        .value(input.getMessageType())
                                        .build()
                        ))
                        .build())
                .build();
    }
}