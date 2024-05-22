package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.AvailabilityDto;

import java.util.List;

public interface AvailabilityService {
    List<AvailabilityDto> getAll();
    void save(AvailabilityDto availabilityDto);
    AvailabilityDto getById(long id);
    AvailabilityDto getByAvailabilityType(String type);
}
