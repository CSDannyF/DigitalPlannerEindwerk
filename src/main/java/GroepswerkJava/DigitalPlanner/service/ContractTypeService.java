package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.ContractTypeDto;

import java.util.List;

public interface ContractTypeService
{
    void save (ContractTypeDto contractTypeDto);
    ContractTypeDto getById(long id);
    ContractTypeDto getByName(String name);
    List<ContractTypeDto> getAll();
}
