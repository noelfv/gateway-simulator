package com.bbva.gateway.mapper;


//import com.bbva.business.gateway.iso20022.Iso20022Request;
//import com.bbva.business.gateway.iso20022.Iso20022Response;
import com.bbva.gateway.dto.iso20022.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Clase para mapear los datos
 */

public class Convert {

	private Convert() {

	}
	// ******************************
	// PROTO REQUEST TO DTO SERVICE
	// ******************************

	/**
	 * Mapea un objeto ISO20022 de entrada (requestDTO) a un mensaje de tipo Iso20022Request
	 * específico del flujo de datos.
	 *
	 * @param requestDTO El objeto ISO20022 de entrada que se desea mapear.
	 * @return Un mensaje de tipo Iso20022Request específico del flujo de datos mapeado a partir de requestDTO.
	 */
	/*
	public static Iso20022Request mapIso20022DTOtoRequestGRPC(ISO20022 requestDTO) {
		return IMapperISO20022.INSTANCE.iso20022DTOToIso20022Request(requestDTO);
	}
*/

	/**
	 * Convierte un mensaje de tipo Iso20022Response en un objeto ISO20022DTO.
	 *
	 * @param response El mensaje de tipo Iso20022Response que se desea convertir.
	 * @return Un objeto ISO20022DTO resultante de la conversión.
	 */
	/*
	public static ISO20022 mapResponseGRPCtoIso20022DTO(Iso20022Response response) {
		return IMapperISO20022.INSTANCE.iso20022ResponseToIso20022DTO(response);
	}
*/


	/**
	 * Convierte un objeto Iso20022Request de GRPC en un objeto ISO20022DTO.
	 *
	 * @param request El objeto Iso20022Request de GRPC que se desea convertir en un objeto ISO20022DTO.
	 * @return Un objeto ISO20022DTO que representa la conversión del objeto Iso20022Request.
	 */
/*
	public static ISO20022 mapRequestGRPCtoIso20022DTO(Iso20022Request request) {
		return IMapperISO20022.INSTANCE.iso20022RequestToIso20022DTO(request);
	}
*/

	/**
	 * Convierte un objeto ISO20022 en un objeto Iso20022Response para comunicación a través de GRPC.
	 *
	 * @param responseDTO El objeto ISO20022 que se desea convertir en un Iso20022Response para GRPC.
	 * @return Un objeto Iso20022Response que representa la conversión del objeto ISO20022.
	 */
/*
	public static Iso20022Response mapIso20022DTOtoResposnseGRPC(ISO20022 responseDTO) {
		return IMapperISO20022.INSTANCE.iso20022DTOToIso20022Response(responseDTO);
	}
*/


	/**
	 * Updates the processing result of the given ISO20022 object with the provided data.
	 * If the processing result does not exist, it initializes a new one with the provided data.
	 *
	 * @param iso20022 The ISO20022 object whose processing result is to be updated.
	 * @param approvalCode The approval code to be set in the processing result.
	 * @param approvalData The approval data to be set in the processing result.
	 * @param actions The list of actions to be set in the processing result.
	 * @param resultData The result data to be set in the processing result.
	 * @param additionalInfo The list of additional information to be set in the processing result.
	 */
	public static void mapProcessingResult(ISO20022 iso20022, String approvalCode, ApprovalDataDTO approvalData,
											  List<ActionDTO> actions, ResultDataDTO resultData,
											  List<AdditionalInformationDTO> additionalInfo) {
		ProcessingResultDTO processingResult = iso20022.getProcessingResult();

		if (processingResult == null) {
			processingResult = initializeProcessingResult(approvalCode, approvalData, actions, resultData, additionalInfo);
			iso20022.setProcessingResult(processingResult);
		} else {
			updateApprovalCode(processingResult, approvalCode);
			updateApprovalData(processingResult, approvalData);
			updateActions(processingResult, actions);
			updateResultData(processingResult, resultData);
			updateAdditionalInformation(processingResult, additionalInfo);
		}
	}


	/**
	 * Initializes a new ProcessingResultDTO object with the provided data.
	 *
	 * @param approvalCode The approval code to be set in the processing result.
	 * @param approvalData The approval data to be set in the processing result.
	 * @param actions The list of actions to be set in the processing result.
	 * @param resultData The result data to be set in the processing result.
	 * @param additionalInfo The list of additional information to be set in the processing result.
	 * @return A new ProcessingResultDTO object initialized with the provided data.
	 */
	private static ProcessingResultDTO initializeProcessingResult(String approvalCode, ApprovalDataDTO approvalData,
																  List<ActionDTO> actions, ResultDataDTO resultData,
																  List<AdditionalInformationDTO> additionalInfo) {
		return ProcessingResultDTO.builder()
				.approvalCode(approvalCode)
				.approvalData(approvalData)
				.action(actions != null ? new ArrayList<>(actions) : null)
				.resultData(resultData)
				.additionalInformation(additionalInfo != null ? new ArrayList<>(additionalInfo) : null)
				.build();
	}


	/**
	 * Updates the existing approval data with the new approval data.
	 * If the new approval data contains a non-null approval entity, it will be set in the existing approval data.
	 *
	 * @param existingApprovalData the existing approval data to be updated
	 * @param newApprovalData the new approval data containing the updates
	 */
	private static void updateApprovalData(ApprovalDataDTO existingApprovalData, ApprovalDataDTO newApprovalData) {
		if (newApprovalData.getApprovalEntity() != null) {
			existingApprovalData.setApprovalEntity(newApprovalData.getApprovalEntity());
		}
	}


	/**
	 * Updates the existing list of actions with the new list of actions.
	 * If an action from the new list has the same action type as an existing action,
	 * it updates the existing action with the new action's additional data.
	 * If an action from the new list does not exist in the existing list, it adds the new action to the existing list.
	 *
	 * @param existingActions the list of existing actions to be updated
	 * @param newActions the list of new actions containing the updates
	 */
	private static void updateActions(List<ActionDTO> existingActions, List<ActionDTO> newActions) {
		for (ActionDTO action : newActions) {
			boolean found = false;
			for (ActionDTO existingAction : existingActions) {
				if (existingAction.getActionType().equals(action.getActionType())) {
					if (action.getAdditionalData() != null) {
						existingAction.setAdditionalData(action.getAdditionalData());
					}
					found = true;
					break;
				}
			}
			if (!found) {
				existingActions.add(action);
			}
		}
	}


	/**
	 * Updates the existing result data with the new result data.
	 * If the new result data contains non-null fields, they will be set in the existing result data.
	 *
	 * @param existingResultData the existing result data to be updated
	 * @param newResultData the new result data containing the updates
	 */
	private static void updateResultData(ResultDataDTO existingResultData, ResultDataDTO newResultData) {
		if (newResultData.getResult() != null) {
			existingResultData.setResult(newResultData.getResult());
		}
		if (newResultData.getResultDetails() != null) {
			existingResultData.setResultDetails(newResultData.getResultDetails());
		}
		if (newResultData.getOtherResult() != null) {
			existingResultData.setOtherResult(newResultData.getOtherResult());
		}
		if (newResultData.getOtherResultDetails() != null) {
			existingResultData.setOtherResultDetails(newResultData.getOtherResultDetails());
		}
		if (newResultData.getAdditionalResultInformation() != null) {
			existingResultData.setAdditionalResultInformation(new ArrayList<>(newResultData.getAdditionalResultInformation()));
		}
	}


	/**
	 * Updates the existing list of additional information with the new list of additional information.
	 * If an entry from the new list has the same key as an existing entry,
	 * it updates the existing entry with the new entry's value.
	 * If an entry from the new list does not exist in the existing list, it adds the new entry to the existing list.
	 *
	 * @param existingAdditionalInfo the list of existing additional information to be updated
	 * @param newAdditionalInfo the list of new additional information containing the updates
	 */
	private static void updateAdditionalInformation(List<AdditionalInformationDTO> existingAdditionalInfo, List<AdditionalInformationDTO> newAdditionalInfo) {
		for (AdditionalInformationDTO info : newAdditionalInfo) {
			boolean found = false;
			for (AdditionalInformationDTO existingInfo : existingAdditionalInfo) {
				if (existingInfo.getKey().equals(info.getKey())) {
					existingInfo.setValue(info.getValue());
					found = true;
					break;
				}
			}
			if (!found) {
				existingAdditionalInfo.add(info);
			}
		}
	}



	/**
	 * Updates the approval code in the given ProcessingResultDTO object.
	 * If the provided approval code is not null, it sets the approval code in the processing result.
	 *
	 * @param processingResult The ProcessingResultDTO object whose approval code is to be updated.
	 * @param approvalCode The approval code to be set in the processing result.
	 */
	private static void updateApprovalCode(ProcessingResultDTO processingResult, String approvalCode) {
		if (approvalCode != null) {
			processingResult.setApprovalCode(approvalCode);
		}
	}


	/**
	 * Updates the existing approval data in the given ProcessingResultDTO object.
	 * If the provided approval data is not null, it sets or updates the approval data in the processing result.
	 *
	 * @param processingResult The ProcessingResultDTO object whose approval data is to be updated.
	 * @param approvalData The approval data to be set or updated in the processing result.
	 */
	private static void updateApprovalData(ProcessingResultDTO processingResult, ApprovalDataDTO approvalData) {
		if (approvalData != null) {
			ApprovalDataDTO existingApprovalData = processingResult.getApprovalData();
			if (existingApprovalData == null) {
				processingResult.setApprovalData(approvalData);
			} else {
				updateApprovalData(existingApprovalData, approvalData);
			}
		}
	}

	/**
	 * Updates the existing list of actions in the given ProcessingResultDTO object.
	 * If the provided list of actions is not null, it sets or updates the list of actions in the processing result.
	 *
	 * @param processingResult The ProcessingResultDTO object whose list of actions is to be updated.
	 * @param actions The list of actions to be set or updated in the processing result.
	 */
	private static void updateActions(ProcessingResultDTO processingResult, List<ActionDTO> actions) {
		if (actions != null) {
			List<ActionDTO> existingActions = processingResult.getAction();
			if (existingActions == null) {
				processingResult.setAction(new ArrayList<>(actions));
			} else {
				updateActions(existingActions, actions);
			}
		}
	}


	/**
	 * Updates the existing result data in the given ProcessingResultDTO object.
	 * If the provided result data is not null, it sets or updates the result data in the processing result.
	 *
	 * @param processingResult The ProcessingResultDTO object whose result data is to be updated.
	 * @param resultData The result data to be set or updated in the processing result.
	 */
	private static void updateResultData(ProcessingResultDTO processingResult, ResultDataDTO resultData) {
		if (resultData != null) {
			ResultDataDTO existingResultData = processingResult.getResultData();
			if (existingResultData == null) {
				processingResult.setResultData(resultData);
			} else {
				updateResultData(existingResultData, resultData);
			}
		}
	}


	/**
	 * Updates the existing list of additional information in the given ProcessingResultDTO object.
	 * If the provided list of additional information is not null, it sets or updates the list of additional information in the processing result.
	 *
	 * @param processingResult The ProcessingResultDTO object whose list of additional information is to be updated.
	 * @param additionalInfo The list of additional information to be set or updated in the processing result.
	 */
	private static void updateAdditionalInformation(ProcessingResultDTO processingResult, List<AdditionalInformationDTO> additionalInfo) {
		if (additionalInfo != null) {
			List<AdditionalInformationDTO> existingAdditionalInfo = processingResult.getAdditionalInformation();
			if (existingAdditionalInfo == null) {
				processingResult.setAdditionalInformation(new ArrayList<>(additionalInfo));
			} else {
				updateAdditionalInformation(existingAdditionalInfo, additionalInfo);
			}
		}
	}

}
