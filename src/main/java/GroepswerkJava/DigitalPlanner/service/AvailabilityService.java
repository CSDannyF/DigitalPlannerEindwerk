package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.AvailabilityDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AvailabilityService {
    List<AvailabilityDto> getAll();
    void save(AvailabilityDto availabilityDto);
    AvailabilityDto getById(long id);
    AvailabilityDto getByAvailabilityType(String type);
}
