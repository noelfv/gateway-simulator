package com.bbva.orchestrator.refactor.impl2;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.orchlib.parser.ParserException;
import java.util.HashMap;
import java.util.Map;

public class Iso8583Parser {


    public static void main(String[] args) {
        String trama="F0F1F0F0767F4401A8E1A000F1F6F5F1F9F3F4F8F8F2F2F1F9F0F4F2F6F4F0F0F3F0F0F0F0F0F0F0F0F0F0F0F1F5F0F0F0F0F0F0F0F0F0F0F1F5F0F0F1F2F1F6F1F8F2F7F3F4F6F1F0F0F0F0F0F0F0F0F1F1F7F3F1F3F2F7F3F4F1F2F1F6F2F5F1F2F1F2F1F6F1F2F1F6F5F9F9F9F0F2F1F0F6F9F9F9F9F0F1F0F6F0F0F9F6F8F5F3F7F5F1F9F3F4F8F8F2F2F1F9F0F4F2F6F4C4F2F5F1F2F2F0F1F1F6F5F2F5F8F3F6F0F1F5F0F0F1F2F0F0F0F0F1F0F0F0F0F1D4E3C640E3C5E2E3C1C2C3F1F2F3E3C5E2E3D4E3C6F1F9D4859983888195A3409581948540404040404040404040D4859983888195A3408389A3A840D7C5D9F0F3F1D9F7F5F2F6F0F1F0F3F0F2F8F0F2F0F2F0F0F0F3F0F3F0F2F8F0F4F0F2F0F0F6F0F4F6F0F4";
        Map<String, String> mappedFields = mapFields(trama);

    }

    /**
     * Parsea una trama ISO 8583 y devuelve un mapa con los nombres de los campos y sus valores decodificados.
     *
     * @param iso La trama ISO 8583 completa en formato hexadecimal.
     * @return Un mapa donde la clave es el nombre del campo y el valor es su contenido decodificado.
     * @throws ParserException Si ocurre un error durante el parsing.
     */
    public static Map<String, String> mapFields(String iso) {
        Map<String, String> valuesMap = new HashMap<>();
        StringBuilder isoMessage = new StringBuilder(iso);
        int position = 0;
        boolean containsSecondaryBitmap = false; // Se mantiene para el mensaje de error

        try {
            // Procesar MTI
            position = processFieldData(ISOFieldMastercard.MESSAGE_TYPE, isoMessage, position, valuesMap);

            // Procesar Bitmap Primario
            position = processFieldData(ISOFieldMastercard.BITMAP_PRIMARY, isoMessage, position, valuesMap);
            String binaryBitMapPrimary = valuesMap.get(ISOFieldMastercard.BITMAP_PRIMARY.getName());

            // Verificar y procesar Bitmap Secundario
            if (binaryBitMapPrimary != null && binaryBitMapPrimary.length() > 0 && binaryBitMapPrimary.charAt(0) == '1') {
                position = processFieldData(ISOFieldMastercard.BITMAP_SECONDARY, isoMessage, position, valuesMap);
                containsSecondaryBitmap = true;
            }

            // Concatenar bitmaps
            String fullBitmap = valuesMap.get(ISOFieldMastercard.BITMAP_PRIMARY.getName())
                    .concat(valuesMap.getOrDefault(ISOFieldMastercard.BITMAP_SECONDARY.getName(), ""));

            // Iterar a través de los campos basándose en el bitmap
            // Se comienza la iteración desde el campo 2, ya que el campo 1 es el bitmap secundario
            for (int i = 2; i <= fullBitmap.length(); i++) {
                if (fullBitmap.charAt(i - 1) == '1') { // Verificar si el bit está activo
                    ISOFieldMastercard field = ISOFieldMastercard.getById(i);

                    if (field == null) {
                        LogsTraces.writeInfo("Campo no permitido: " + i + ". No hay mapeo disponible.");
                        throw new ParserException(createMessageError(i, "No hay mapeo disponible"));
                    }

                    position = processFieldData(field, isoMessage, position, valuesMap);
                }
            }
        } catch (ParserException e) {
            throw e; // Relanzar ParserException directamente
        } catch (Exception e) {
            // Capturar otras excepciones y envolverlas en ParserException
            throw new ParserException("No se puede parsear el mensaje ISO: " + e.getMessage() + "|" + ISOUtil.processError(iso, containsSecondaryBitmap), e);
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

            // La estrategia de parsing del campo es ahora responsable de:
            // 1. Leer su propio prefijo de longitud (si es variable).
            // 2. Extraer la data hexadecimal del valor del campo.
            // 3. Decodificar esa data según su tipo.
            // 4. Devolver el valor decodificado y la longitud total de caracteres HEX consumidos.
            ParsedFieldResult result = isoField.getParserStrategy().parse(remainingMessageSegment, isoField);

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
    private static String createMessageError(int fieldId, String message) {
        return "Error en campo " + fieldId + ": " + message;
    }
}

