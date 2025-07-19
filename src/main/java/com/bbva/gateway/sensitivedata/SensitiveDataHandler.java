package com.bbva.gateway.sensitivedata;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.gateway.featuretoggle.sensitivedata.SensitiveData;
import com.bbva.gateway.featuretoggle.sensitivedata.SensitiveDataRule;
import com.bbva.gateway.mapper.ICloneISO20022Mapper;
import com.bbva.gateway.utils.LogsTraces;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.bbva.gateway.rules.RulesCommon.extractValueByMethodSequence;

public class SensitiveDataHandler {

    private static SensitiveData sensitiveData;
    private static final List<String> failedFields = new ArrayList<>();

    private SensitiveDataHandler() {
    }

    public static void setSensitiveData(SensitiveData sensitiveData) {
        SensitiveDataHandler.sensitiveData = sensitiveData;
    }

    public static ISO20022 iso20022Sensitive(ISO20022 iso20022) {

        return iso20022Sensitive(iso20022, "default");
    }


    /**
     * Obfuscate sensitive data in an ISO20022 object
     *
     * @param iso20022    The ISO20022 object
     * @param application The application to get the sensitive data rules
     * @return The ISO20022 object with the sensitive data obfuscated
     */
    public static ISO20022 iso20022Sensitive(ISO20022 iso20022, String application) {
        ISO20022 clone = ICloneISO20022Mapper.INSTANCE.clone(iso20022); // Deep clone
        List<SensitiveDataRule> sensitiveDataRules = sensitiveData.getApplications().get(application);

        for (SensitiveDataRule data : sensitiveDataRules) {
            String[] sequence = data.getField().split("/");
            if (data.getAction().equals("obfuscate")) {
                Object parent = extractParentByMethodSequence(clone, sequence);
                String[] field = {sequence[sequence.length - 1]};
                Object value = extractValueByMethodSequence(parent, field, field[0]);

                if (value == null || value.toString().isEmpty()) {
                    failedFields.add(field[0]);
                } else {
                    String obfuscatedValue = obfuscate(value.toString(), data.getDirection(), data.getCharacter(), data.getStartPosition(), data.getNumberOfChars());
                    data.setValue(obfuscatedValue);
                    updateValueByMethodSequence(parent, field, obfuscatedValue);
                }

            } else {
                deleteByMethodSequence(clone, sequence);
            }
        }
        if (!failedFields.isEmpty()) {
            LogsTraces.writeWarning("[SensitiveDataHandler] Fields not found: " + String.join(", ", failedFields));
            failedFields.clear();
        }
        return clone;
    }


    /**
     * Obfuscate a string
     *
     * @param value         The value to obfuscate
     * @param direction     The direction of the obfuscation
     * @param character     The character to use for obfuscation
     * @param startPosition The start position of the obfuscation
     * @param numberOfChars The number of characters to obfuscate
     * @return The obfuscated string
     */
    public static String obfuscate(String value, String direction, String character, int startPosition, int numberOfChars) {
        if (value == null) return "";

        character = character.substring(0, 1); // Only 1 character for obfuscation needed
        StringBuilder obfuscatedString = new StringBuilder(value);

        if (direction.equals("left")) {
            int start = Math.max(startPosition, 0);
            int end = Math.min(start + numberOfChars, obfuscatedString.length());
            obfuscatedString.replace(start, end, character.repeat(end - start));
        } else {
            int end = Math.max(obfuscatedString.length() - startPosition, 0);
            int start = Math.max(end - numberOfChars, 0);
            obfuscatedString.replace(start, end, character.repeat(end - start));
        }

        return obfuscatedString.toString();
    }


    /**
     * Delete a field from an object by its sequence
     *
     * @param root     The root object
     * @param sequence The sequence of methods to reach the field
     */
    public static void deleteByMethodSequence(Object root, String[] sequence) {
        Object parent = extractParentByMethodSequence(root, sequence);
        String field = sequence[sequence.length - 1];

        if (parent instanceof List<?> resultList) {
            deleteFromListItems(resultList, sequence[sequence.length - 2], field);
        } else if (field.contains("(") && field.contains(")")) {
            deleteItemInList(parent, field);
        } else {
            updateValueByMethodSequence(parent, new String[]{field}, null);
        }
    }



    /**
     * Delete an item from a list by its class name and key
     *
     * @param parent The parent object
     * @param field  The field to delete
     */
    private static void deleteItemInList(Object parent, String field) {
        try {
            String[] extracted = field.replace(")", "").split("\\(");
            String className = extracted[0];
            String key = extracted[1].replace("\"", "");

            String methodName = "get" + className;
            Method method = parent.getClass().getMethod(methodName);
            Object result = method.invoke(parent);

            if (result instanceof List<?> resultList) {
                resultList = resultList.stream()
                        .filter(item -> (className + "DTO").equals(item.getClass().getSimpleName()))
                        .filter(item -> {
                            try {
                                Method getKeyMethod = item.getClass().getMethod("getKey");
                                return !key.equals(getKeyMethod.invoke(item));
                            } catch (Exception e) {
                                return false;
                            }
                        })
                        .collect(Collectors.toCollection(ArrayList::new));

                if (resultList.isEmpty()) {
                    resultList = null;
                }

                methodName = "set" + className;
                method = parent.getClass().getMethod(methodName, List.class);
                method.invoke(parent, resultList);
            }
        } catch (Exception e) {
            failedFields.add(field);
        }
    }


    /**
     * Delete a field from the items on a list
     *
     * @param list      The list of items
     * @param className The class name of the items on the list
     * @param field     The field to delete
     */
    private static void deleteFromListItems(List<?> list, String className, String field) {
        list.stream()
                .filter(item -> (className + "DTO").equals(item.getClass().getSimpleName()))
                .forEach(item -> {
                    try {
                        String methodName = "set" + field;
                        Class<?> clazz = item.getClass().getDeclaredField(StringUtils.uncapitalize(field)).getType();
                        Method method = item.getClass().getMethod(methodName, clazz);
                        method.invoke(item, (Object) null);
                    } catch (Exception e) {
                        failedFields.add(field);
                    }
                });
    }


    /**
     * Update a field in an object by its sequence
     *
     * @param parent   The parent object
     * @param sequence The sequence of methods to reach the field
     * @param newValue The new value to set
     */
    private static void updateValueByMethodSequence(Object parent, String[] sequence, Object newValue) {
        String field = sequence[sequence.length - 1];

        try {
            String methodName = "set" + field;
            Class<?> clazz = parent.getClass().getDeclaredField(StringUtils.uncapitalize(field)).getType();
            Method method = parent.getClass().getMethod(methodName, clazz);
            method.invoke(parent, clazz.cast(newValue));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NullPointerException |
                 NoSuchFieldException e) {
            failedFields.add(field);
        }
    }


    /**
     * Extract the parent object of a field by its sequence
     *
     * @param root     The root object
     * @param sequence The sequence of methods to reach the field
     * @return The parent object
     */
    private static Object extractParentByMethodSequence(Object root, String[] sequence) {
        String[] partsToParent = Arrays.copyOf(sequence, sequence.length - 1);
        String field = sequence[sequence.length - 1];
        return extractValueByMethodSequence(root, partsToParent, field);
    }
}