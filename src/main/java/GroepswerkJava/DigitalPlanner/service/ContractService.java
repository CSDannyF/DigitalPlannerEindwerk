package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.model.Company;
import GroepswerkJava.DigitalPlanner.model.ContractType;

import java.util.List;

public interface ContractService {
    List<ContractType> getAllContracts();

    void saveContract(ContractType contractType);

    Company getContractById(long id);
}
