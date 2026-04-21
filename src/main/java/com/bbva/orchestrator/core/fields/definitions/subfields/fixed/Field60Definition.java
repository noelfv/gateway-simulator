package com.bbva.orchestrator.core.fields.definitions.subfields.fixed;

import com.bbva.orchestrator.core.parser.iso8583.ParsedSubFieldResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Qualifier("field60Definition")
public class Field60Definition implements CompositeFieldDefinition {

    @Override
    public String getId() {
        return "60";
    }

    @Override
    public List<ParsedSubFieldResult> getSubFields() {
        return List.of(
                new ParsedSubFieldResult("60.01", "Terminal Type", 1),
                new ParsedSubFieldResult("60.02", "Terminal Entry Cap", 1),
                new ParsedSubFieldResult("60.03", "Chip Condition Code", 1),
                new ParsedSubFieldResult("60.04", "Special Condition Indicator", 1),
                new ParsedSubFieldResult("60.05", "Merchant Group", 2),
                new ParsedSubFieldResult("60.06", "Chip Transaction Indicator", 1),
                new ParsedSubFieldResult("60.07", "Chip Card Authentication Reliability Indicator", 1),
                new ParsedSubFieldResult("60.08", "Mail/Phone/Electronic Commerce and Payment Indicator", 2),
                new ParsedSubFieldResult("60.09", "Cardholder ID Method Indicator", 1),
                new ParsedSubFieldResult("60.10", "Additional Authorization Indicators", 1)

        );
    }
}