package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.AvailabilityDto;
import GroepswerkJava.DigitalPlanner.model.Availability;
import GroepswerkJava.DigitalPlanner.repository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
//@Service
public class AvailabilityServiceImpl implements AvailabilityService, CommandLineRunner {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    private static final List<String> AVAILABILITIES = Arrays.asList("Available", "Sick", "Absence", "Planned", "OPEN SHIFT");

    @Override
    public List<AvailabilityDto> getAll() {
        List<Availability> availabilityList = availabilityRepository.findAll();
        List<AvailabilityDto> availabilityDtoList = new ArrayList<>();
        for (Availability availability: availabilityList) {
            AvailabilityDto availabilityDto = mapToAvailabilityDto(availability);
            availabilityDtoList.add(availabilityDto);
        }
        return availabilityDtoList;
    }

    @Override
    public void save(AvailabilityDto availabilityDto) {
        availabilityRepository.save(mapToAvailabilityModel(availabilityDto));
    }

    @Override
    public AvailabilityDto getById(long id) {
        Optional<Availability> optional = availabilityRepository.findById(id);
        Availability availability = null;
        if(optional.isPresent()){
            availability = optional.get();
        } else {
            throw new RuntimeException("Availability not found");
        }
        return mapToAvailabilityDto(availability);
    }

    @Override
    public AvailabilityDto getByAvailabilityType(String type) {
        Availability availability = availabilityRepository.findByAvailabilityType(type);
        return mapToAvailabilityDto(availability);
    }

    protected AvailabilityDto mapToAvailabilityDto(Availability availability)
    {
        AvailabilityDto availabilityDto = new AvailabilityDto();
        availabilityDto.setAvailabilityId(availability.getAvailabilityId());
        availabilityDto.setAvailabilityType(availability.getAvailabilityType());
        return availabilityDto;
    }

    @Override
    public Availability mapToAvailabilityModel(AvailabilityDto availabilityDto)
    {
        Availability availability = new Availability();
        availability.setAvailabilityId(availabilityDto.getAvailabilityId());
        availability.setAvailabilityType(availabilityDto.getAvailabilityType());
        return availability;
    }

    @Override
    public List<AvailabilityDto> getUnavailabilities() {
        List<AvailabilityDto> uavailabilities = new ArrayList<>();
        uavailabilities.add(getByAvailabilityType("Sick"));
        uavailabilities.add(getByAvailabilityType("Absence"));
        System.out.println(uavailabilities);


        return uavailabilities;
    }

    @Override
    public Optional<Availability> getModelById(Long unavailabilityId) {
        return availabilityRepository.findById(unavailabilityId);
    }

    private void initializeAvailabilities()
    {
        for (String availability: AVAILABILITIES)
        {
            Availability availability1 = new Availability();
            availability1.setAvailabilityType(availability);
            availabilityRepository.save(availability1);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        if (availabilityRepository.count() == 0)
        {
            initializeAvailabilities();
        }
    }
}
