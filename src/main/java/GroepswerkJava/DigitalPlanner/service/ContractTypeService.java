package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.ContractTypeDto;

import java.util.List;

public interface ContractTypeService {
    ContractTypeDto save(ContractTypeDto contractTypeDto);

    ContractTypeDto getById(long id);

    ContractTypeDto getByContractTypeName(String name);

    List<ContractTypeDto> getAll();

    void deleteContractTypeByName(String contractTypeName);
}
