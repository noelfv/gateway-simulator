package com.bbva.orchestrator.network.visa.subfields;

import java.util.Collections;
import java.util.Map;
import static com.bbva.orchestrator.parser.common.ISO8583SubFieldsParser.ofEntries;

public class ISOVisaSubfieldsUtil {

    private ISOVisaSubfieldsUtil() {
    }

    public static final Map<String, Integer> processingCode =
            Collections.unmodifiableMap(ofEntries(
                    // k: id, v: length in characters
                    Map.entry("01", 2),
                    Map.entry("02", 2),
                    Map.entry("03", 2)
            ));

    public static final Map<String, Integer> pointServiceEntryMode =
            Collections.unmodifiableMap(ofEntries(
                    // k: id, v: length in characters
                    Map.entry("01", 2),
                    Map.entry("02", 1),
                    Map.entry("03", 1)
            ));
    public static final Map<String, Integer> posTerminalDataSubFields =
            Collections.unmodifiableMap(ofEntries(
                    // k: id, v: length in characters
                    Map.entry("01", 1),
                    Map.entry("02", 1),
                    Map.entry("03", 1),
                    Map.entry("04", 1),
                    Map.entry("05", 2),
                    Map.entry("06", 1),
                    Map.entry("07", 1),
                    Map.entry("08", 2),
                    Map.entry("09", 1),
                    Map.entry("10", 1)
            ));

    public static final Map<String, Integer> postalCodeSubFields =
            Collections.unmodifiableMap(ofEntries(
                    // k: id, v: length in characters
                    Map.entry("01", 2),
                    Map.entry("02", 16),
                    Map.entry("03", 8),
                    Map.entry("04", 2),
                    Map.entry("05", 2),
                    Map.entry("06", 2),
                    Map.entry("07", 52),
                    Map.entry("08", 6),
                    Map.entry("09", 2),
                    Map.entry("10", 6),
                    Map.entry("11", 2),
                    Map.entry("12", 2),
                    Map.entry("13", 2),
                    Map.entry("14", 12),
                    Map.entry("15", 2),
                    Map.entry("16", 4),
                    Map.entry("17", 30),
                    Map.entry("18", 2),
                    Map.entry("19", 4),
                    Map.entry("20", 10),
                    Map.entry("21", 8),
                    Map.entry("22", 12),
                    Map.entry("23", 4),
                    Map.entry("24", 12),
                    Map.entry("25", 2)
            ));

    public static final Map<String, Integer> networkDataSubFields =
            Collections.unmodifiableMap(ofEntries(
                    // k: id, v: length in characters
                    Map.entry("01", 4),
                    Map.entry("02", 4),
                    Map.entry("03", 4),
                    Map.entry("04", 4),
                    Map.entry("05", 6),
                    Map.entry("06", 14),
                    Map.entry("07", 16),
                    Map.entry("08", 8),
                    Map.entry("09", 28),
                    Map.entry("10", 26),
                    Map.entry("11", 2),
                    Map.entry("12", 60),
                    Map.entry("13", 6),
                    Map.entry("14", 72),
                    Map.entry("15", 16),
                    Map.entry("16", 0),
                    Map.entry("17", 0),
                    Map.entry("18", 4),
                    Map.entry("19", 6),
                    Map.entry("20", 0),
                    Map.entry("21", 2)
            ));

}
