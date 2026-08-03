package com.panonit.ingestion_service.mapper;

import com.panonit.ingestion_service.dto.CreateEnergyUsageRequestDto;
import com.panonit.kafka.event.EnergyUsageEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EnergyUsageMapper {

    EnergyUsageEvent toEvent(final CreateEnergyUsageRequestDto createEnergyUsageRequestDto);
}
