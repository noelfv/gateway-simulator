package com.bbva.orchestrator.parser.refactor.parser.iso8583.definitions;


public interface ISOSubField extends IFieldDefinition {
    String getId(); // Mantiene el ID como int para campos principales
    //String getIdentifier();// se implementará en el enum ISOFieldMastercard
}
