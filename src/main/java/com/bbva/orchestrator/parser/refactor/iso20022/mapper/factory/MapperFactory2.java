package com.bbva.orchestrator.parser.refactor.iso20022.mapper.factory;

import com.bbva.orchestrator.parser.refactor.iso20022.mapper.ISO8583ToISO20022Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

//@Component
public class MapperFactory2 {
    private final Map<String, ISO8583ToISO20022Mapper> mappers;

    public MapperFactory2(List<ISO8583ToISO20022Mapper> mapperList) {
        this.mappers = mapperList.stream()
                .collect(Collectors.toMap(
                        m -> m.getClass().getSimpleName().replace("Mapper", ""),
                        m -> m));
    }

    public MapperFactory2(Map<String, ISO8583ToISO20022Mapper> mapperMap) {
        // Convertimos los nombres de los beans en claves limpias: "VisaMapper" → "Visa"
        this.mappers = new ConcurrentHashMap<>();
        mapperMap.forEach((beanName, mapper) -> {
            String simpleName = mapper.getClass().getSimpleName();
            String key = simpleName.replace("ISO8583ToISO20022Mapper", "")
                    .replace("Mapper", "");
            mappers.put(key, mapper);
        });
    }


    /**
     * Obtiene un mapper por nombre de red, o devuelve el default si no existe.
     */
    public ISO8583ToISO20022Mapper getMapper(String network) {
        return mappers.getOrDefault(
                capitalize(network),
                mappers.getOrDefault("Default", null)
        );
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    //REVISAR
   /* public ISO8583ToISO20022Mapper getMapper(String network) {
        return mappers.getOrDefault(network + "Mapper", defaultMapper);
    }*/
}