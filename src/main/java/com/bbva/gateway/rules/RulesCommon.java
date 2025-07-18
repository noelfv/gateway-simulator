package com.bbva.gateway.rules;

import com.bbva.gateway.utils.LogsTraces;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class RulesCommon {


    private RulesCommon() {
    }
    /**
     * Obtiene un valor resultante al aplicar una secuencia de métodos de acceso a un objeto.
     * Este método aplica una secuencia de métodos de acceso (getter) al objeto proporcionado,
     * utilizando los nombres de los métodos especificados en la matriz de partes.
     *
     * @param result El objeto del cual se obtendrá el valor.
     * @param parts  Una matriz de cadenas que contiene los nombres de los métodos de acceso
     *               a aplicar secuencialmente al objeto.
     * @return El valor resultante después de aplicar los métodos de acceso especificados al objeto.
     */
    public static Object extractValueByMethodSequence(Object result, String[] parts, String filterType) {
        if (result == null) {
            return null;
        }

        try {
            String key = "";
            String className = "";

            for (String part : parts) {

                // Verificar si tiene un formato especial
                if (part.contains("(") && part.contains(")")) {
                    String[] extracted = extractClassNameAndKey(part);
                    className = extracted[0];
                    key = extracted[1];
                    part = className;
                }

                String methodName = "get" + part;
                Method method = result.getClass().getMethod(methodName);
                result = method.invoke(result);

                result = findItemInList(result, className, key);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NullPointerException e) {
            // Check if the calling class is SensitiveDataHandler
            if (!isCalledBySensitiveDataHandler()) {
                LogsTraces.writeWarning("[RulesCommon] No value for filterlabel: " + filterType);                }

            result = null;
        }
        return result;
    }





    /** Obtiene el nombre de la Clase y la clave que esta en la configuracion de la filterLabels
     *
     * @param part La cadena de texto con formato especial de donde se obtendra el valor
     * @return Un array de dos elementos donde el primer elemento es el nombre de la clase
     *          y el segundo elemento es la clave.
     */
    private static String[] extractClassNameAndKey(String part) {
        String key = part.substring(part.indexOf("\"") + 1, part.lastIndexOf("\""));
        String className = part.substring(0, part.indexOf("("));
        return new String[]{className, key};
    }



    /** Busca un elemento en una lista que coincida con el nombre de clase y clave proporcionados.
     * El nombre de clase esperado debe tener el sufijo "DTO".
     *
     * @param result  El objeto que debe ser una lista.
     * @param className El nombre de la clase a buscar.
     * @param key La clave que debe coincidir con el valor retornado por el método "getKey".
     * @return El primer elemento que coincida con el nombre de la clase y la clave,
     *          o regresa el objeto que recibe si no se encuentra ninguno.
     */
    private static Object findItemInList(Object result, String className, String key) {
        if (result instanceof List<?> resultList) {
            Optional<?> foundItem = resultList.stream()
                    .filter(item -> (className + "DTO").equals(item.getClass().getSimpleName()))
                    .filter(item -> {
                        try {
                            Method getKeyMethod = item.getClass().getMethod("getKey");
                            return key.equals(getKeyMethod.invoke(item));
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .findFirst();

            if (foundItem.isPresent())
                result = foundItem.get();
        }
        return result;
    }


    /**
     * Verifica si el método está siendo llamado por la clase SensitiveDataHandler.
     * Esto se utiliza para evitar registrar logs ya que se registran en la clase SensitiveDataHandler.
     *
     * @return true si el método es llamado por SensitiveDataHandler, false de lo contrario.
     */
    private static boolean isCalledBySensitiveDataHandler() {
        String sensitiveDataClass = "com.bbva.gateway.sensitivedata.SensitiveDataHandler";
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (sensitiveDataClass.equals(element.getClassName())) {
                return true;
            }
        }
        return false;
    }

}