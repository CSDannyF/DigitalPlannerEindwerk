package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.CompanyDto;
import GroepswerkJava.DigitalPlanner.dto.StatisticsDto;

import java.util.List;

public interface CompanyService {
    List<CompanyDto> getAll();

    void save(CompanyDto companyDto);

    StatisticsDto getStatistics();

    CompanyDto getById(long id);
}
