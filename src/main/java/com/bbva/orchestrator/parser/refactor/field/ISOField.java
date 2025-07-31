package com.bbva.orchestrator.parser.refactor.field;

public interface ISOField extends IFieldDefinition {
    int getId(); // Mantiene el ID como int para campos principales
    // getIdentifier() se implementará en el enum ISOFieldMastercard
}
