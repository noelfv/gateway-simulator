package com.bbva.gateway.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogsTraces {

    private static final Logger LOGGER= LoggerFactory.getLogger(LogsTraces.class);

    public static void writeWarning(String message) {
        // Implement logging logic here
        LOGGER.warn("TRACE: " + message);
    }

    public static void writeInfo(String message) {
        // Implement logging logic here
        LOGGER.info("INFO: " + message);
    }


    public static void writeError(String message) {
        LOGGER.error("ERROR: {}", message);
    }

    public static void writeError(String message, Exception e) {
        LOGGER.error("ERROR: {}", message, e);
    }
}
