package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.DepartmentDto;

import java.util.List;

public interface DepartmentService
{
    void save(DepartmentDto departmentDto);
    DepartmentDto getByName(String departmentName);
    DepartmentDto getById(long id);
    List<DepartmentDto> getAll();
}
