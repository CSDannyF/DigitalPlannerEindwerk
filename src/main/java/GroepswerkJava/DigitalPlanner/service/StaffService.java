package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.StaffDto;

import java.util.List;

public interface StaffService {
    void saveStaff(StaffDto staffDto);
    StaffDto getById(long id);
    List<StaffDto> getAll();
}
