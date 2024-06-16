package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.DepartmentDto;

import java.util.List;

public interface DepartmentService {
    DepartmentDto save(DepartmentDto departmentDto);

    DepartmentDto getByDepartmentName(String departmentName);

    DepartmentDto getById(long id);

    List<DepartmentDto> getAll();

    void deleteDepartmentByName(String departmentName);
}
