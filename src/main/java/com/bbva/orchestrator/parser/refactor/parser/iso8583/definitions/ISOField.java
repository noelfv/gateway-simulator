package com.bbva.orchestrator.parser.refactor.parser.iso8583.definitions;

public interface ISOField extends IFieldDefinition {
    int getId(); // Mantiene el ID como int para campos principales
    // getIdentifier() se implementará en el enum ISOFieldMastercard
}
