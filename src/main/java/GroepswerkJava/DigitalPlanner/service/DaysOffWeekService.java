package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.DaysOffWeekDto;

import java.util.List;

public interface DaysOffWeekService {
    DaysOffWeekDto save(DaysOffWeekDto daysOffWeekDto);

    DaysOffWeekDto getById(long id);

    DaysOffWeekDto getByName(String name);

    List<DaysOffWeekDto> getAll();
}
