package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.DepartmentDto;
import GroepswerkJava.DigitalPlanner.dto.StaffFunctionDto;

import java.util.List;

public interface StaffFunctionService
{
    void save(StaffFunctionDto staffFunctionDto);
    StaffFunctionDto getById(long id);
    StaffFunctionDto getByName(String functionName);

    List<StaffFunctionDto> getAll();
}
