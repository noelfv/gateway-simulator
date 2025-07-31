package com.bbva.orchlib.rules;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.gateway.rules.Operations;
import com.bbva.gateway.utils.LogsTraces;
import com.bbva.orchlib.enums.RuleType;
import com.bbva.orchlib.featuretoggle.rulesglobal.ConditionGlobal;
import com.bbva.orchlib.featuretoggle.rulesglobal.RulesGlobalsListPreLoad;
import com.bbva.orchlib.featuretoggle.rulesglobal.RulesGlobalsPreLoad;
import com.bbva.orchlib.featuretoggle.ruleslocal.ConditionLocal;
import com.bbva.orchlib.featuretoggle.ruleslocal.RulesLocalsListPreLoad;
import com.bbva.orchlib.featuretoggle.ruleslocal.RulesLocalsPreLoad;

import java.util.*;

import static com.bbva.gateway.rules.RulesCommon.extractValueByMethodSequence;



public class RulesCommon {


    private static RulesGlobalsListPreLoad rulesGlobalsList;
    private static RulesLocalsListPreLoad rulesValidationsLocalsList;
    private static RulesLocalsListPreLoad rulesOrchLocalsList;
    private static Map<String, String> filterLabelsLocals;
    private static Map<String, String> filterLabelsGlobals;
    private RulesCommon() {
    }

    public static void setRulesGlobalLoadList(RulesGlobalsListPreLoad rulesGlobalsList) {
        RulesCommon.rulesGlobalsList = rulesGlobalsList;
    }

    public static void setRulesValidationsLocalLoadList(RulesLocalsListPreLoad rulesValidationsLocalsList) {
        RulesCommon.rulesValidationsLocalsList = rulesValidationsLocalsList;
    }
    public static void setRulesOrchLocalLoadList(RulesLocalsListPreLoad rulesOrchLocalsList) {
        RulesCommon.rulesOrchLocalsList = rulesOrchLocalsList;
    }

    public static void setFilterLabelsLocal(Map<String, String> filterLabelsLocals) {
        RulesCommon.filterLabelsLocals = filterLabelsLocals;
    }

    public static void setFilterLabelsGlobal(Map<String, String> filterLabelsGlobals) {
        RulesCommon.filterLabelsGlobals = filterLabelsGlobals;
    }



    /**
     * Filtra la lista de reglas proporcionada por red y devuelve una nueva lista que contiene solo las reglas
     * que pertenecen a la red especificada.
     *
     * @param rulesList La lista de reglas a filtrar.
     * @param network   La red por la cual filtrar las reglas.
     * @param <T>       El tipo de elementos en la lista de reglas.
     * @return Una lista filtrada que contiene solo las reglas que pertenecen a la red especificada.
     */
    private static <T> List<T> filterRulesListByNetwork(List<T> rulesList, String network) {
        return rulesList.stream()
                .filter(rule -> {
                    if (rule instanceof RulesLocalsPreLoad localsRule) {
                        return localsRule.getNetwork().equalsIgnoreCase(network);
                    } else {
                        RulesGlobalsPreLoad globalsRule = (RulesGlobalsPreLoad) rule;
                        return globalsRule.getNetwork().equalsIgnoreCase(network);
                    }
                })
                .toList();
    }

    /**
     * Filtra la lista de reglas globales por la red especificada.
     * Este método devuelve una instancia de {@link RulesGlobalsListPreLoad} que contiene solo las reglas globales
     * que pertenecen a la red especificada.
     *
     * @param network la red por la que se desea filtrar las reglas globales
     * @return una instancia de {@link RulesGlobalsListPreLoad} que contiene las reglas globales filtradas
     */
    public static RulesGlobalsListPreLoad filterRulesGlobalsListByNetwork(String network) {
        List<RulesGlobalsPreLoad> filteredRules = rulesGlobalsList.getRulesList();
        List<RulesGlobalsPreLoad> filtered = filterRulesListByNetwork(filteredRules, network);
        RulesGlobalsListPreLoad filteredRulesList = new RulesGlobalsListPreLoad();
        filteredRulesList.setRulesList(filtered);
        return filteredRulesList;
    }


    /**
     * Filtra la lista de reglas locales por la red especificada.
     * Este método devuelve una instancia de {@link RulesLocalsListPreLoad} que contiene solo las reglas locales
     * que pertenecen a la red especificada.
     *
     * @param network la red por la que se desea filtrar las reglas locales
     * @return una instancia de {@link RulesLocalsListPreLoad} que contiene las reglas locales filtradas
     */
    public static RulesLocalsListPreLoad filterRulesLocalsListByNetwork(String network, RuleType ruleType) {
        List<RulesLocalsPreLoad> filteredRules = null;
        if(ruleType == RuleType.ORCHESTRATIONS){
            filteredRules = rulesOrchLocalsList.getRulesList();
        }else if(ruleType == RuleType.VALIDATIONS){
            filteredRules = rulesValidationsLocalsList.getRulesList();
        }

        assert filteredRules != null;
        List<RulesLocalsPreLoad> filtered = filterRulesListByNetwork(filteredRules, network);
        RulesLocalsListPreLoad filteredRulesList = new RulesLocalsListPreLoad();
        filteredRulesList.setRulesList(filtered);
        return filteredRulesList;
    }



    /**
     * Aplica las condiciones de las reglas locales a un objeto ISO20022.
     * Este método procesa las condiciones de cada regla local en la lista proporcionada
     * y actualiza los resultados de las condiciones procesadas en las reglas locales.
     *
     * @param iso20022 el objeto ISO20022 al que se aplicarán las condiciones de las reglas locales
     * @param rulesList la lista de reglas locales que se aplicarán al objeto ISO20022
     * @return la lista de reglas locales original con los resultados de las condiciones actualizados
     */
    public static RulesLocalsListPreLoad applyRuleLocalCondition(ISO20022 iso20022, RulesLocalsListPreLoad rulesList) {
        Map<String, String> conditionResults = new HashMap<>();
        for (RulesLocalsPreLoad rule : rulesList.getRulesList()) {
            for (ConditionLocal condition : rule.getCondition()) {
                String result = getResultForCondition(iso20022, conditionResults, condition, filterLabelsLocals);
                if (result != null) {
                    condition.setFilterLabelProcess(result);
                }
            }
        }
        return rulesList;
    }



    /**
     * Aplica las condiciones de las reglas globales a un objeto ISO20022.
     * Este método procesa las condiciones de cada regla global en la lista proporcionada
     * y actualiza los resultados de las condiciones procesadas en las reglas globales.
     *
     * @param iso20022 el objeto ISO20022 al que se aplicarán las condiciones de las reglas globales
     * @param rulesList la lista de reglas globales que se aplicarán al objeto ISO20022
     * @return la lista de reglas globales original con los resultados de las condiciones actualizados
     */
    public static RulesGlobalsListPreLoad applyRuleGlobalCondition(ISO20022 iso20022, RulesGlobalsListPreLoad rulesList) {
        Map<String, String> conditionResults = new HashMap<>();
        for (RulesGlobalsPreLoad rule : rulesList.getRulesList()) {
            for (ConditionGlobal condition : rule.getCondition()) {
                String result = getResultForCondition(iso20022, conditionResults, condition, filterLabelsGlobals);
                if (result != null) {
                    condition.setFilterLabelProcess(result);
                }
            }
        }
        return rulesList;
    }

    /**
     * Recupera el resultado para una condición dada desde el mapa conditionResults o lo calcula basándose en el mapa filterLabels.
     *
     * @param iso20022          El objeto ISO20022 que contiene los datos a ser utilizados para la evaluación.
     * @param conditionResults  Un mapa que contiene los resultados precalculados para las condiciones.
     * @param condition         La condición para la cual se desea recuperar el resultado.
     * @param filterLabels      Un mapa que contiene etiquetas para extraer valores del objeto ISO20022.
     * @return                  El resultado de la evaluación de la condición, o null si no se encuentra o no se puede calcular.
     */
    private static String getResultForCondition(ISO20022 iso20022, Map<String, String> conditionResults, Object condition, Map<String, String> filterLabels) {
        String conditionName = getConditionName(condition);
        String result = null;
        if (conditionResults.containsKey(conditionName)) {
            return conditionResults.get(conditionName);
        } else {
            String value = filterLabels.get(conditionName);
            if (value != null) {
                String[] parts = value.split("/");
                Object resultObject = extractValueByMethodSequence(iso20022, parts, conditionName);
                if (resultObject != null) {
                    result = resultObject.toString();
                }
                conditionResults.put(conditionName, result);
                return result;
            }
        }
        return null;
    }



    /**
     * Obtiene el nombre de la condición proporcionada.
     *
     * @param condition La condición de la cual se desea obtener el nombre.
     * @return El nombre de la condición si es local o global; de lo contrario, devuelve null.
     */
    private static String getConditionName(Object condition) {
        if (condition instanceof ConditionLocal conditionLocal) {
            return conditionLocal.getName();
        } else {
            ConditionGlobal conditionGlobal = (ConditionGlobal) condition;
            return conditionGlobal.getName();
        }
    }




    /**
     * Obtiene las funciones de la lista de reglas proporcionada.
     * Si rulesList es una instancia de {@link RulesLocalsListPreLoad}, se agregarán funciones desde la lista de locales;
     * de lo contrario, se agregarán desde la lista de globales.
     *
     * @param rulesList La lista de reglas de la cual se obtendrán las funciones.
     * @return Una lista de cadenas que representan las funciones extraídas de la lista de reglas.
     */
    public static List<String> getFunctionsRules(Object rulesList) {
        List<String> functions = new ArrayList<>();

        if (rulesList instanceof RulesLocalsListPreLoad localsList) {
            addFunctionsFromRulesList(localsList.getRulesList(), functions, true);
        } else {
            RulesGlobalsListPreLoad globalsList = (RulesGlobalsListPreLoad) rulesList;
            addFunctionsFromRulesList(globalsList.getRulesList(), functions, false);
        }

        return functions;
    }

    /**
     * Extrae los nombres de funciones de una lista de reglas y los añade a una lista de funciones,
     * si se cumplen ciertas condiciones.
     *
     * <p>Este método procesa la lista de reglas proporcionada, evaluando las condiciones asociadas a
     * cada regla y extrayendo los nombres de las funciones si se cumplen dichas condiciones. Los nombres
     * de las funciones extraídas se añaden a la lista de funciones proporcionada.
     *
     * @param rules una lista de reglas de tipo genérico.
     * @param functions una lista de cadenas donde se añadirán los nombres de funciones extraídos.
     * @param isLocal un booleano que indica si las reglas son locales o globales.
     *                Si es true, se trata de reglas locales; si es false, son reglas globales.
     */
    private static void addFunctionsFromRulesList(List<?> rules, List<String> functions, boolean isLocal) {
        for (Object rule : rules) {
            boolean conditionsMet;
            String function;

            if (isLocal) {
                RulesLocalsPreLoad localRule = (RulesLocalsPreLoad) rule;
                conditionsMet = evaluateAllConditionsLocal(localRule.getCondition());
                function = localRule.getFunction();
            } else {
                RulesGlobalsPreLoad globalRule = (RulesGlobalsPreLoad) rule;
                conditionsMet = evaluateAllConditionsGlobal(globalRule.getCondition());
                function = globalRule.getFunction();
            }

            if (conditionsMet) {
                String[] splitFunctions = function.split(",");
                Collections.addAll(functions, splitFunctions);
                break;
            }
        }
    }

    /**
     * Evalúa todas las condiciones locales en la lista proporcionada.
     * Este método evalúa cada condición local en la lista y devuelve true si todas las condiciones se cumplen, de lo contrario, devuelve false.
     *
     * @param conditions la lista de condiciones locales que se evaluarán
     * @return true si todas las condiciones se cumplen, false de lo contrario
     */
    private static boolean evaluateAllConditionsLocal(List<ConditionLocal> conditions) {
        for (ConditionLocal condition : conditions) {
            if (!evaluateCondition(condition)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Evalúa todas las condiciones globales en la lista proporcionada.
     * Este método evalúa cada condición global en la lista y devuelve true si todas las condiciones se cumplen, de lo contrario, devuelve false.
     *
     * @param conditions la lista de condiciones globales que se evaluarán
     * @return true si todas las condiciones se cumplen, false de lo contrario
     */
    private static boolean evaluateAllConditionsGlobal(List<ConditionGlobal> conditions) {
        for (ConditionGlobal condition : conditions) {
            if (!evaluateCondition(condition)) {
                return false;
            }
        }
        return true;
    }



    /**
     * Evalúa la condición proporcionada y devuelve un booleano que indica si la condición es verdadera o falsa.
     *
     * @param condition La condición a evaluar, que puede ser una instancia de {@link ConditionLocal} o {@link ConditionGlobal}.
     * @return true si la condición se cumple, false en caso contrario.
     */
    public static boolean evaluateCondition(Object condition) {
        String value;
        String filterLabelProcess;
        String operation;

        if (condition instanceof ConditionLocal conditionLocal) {
            value = conditionLocal.getValue();
            filterLabelProcess = conditionLocal.getFilterLabelProcess();
            operation = conditionLocal.getOperation();
        } else {
            ConditionGlobal conditionGlobal = (ConditionGlobal) condition;
            value = conditionGlobal.getValue();
            filterLabelProcess = conditionGlobal.getFilterLabelProcess();
            operation = conditionGlobal.getOperation();
        }

        return switch (operation) {
            case "Equals" -> Operations.equals(filterLabelProcess, value);
            case "NotEquals" -> Operations.notEquals(filterLabelProcess, value);
            case "Greater" -> Operations.greater(filterLabelProcess, value);
            case "Lower" -> Operations.less(filterLabelProcess, value);
            case "StartWith" -> Operations.startwith(filterLabelProcess, value);
            case "EndWith" -> Operations.endwith(filterLabelProcess, value);
            case "In" -> Operations.in(filterLabelProcess, value);
            case "NotIn" -> Operations.notIn(filterLabelProcess, value);
            case "Range" -> Operations.range(filterLabelProcess, value);
            default -> {
                LogsTraces.writeError("[RulesCommon] No se encontro condición a evaluar. Revise las reglas a implementar.");
                yield false;
            }
        };
    }

}
