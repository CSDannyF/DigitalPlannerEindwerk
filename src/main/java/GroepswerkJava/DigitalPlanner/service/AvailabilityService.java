package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.AvailabilityDto;
import GroepswerkJava.DigitalPlanner.model.Availability;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface AvailabilityService {
    List<AvailabilityDto> getAll();

    AvailabilityDto save(AvailabilityDto availabilityDto);

    AvailabilityDto getById(long id);

    AvailabilityDto getByAvailabilityType(String type);

    Availability mapToAvailabilityModel(AvailabilityDto availabilityDto);

    List<AvailabilityDto> getUnavailabilities();

    Optional<Availability> getModelById(Long unavailabilityId);
}
