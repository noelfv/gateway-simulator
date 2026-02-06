package com.bbva.gui.dto;

import java.util.Map;

public record ParseResult(Map<String, String> fieldsByDescription, Map<String, String> fieldsById) {

}