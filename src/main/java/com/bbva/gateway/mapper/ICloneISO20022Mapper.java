package com.bbva.gateway.mapper;

import com.bbva.gateway.dto.iso20022.ISO20022;
import org.mapstruct.Mapper;
import org.mapstruct.control.DeepClone;
import org.mapstruct.factory.Mappers;

@Mapper(mappingControl = DeepClone.class)
public interface ICloneISO20022Mapper {
    ICloneISO20022Mapper INSTANCE = Mappers.getMapper(ICloneISO20022Mapper.class);

    ISO20022 clone(ISO20022 in);
}