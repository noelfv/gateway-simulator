package com.bbva.orchlib.utils;

public class RulesLocalUtils {
	private static String orchestrations;
    
	private RulesLocalUtils() {}
	
    public static void setOrchestrations(String orchestrations) {
    	RulesLocalUtils.orchestrations = orchestrations;
    }

	public static String getLastOrchestration() {
		if (orchestrations == null) {
			return null;
		}
		String[] splitFunctions = orchestrations.split(",");
		return splitFunctions[splitFunctions.length - 1];
	}
}
