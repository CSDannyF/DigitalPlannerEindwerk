package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.StaffFunctionDto;

import java.util.List;

public interface StaffFunctionService {
    StaffFunctionDto save(StaffFunctionDto staffFunctionDto);

    StaffFunctionDto getById(long id);

    StaffFunctionDto getByStaffFunctionName(String functionName);

    List<StaffFunctionDto> getAll();

    void deleteStaffFunctionByName(String staffFunctionName);
}
