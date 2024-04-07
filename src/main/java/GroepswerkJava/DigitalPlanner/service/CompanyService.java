package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.CompanyDto;

import java.util.List;

public interface CompanyService {
    List<CompanyDto> getAll();

    void save(CompanyDto companyDto);

    CompanyDto getById(long id);
}
