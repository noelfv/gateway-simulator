package com.bbva.orchestrator.refactor.impl3;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.gui.commons.ISO8583Processor;
import com.bbva.orchlib.parser.ParserException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Iso8583Parser {

    private static String trama122="F0F1F0F0FEFF640188E1E10A0000000000000040F1F6F5F5F3F6F5F0F9F9F9F9F9F9F9F9F9F9F0F0F0F0F0F0F0F0F0F0F0F0F0F2F2F9F9F0F0F0F0F0F0F0F0F0F6F3F5F6F0F0F0F0F0F0F0F2F2F9F9F0F0F6F1F6F0F7F2F7F2F4F7F2F7F6F4F6F8F0F6F1F0F0F0F0F0F0F8F9F8F7F1F6F0F3F2F7F2F4F0F6F1F6F2F9F0F5F0F6F1F6F0F6F1F5F5F8F1F8F8F4F0F1F0F0F0F6F0F0F3F2F8F6F0F6F0F0F3F2F8F6F5F1F6F7F5F4F8F9F8F7F1F6F0F0F4F0F0F2F1F6F4F0F0F2F1F6F0F0F0F1F0F8F7F7F8C1D7D7D3C54BC3D6D461C2C9D3D3404040404040404040F8F6F660F7F1F260F7F7F5F34040E4E2C1F1F1F8E3F3F7F1F5F0F5F1F1F0F0F0F0F0F9F9F9F9F9F7F4F2F0F7F0F1F0F3F2F1F0F2F2F0F8F0F5F0F4D4F1F0F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F6C1D8E2F6F0F9C1D8C6F1F1F6F7F5F3F2F0F1F0F3F8F8F0F0F2F0F2F1F4F0F3F0F3F8F8F0F0F4F0F2F1F4F0F5F0F2F0F0F7F1F0F4F1F8C340F6F0F4F8F4F0F6F0F4F0F3F7F0F1F3F3F0F1F2F9F5F0F0F1F9F3C8D2D8C9E6C5E8E5C9E2C5F7E4D2E3D8D2F8E8D1F5C3F0F0F2F6F0F0F0F4F1F0F0F0F0F0F6F0F0F8F4F0F9F5F0F1F44040404040F0F0F9D4C2D2C3C7F4F6F2C6F1F0F1F0F0F1F0F9F5F0F0F1F0F1F8D6D5C540C1D7D7D3C540D7C1D9D240E6C1E8F0F0F2F0F0F3C3C140F0F0F3F0F1F3C1D7D7D3C54BC3D6D440C2C9D3F0F0F4F0F1F0F8F6F6F7F1F2F7F7F5F3F0F0F7F0F2F1F8F4F2F8F0F5F8F2F24040404040404040404040E8";

    public static void main(String[] args) {
        String trama="F0F1F0F0767F4401A8E1A000F1F6F5F1F9F3F4F8F8F2F2F1F9F0F4F2F6F4F0F0F3F0F0F0F0F0F0F0F0F0F0F0F1F5F0F0F0F0F0F0F0F0F0F0F1F5F0F0F1F2F1F6F1F8F2F7F3F4F6F1F0F0F0F0F0F0F0F0F1F1F7F3F1F3F2F7F3F4F1F2F1F6F2F5F1F2F1F2F1F6F1F2F1F6F5F9F9F9F0F2F1F0F6F9F9F9F9F0F1F0F6F0F0F9F6F8F5F3F7F5F1F9F3F4F8F8F2F2F1F9F0F4F2F6F4C4F2F5F1F2F2F0F1F1F6F5F2F5F8F3F6F0F1F5F0F0F1F2F0F0F0F0F1F0F0F0F0F1D4E3C640E3C5E2E3C1C2C3F1F2F3E3C5E2E3D4E3C6F1F9D4859983888195A3409581948540404040404040404040D4859983888195A3408389A3A840D7C5D9F0F3F1D9F7F5F2F6F0F1F0F3F0F2F8F0F2F0F2F0F0F0F3F0F3F0F2F8F0F4F0F2F0F0F6F0F4F6F0F4";
        Map<String, String> mapValues = mapFields(trama122);
       // List<StructureSubField> subFields = ISO8583Processor.parseSubfields(mapValues);

        System.out.println(mapValues);
    }

    /**
     * Parsea una trama ISO 8583 y devuelve un mapa con los nombres de los campos y sus valores decodificados.
     *
     * @param tramaIso La trama ISO 8583 completa en formato hexadecimal.
     * @return Un mapa donde la clave es el nombre del campo y el valor es su contenido decodificado.
     * @throws ParserException Si ocurre un error durante el parsing.
     */
    public static Map<String, String> mapFields(String tramaIso) {
        Map<String, String> valuesMap = new HashMap<>();
        StringBuilder isoMessage = new StringBuilder(tramaIso);
        int position = 0;
        boolean containsSecondaryBitmap = false; // Se mantiene para el mensaje de error

        try {
            // Procesar MTI
            position = processFieldData(ISOFieldMastercard.MESSAGE_TYPE, isoMessage, position, valuesMap);

            // Procesar Bitmap Primario
            position = processFieldData(ISOFieldMastercard.BITMAP_PRIMARY, isoMessage, position, valuesMap);
            String binaryBitMapPrimary = valuesMap.get(ISOFieldMastercard.BITMAP_PRIMARY.getName());

            // Verificar y procesar Bitmap Secundario
            if (binaryBitMapPrimary != null && !binaryBitMapPrimary.isEmpty() && binaryBitMapPrimary.charAt(0) == '1') {
                position = processFieldData(ISOFieldMastercard.BITMAP_SECONDARY, isoMessage, position, valuesMap);
                containsSecondaryBitmap = true;
            }

            // Concatenar bitmaps (asegurarse de que getOrDefault maneje el bitmap secundario ausente)
            String fullBitmap = valuesMap.get(ISOFieldMastercard.BITMAP_PRIMARY.getName())
                    .concat(valuesMap.getOrDefault(ISOFieldMastercard.BITMAP_SECONDARY.getName(), ""));

            // Iterar a través de los campos basándose en el bitmap
            // Se comienza la iteración desde el campo 2, ya que el campo 1 es el bitmap secundario
            for (int i = 2; i <= fullBitmap.length(); i++) {
                if (fullBitmap.charAt(i - 1) == '1') { // Verificar si el bit está activo
                    ISOFieldMastercard field = ISOFieldMastercard.getById(i);

                    if (field == null) {
                        LogsTraces.writeInfo("Campo no permitido: " + i + ". No hay mapeo definido en la estructura ISOField");
                        throw new ParserException(createMessageError(i));
                    }

                    position = processFieldData(field, isoMessage, position, valuesMap);
                }
            }
        } catch (ParserException e) {
            throw e; // Relanzar ParserException directamente
        } catch (Exception e) {
            // Capturar otras excepciones y envolverlas en ParserException
            throw new ParserException("No se puede parsear el mensaje ISO: " + e.getMessage() + "|" + ISOUtil.processError(tramaIso, containsSecondaryBitmap), e);
        }
        return valuesMap;
    }

    /**
     * Procesa los datos de un campo ISO 8583 de la trama.
     * Delega el parsing específico del tipo de dato a la estrategia asociada al campo.
     *
     * @param isoField La definición del campo ISO 8583 (desde el enum).
     * @param isoMessage La trama ISO 8583 completa en StringBuilder (formato hexadecimal).
     * @param currentPosition La posición actual en la trama (índice de caracteres HEX).
     * @param valuesMap El mapa donde se almacenarán los valores parseados.
     * @return La nueva posición en la trama después de procesar el campo.
     * @throws ParserException Si ocurre un error durante el parsing del campo.
     */
    private static int processFieldData(ISOFieldMastercard isoField, StringBuilder isoMessage, int currentPosition, Map<String, String> valuesMap) {
        try {
            // Obtener la porción restante de la trama desde la posición actual
            String remainingMessageSegment = isoMessage.substring(currentPosition);
            ParsedFieldResult result;

            if (isoField.isVariable()) {
                // Para campos variables, LlvarLengthPrefixParser maneja todos los cálculos de longitud internamente.
                // No pasamos un expectedHexLength aquí, ya que la estrategia misma lo determina.
                // El 0 es un valor dummy para el parámetro 'ignoredExpectedHexLength'
                result = isoField.getParserStrategy().parse(remainingMessageSegment, 0, isoField);
            } else {
                // Para campos fijos, calcular la longitud HEX esperada y pasarla.
                int expectedHexLength = isoField.getLength() * 2;
                result = isoField.getParserStrategy().parse(remainingMessageSegment, expectedHexLength, isoField);
            }

            valuesMap.put(isoField.getName(), result.getValue());

            // Avanzar la posición en la trama por la longitud total de caracteres HEX consumidos por el campo.
            currentPosition += result.getConsumedLengthInChars();

            return currentPosition;

        } catch (ParserException e) {
            throw e; // Relanzar excepciones personalizadas
        } catch (Exception e) {
            // Capturar otras excepciones y envolverlas en ParserException
            throw new ParserException("Error procesando campo " + isoField.getId() + " (" + isoField.getName() + ") en posición " + currentPosition + ": " + e.getMessage(), e);
        }
    }

    // Método auxiliar para mensajes de error
    private static String createMessageError(int fieldId) {
        return "Error en campo " + fieldId + ": " + "No tiene mapeo definido en la estructura ISOField";
    }
}