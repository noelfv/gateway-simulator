package com.bbva.orchlib.validations;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.orchlib.featuretoggle.businessdata.BusinessData;
import com.bbva.orchlib.featuretoggle.businessdata.Fields;
import com.bbva.orchlib.featuretoggle.businessdatalocal.BusinessDataLocal;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Class for global validations
 */

public class ValidationsGlobal {

  private static BusinessData businessData;
  private static List<BusinessDataLocal> businessDataLocal;

    public ValidationsGlobal() {
       // constructor
    }

    public static void setBusinessData(BusinessData businessData) {
        ValidationsGlobal.businessData = businessData;
    }

    public static void setBusinessDataLocalList(List<BusinessDataLocal> businessDataLocal) {
        ValidationsGlobal.businessDataLocal =businessDataLocal;
    }


    /**
     * Validates the BIN
     * @param iso20022 Receives the iso20022 to obtain the fields to validate
     * @return Returns true if all validations are true, if any are false, returns false
     */
    public static boolean validateBin(ISO20022 iso20022) {

        String bin = iso20022.getEnvironment().getCard().getPan();
        boolean result = false;

        for(int i=0 ; i<businessData.getBins().size() ; i++) {
            String key = businessData.getBins().get(i).getKey();
            if(bin.startsWith(key)) {
            	result = true;
                break;
            }

        }
        return result;
    }

    /**
     * Validates the processing code
     * @param iso20022 Receives the iso20022 to obtain the fields to validate
     * @return Returns true if all validations are true, if any are false, returns false
     */
    public static boolean validateProcessingCode(ISO20022 iso20022) {
        String networkName = iso20022.getNetworkName();
        String transactionType = iso20022.getTransaction().getTransactionType();
        String accountFrom = iso20022.getTransaction().getAccountFrom().getAccountType();
        String accountTo = iso20022.getTransaction().getAccountTo().getAccountType();
        boolean foundTransactionType = validateType(networkName, transactionType, 0);
        boolean foundAccountFrom = validateType(networkName, accountFrom, 1);
        boolean foundAccountTo = validateType(networkName, accountTo, 2);
        return foundTransactionType && foundAccountFrom && foundAccountTo;
    }


    /**
     * Validates the type processing code
     * @param type Receives the type of field to validate
     * @param positionType Receives the position of the type of field to validate
     * @return Returns true if all validations are true, if any are false, returns false
     */
    private static boolean validateType(String networkName, String type, int positionType) {
        for (BusinessDataLocal businessDataLocal : businessDataLocal) {
            if(networkName.equals(businessDataLocal.getNetwork()) && (positionType < businessDataLocal.getProcess().size())) {
                boolean matchFound = businessDataLocal.getProcess().get(positionType).getData().stream()
                        .map(data -> data.getName().equals("0") ? "00" : data.getName())
                        .anyMatch(name -> name.equals(type));

                if (matchFound) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Validates the currency
     * @param iso20022 Receives the iso20022 to obtain the fields to validate
     * @return Returns true if all validations are true, if any are false, returns false
     */
    public static boolean validateCurrency(ISO20022 iso20022) {
        String currency =  iso20022.getTransaction().getTransactionAmounts().getTransactionAmount().getCurrency();
        boolean result = false;

        for(int i=0 ; i<businessData.getCurrency().size() ; i++) {
            String key = businessData.getCurrency().get(i).getCurrencyCode();

            if(key.equals(currency)) {
                result = true;
                break;
            }

        }
        return result;
    }

    /**
     * Validates that the operation date does not exceed the system date by more than 23 hours
     * @param iso20022 Iso where the operation date is recovered
     * @return Returns true, if the operation date does not exceed more than 23 hours
     * to system date, otherwise returns false
     */
    public static boolean validateOperationDate(ISO20022 iso20022){
        boolean tiempo = false;

        String isoLocalDate = iso20022.getTransaction().getTransactionId().getLocalDate();
        String isoLocalTime = iso20022.getTransaction().getTransactionId().getLocalTime();

        try {

            // Get system Date
            Calendar calendar = Calendar.getInstance();

            //iso date format
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
            Date isoDate = originalFormat.parse(calendar.get(Calendar.YEAR)+isoLocalDate+" "+isoLocalTime);

            // Calculate the difference in milliseconds and converting to hours
            long hoursDifference = (calendar.getTimeInMillis() - isoDate.getTime()) / 3600000;

            if(hoursDifference>=0 && hoursDifference <= 23 )
                tiempo = true;
        } catch (ParseException e) {
            return false;
        }

        return tiempo;
    }

    /**
     * Validates non-financial requests must not have an amount greater than 0.
     * @param iso20022 Iso where the operation financial is recovered
     * @return If true, return true, otherwise false
     */
    public static boolean validateNonFinancialRequests(ISO20022 iso20022) {
        String processCode = iso20022.getTransaction().getTransactionType();
        Double amount = iso20022.getTransaction().getTransactionAmounts().getTransactionAmount().getAmount();

        boolean validate = false;

        int processInt = Integer.parseInt(processCode);

        // Eliminar el punto decimal y convertir a BigDecimal
        BigDecimal bigDecimal = BigDecimal.valueOf(amount);
        // Convertir a entero
        int amountInt = bigDecimal.intValue();

        if(processInt > 29 && amountInt == 0)
            validate = true;

        return validate;
    }

    /**
     * Retrieves the description of the BIN.
     * @param bin BIN to search
     * @return Returns the description of the BIN.
     */
    public static String getDescriptionBin(String bin) {

        String description = "";

        for(int i=0 ; i<businessData.getBins().size() ; i++) {
            String key = businessData.getBins().get(i).getKey();
            if(bin.startsWith(key)) {
                for (Fields file : businessData.getBins().get(i).getFields()) {
                    if(file.getName().equals("description")) {
                        description = file.getValue();
                        break;
                    }
                }
                break;
            }
        }
        return description;
    }

}