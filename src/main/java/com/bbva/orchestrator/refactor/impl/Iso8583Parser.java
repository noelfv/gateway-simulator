package com.bbva.orchestrator.refactor.impl;

import com.bbva.orchestrator.parser.common.ISODataType;
import com.bbva.orchestrator.refactor.utilsXXXX;
import com.bbva.orchlib.parser.ParserException;

import java.util.HashMap;
import java.util.Map;

// Asume que ISOUtil es una clase de utilidad existente con métodos para conversiones.
// Asegúrate de que tus ISODataType estén definidos.
// import com.yourpackage.ISOUtil;
// import com.yourpackage.ISODataType;
// import com.yourpackage.ISOField; // Tu interfaz ISOField

public class Iso8583Parser {


    public static void main(String[] args) {
        String trama="F0F1F0F0767F4401A8E1A000F1F6F5F1F9F3F4F8F8F2F2F1F9F0F4F2F6F4F0F0F3F0F0F0F0F0F0F0F0F0F0F0F1F5F0F0F0F0F0F0F0F0F0F0F1F5F0F0F1F2F1F6F1F8F2F7F3F4F6F1F0F0F0F0F0F0F0F0F1F1F7F3F1F3F2F7F3F4F1F2F1F6F2F5F1F2F1F2F1F6F1F2F1F6F5F9F9F9F0F2F1F0F6F9F9F9F9F0F1F0F6F0F0F9F6F8F5F3F7F5F1F9F3F4F8F8F2F2F1F9F0F4F2F6F4C4F2F5F1F2F2F0F1F1F6F5F2F5F8F3F6F0F1F5F0F0F1F2F0F0F0F0F1F0F0F0F0F1D4E3C640E3C5E2E3C1C2C3F1F2F3E3C5E2E3D4E3C6F1F9D4859983888195A3409581948540404040404040404040D4859983888195A3408389A3A840D7C5D9F0F3F1D9F7F5F2F6F0F1F0F3F0F2F8F0F2F0F2F0F0F0F3F0F3F0F2F8F0F4F0F2F0F0F6F0F4F6F0F4";
        Map<String, String> mappedFields = mapFields(trama);

    }

    // Método principal para mapear la trama a un Map de valores
    public static Map<String, String> mapFields(String iso) {
        Map<String, String> valuesMap = new HashMap<>();
        // Usa StringBuilder para eficiencia si la trama es grande o se modifica
        StringBuilder isoMessage =  new StringBuilder(iso);
        int position = 0;

        try {
            // Process MTI
            position = processFieldData(ISOFieldMastercard.MESSAGE_TYPE, isoMessage, position, valuesMap);

            // Process Primary Bitmap
            position = processFieldData(ISOFieldMastercard.BITMAP_PRIMARY, isoMessage, position, valuesMap);
            String binaryBitMapPrimary = valuesMap.get(ISOFieldMastercard.BITMAP_PRIMARY.getName());

            // Check and process Secondary Bitmap
            if (binaryBitMapPrimary.charAt(0) == '1') {
                position = processFieldData(ISOFieldMastercard.BITMAP_SECONDARY, isoMessage, position, valuesMap);
            }
            // Concatenate bitmaps (ensure getOrDefault handles missing secondary bitmap)
            String fullBitmap = valuesMap.get(ISOFieldMastercard.BITMAP_PRIMARY.getName())
                    .concat(valuesMap.getOrDefault(ISOFieldMastercard.BITMAP_SECONDARY.getName(), ""));

            // Iterate through fields based on bitmap
            // Start from field 2, as field 1 is the secondary bitmap itself
            for (int i = 2; i <= fullBitmap.length(); i++) {
                if (fullBitmap.charAt(i - 1) == '1') { // Check if the bit is set
                    ISOFieldMastercard field = ISOFieldMastercard.getById(i);

                    if (field == null) {
                        // Log a warning or throw a specific exception if a field is not mapped
                        System.err.println("Field " + i + " is active in bitmap but not defined in ISOFieldMastercard enum.");
                        // You might choose to skip this field or throw an error based on your strictness
                        throw new ParserException("Field not allowed: " + i + " - No mapping available.");
                    }
                    position = processFieldData(field, isoMessage, position, valuesMap);
                }
            }
        } catch (Exception e) {
            // Consider more specific exception handling and error messages
           throw new ParserException("Error parsing ISO message at position " + position + ": " + e.getMessage(), e);
            //throw new ParserException("Error parsing ISO message at position " + position + ": " + e.getMessage());
        }
        return valuesMap;
    }

    // Método refactorizado para procesar cada campo
    private static int processFieldData(ISOFieldMastercard isoField, StringBuilder isoMessage, int currentPosition, Map<String, String> valuesMap) {
        String rawFieldData; // Esta será la porción de la trama para este campo, antes de decodificarla
        int fieldDataLengthInChars; // Longitud del dato real del campo en caracteres hexadecimales

        try {
            if (isoField.isVariable()) {
                // Determine length of the length prefix (e.g., 2 for LLVAR, 3 for LLLVAR)
                int prefixLengthInChars = isoField.getLength() * 2; // e.g., LLVAR (2 chars) = 4 chars HEX

                if (currentPosition + prefixLengthInChars > isoMessage.length()) {
                    throw new ParserException("Trama demasiado corta para leer el prefijo de longitud del campo " + isoField.getId());
                }

                String lengthPrefixHex = isoMessage.substring(currentPosition, currentPosition + prefixLengthInChars);
                int actualValueLengthInBytes;

                // Determine how to parse the length prefix based on ISODataType
                if (isoField.getTypeData() == ISODataType.NUMERIC || isoField.getTypeData() == ISODataType.NUMERIC_DECIMAL) {
                    // Assuming numeric length prefixes are EBCDIC and then converted to decimal
                    actualValueLengthInBytes = Integer.parseInt(utilsXXXX.ISOUtil.ebcdicToString(lengthPrefixHex));
                } else if (isoField.getTypeData() == ISODataType.BINARY_STRING) {
                    // Assuming binary/BCD length prefixes
                    actualValueLengthInBytes = Integer.parseInt(utilsXXXX.ISOUtil.hexToDecimal(lengthPrefixHex));
                } else {
                    // Default for other types, might need more specific handling
                    actualValueLengthInBytes = Integer.parseInt(utilsXXXX.ISOUtil.ebcdicToString(lengthPrefixHex));
                }

                fieldDataLengthInChars = actualValueLengthInBytes * 2; // Cada byte son 2 chars HEX

                currentPosition += prefixLengthInChars; // Advance past the length prefix

            } else { // Fixed length field
                // Longitud total en caracteres hexadecimales
                fieldDataLengthInChars = isoField.getLength() * 2;
            }

            if (currentPosition + fieldDataLengthInChars > isoMessage.length()) {
                throw new ParserException("Trama demasiado corta para el campo " + isoField.getId() + ". Esperado: " + fieldDataLengthInChars + " chars, Disponible desde pos " + currentPosition + ": " + (isoMessage.length() - currentPosition));
            }

            // Extract the raw data for the field
            rawFieldData = isoMessage.substring(currentPosition, currentPosition + fieldDataLengthInChars);

            // Delegate the actual parsing to the strategy associated with the enum field
            String processedValue = isoField.getParserStrategy().parse(rawFieldData, isoField);

            valuesMap.put(isoField.getName(), processedValue);
            currentPosition += fieldDataLengthInChars;

            return currentPosition; // Return the new position
        } catch (ParserException e) {
            throw e; // Re-throw custom exceptions
        } catch (Exception e) {
            throw new ParserException("Error processing field " + isoField.getId() + " (" + isoField.getName() + ") at position " + currentPosition + ": " + e.getMessage(), e);
        }
    }
}