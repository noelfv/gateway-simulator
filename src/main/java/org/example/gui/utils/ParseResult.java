package org.example.gui.utils;

import java.util.Map;

public record ParseResult(Map<String, String> fieldsByDescription, Map<String, String> fieldsById) {

}