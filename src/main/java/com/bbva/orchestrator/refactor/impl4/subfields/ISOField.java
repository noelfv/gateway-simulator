package com.bbva.orchestrator.refactor.impl4.subfields;

public interface ISOField extends IFieldDefinition{
    int getId(); // Mantiene el ID como int para campos principales
    // getIdentifier() se implementará en el enum ISOFieldMastercard
}
