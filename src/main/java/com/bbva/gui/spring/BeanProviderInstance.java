package com.bbva.gui.spring;

import com.bbva.orchestrator.core.logic.factory.FieldLogicFactory;
import com.bbva.orchestrator.core.mapper.factory.MapperFactory;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;

public record BeanProviderInstance(ParserFactory parserFactory, MapperFactory mapperFactory,
                                   FieldLogicFactory fieldLogicFactory) {

}
