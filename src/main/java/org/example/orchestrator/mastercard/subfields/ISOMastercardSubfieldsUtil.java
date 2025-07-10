package org.example.orchestrator.mastercard.subfields;

import java.util.Collections;
import java.util.Map;
import static java.util.Map.ofEntries;

public class ISOMastercardSubfieldsUtil {
    private ISOMastercardSubfieldsUtil() {
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
                Map.entry("02", 1)
            ));


    public static final Map<String, Integer> posCardIssuerSubFields =
            Collections.unmodifiableMap(ofEntries(
                    // k: id, v: length in characters
                    Map.entry("01", 1),
                    Map.entry("02", 1),
                    Map.entry("03", 1),
                    Map.entry("04", 1),
                    Map.entry("05", 1),
                    Map.entry("06", 1),
                    Map.entry("07", 1),
                    Map.entry("08", 1),
                    Map.entry("09", 1),
                    Map.entry("10", 1),
                    Map.entry("11", 1),
                    Map.entry("12", 2),
                    Map.entry("13", 3),
                    Map.entry("14", 0)
            ));

    public static final Map<String, Integer> additionalAmountsSubFields =
            Collections.unmodifiableMap(ofEntries(
                    // k: id, v: length in characters
                    Map.entry("01", 2),
                    Map.entry("02", 2),
                    Map.entry("03", 3),
                    Map.entry("04", 1),
                    Map.entry("05", 12)
            ));
    public static final Map<String, Integer> additionalDataRetailerSubFields =
            Collections.unmodifiableMap(ofEntries(
                    // k: id, v: length in characters
                    Map.entry("05", 1),
                    Map.entry("09", 1),
                    Map.entry("11", -1), // Subelement 11 (Key Exchange Block Data [Double-Length Keys]) -> cortar con map
                    Map.entry("13", -1), // Subelement 13 (Mastercard Hosted Mobile Phone Top-Up Request Data) -> cortar con map
                    Map.entry("15", -1), // Subelement 13 (Authorization System Advice Date and Time) -> cortar con map
                    Map.entry("18", 1),
                    Map.entry("21", 1),
                    Map.entry("22", 1),
                    Map.entry("23", -1), // Subelement 23 (Payment Initiation Channel) -> cortar con map
                    Map.entry("24", 1),
                    Map.entry("25", 1),
                    Map.entry("26", -1), // Subelement (Wallet Program Data) -> cortar con map
                    Map.entry("27", 1),
                    Map.entry("33", 1),
                    Map.entry("34", -1), // subelemet (ATC Information) -> cortar con map
                    Map.entry("36", 1),
                    Map.entry("37", 1),
                    Map.entry("40", 1),
                    Map.entry("41", 1),
                    Map.entry("42", 1),
                    Map.entry("48", 1),
                    Map.entry("49", -1), // subelemet (Time Validation Information) -> cortar con map
                    Map.entry("50", 1),
                    Map.entry("51", -1), // subelement  (Merchant On-behalf Services) -> cortar con map
                    Map.entry("53", -1), // subelement  (E-ID Request Code) -> cortar con map
                    Map.entry("55", 1),
                    Map.entry("56", -1), // subelement -> cortar con map
                    Map.entry("57", -1), // subelement -> cortar con map
                    Map.entry("58", -1), // subelement -> cortar con map
                    Map.entry("60", 1),
                    Map.entry("61", -1), // subelement -> cortar con map
                    Map.entry("62", 1),
                    Map.entry("64", -1), // subelement -> cortar con map
                    Map.entry("65", -1), // subelement -> cortar con map
                    Map.entry("66", 1),
                    Map.entry("67", 1),
                    Map.entry("68", 1),
                    Map.entry("71", -1), // subelement -> cortar con map
                    Map.entry("74", -1), // subelement -> cortar con map
                    Map.entry("75", 1),
                    Map.entry("78", -1), // subelement -> cortar con map
                    Map.entry("79", -1), // subelement -> cortar con map
                    Map.entry("93", -1)  // subelement -> cortar con map
            ));
    public static final Map<String, Map<String, Integer>> internalSubFields48 =
            Collections.unmodifiableMap(ofEntries(
                    // k: id, v: length in characters
                    Map.entry("11",
                            Collections.unmodifiableMap(ofEntries(
                                    Map.entry("01", 2),
                                    Map.entry("02", 2),
                                    Map.entry("03", 2),
                                    Map.entry("04", 32),
                                    Map.entry("05", 16)
                            ))
                    ),
                    Map.entry("13",
                            Collections.unmodifiableMap(ofEntries(
                                    Map.entry("01", 17),
                                    Map.entry("02", 30)
                            ))
                    ),
                    Map.entry("15",
                            Collections.unmodifiableMap(ofEntries(
                                    Map.entry("01", 4),
                                    Map.entry("02", 6)
                            ))
                    ),
                    Map.entry("23",
                            Collections.unmodifiableMap(ofEntries(
                                    Map.entry("01", 2)
                            ))
                    ),
                    Map.entry("26",
                            Collections.unmodifiableMap(ofEntries(
                                    Map.entry("01", 3)
                            ))
                    ),
                    Map.entry("34",
                            Collections.unmodifiableMap(ofEntries(
                                    Map.entry("01", 5),
                                    Map.entry("02", 5),
                                    Map.entry("03", 1)
                            ))
                    ),
                    Map.entry("49",
                            Collections.unmodifiableMap(ofEntries(
                                    Map.entry("01", 8),
                                    Map.entry("02", 5),
                                    Map.entry("03", 2)
                            ))
                    ),
                    Map.entry("51",
                            Collections.unmodifiableMap(ofEntries(
                                    Map.entry("01", 2),
                                    Map.entry("02", 1),
                                    Map.entry("03", 1)
                            ))
                    ),
                    Map.entry("53",
                            Collections.unmodifiableMap(ofEntries(
                                    Map.entry("01", 2)
                            ))
                    ),
                    Map.entry("56",
                            Collections.unmodifiableMap(ofEntries(
                                    Map.entry("01", 3),
                                    Map.entry("02", 3)
                            ))
                    ),
                    Map.entry("57",
                            Collections.unmodifiableMap(ofEntries(
                                    Map.entry("01", 3),
                                    Map.entry("02", 3)
                            ))
                    ),
                    Map.entry("58",
                            Collections.unmodifiableMap(ofEntries(
                                    Map.entry("01", 4),
                                    Map.entry("02", 6),
                                    Map.entry("03", 12),
                                    Map.entry("04", 2),
                                    Map.entry("05", 2),
                                    Map.entry("06", 2),
                                    Map.entry("07", 1),
                                    Map.entry("08", 4)
                            ))
                    ),
                    Map.entry("61",
                            Collections.unmodifiableMap(ofEntries(
                                    Map.entry("01", 1),
                                    Map.entry("02", 1),
                                    Map.entry("03", 1)
                            ))
                    ),
                    Map.entry("64",
                            Collections.unmodifiableMap(ofEntries(
                                    Map.entry("01", 2),
                                    Map.entry("02", 2)
                            ))
                    ),
                    Map.entry("65",
                            Collections.unmodifiableMap(ofEntries(
                                    Map.entry("01", 1),
                                    Map.entry("02", 1)
                            ))
                    ),
                    Map.entry("71",
                            Collections.unmodifiableMap(ofEntries(
                                    Map.entry("01", 2),
                                    Map.entry("02", 1),
                                    Map.entry("03", 1)
                            ))
                    ),
                    Map.entry("74",
                            Collections.unmodifiableMap(ofEntries(
                                    Map.entry("01", 2),
                                    Map.entry("02", 1)
                            ))
                    ),
                    Map.entry("78",
                            Collections.unmodifiableMap(ofEntries(
                                    Map.entry("01", 1),
                                    Map.entry("02", 1),
                                    Map.entry("03", 1),
                                    Map.entry("04", 1),
                                    Map.entry("05", 1),
                                    Map.entry("06", 1)
                            ))
                    ),
                    Map.entry("79",
                            Collections.unmodifiableMap(ofEntries(
                                    Map.entry("01", 1),
                                    Map.entry("02", 2),
                                    Map.entry("03", 1),
                                    Map.entry("04", 1)
                            ))
                    ),
                    Map.entry("93",
                            Collections.unmodifiableMap(ofEntries(
                                    Map.entry("01", 2),
                                    Map.entry("02", 17)
                            ))
                    )
            ));
}
