package com.bbva.orchestrator.parser.common;

import java.util.Map;

public abstract class ISOStringConverter {

    private static final char ZERO_VALUE = '0';
    private static final char ONE_VALUE = '1';
    private static final int LENGTH_BINARY_PRIMARY_BITMAP = 64;
    private static final int LENGTH_BINARY_SECONDARY_BITMAP = 128;

    protected ISOStringConverter() {
    }

    public String convertToISOString(Map<String, String> mapValues, boolean clean) {
        StringBuilder binaryBitmap = new StringBuilder();
        binaryBitmap.append(ZERO_VALUE);
        StringBuilder isoValues = new StringBuilder();
        // Se iteran todos los posibles campos de la trama ISO8583
        for (int i = 2; i <= LENGTH_BINARY_SECONDARY_BITMAP; i++) {
            ISOField isoField = getISOFieldById(i);
            if (null != isoField && mapValues.containsKey(isoField.getName())) {
                String value = mapValues.get(isoField.getName());
                if (null != value && !value.isEmpty()) {
                    binaryBitmap.append(ONE_VALUE);
                    isoValues.append(isoField.parseISOValue(isoField, value, clean));
                } else {
                    binaryBitmap.append(ZERO_VALUE);
                }
            } else {
                binaryBitmap.append(ZERO_VALUE);
            }
        }

        if (binaryBitmap.indexOf("1", LENGTH_BINARY_PRIMARY_BITMAP) != -1) {
            binaryBitmap.setCharAt(0, '1');
        } else {
            binaryBitmap.setLength(LENGTH_BINARY_PRIMARY_BITMAP);
        }

        return clean ?
                mapValues.get("messageType") + ISOUtil.convertBITMAPtoHEX(binaryBitmap.toString()) + isoValues :
                ISOUtil.stringToEBCDICHex(mapValues.get("messageType")) + ISOUtil.convertBITMAPtoHEX(binaryBitmap.toString()) + isoValues;
    }

    protected abstract ISOField getISOFieldById(int id);
}
