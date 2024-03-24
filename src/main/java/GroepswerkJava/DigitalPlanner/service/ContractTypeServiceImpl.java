package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.model.ContractType;
import GroepswerkJava.DigitalPlanner.repository.ContractTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Component
@Service
public class ContractTypeServiceImpl implements IService<ContractType> {

    @Autowired
    private ContractTypeRepository contractTypeRepository;

    @Override
    public List<ContractType> getAll() {
        return this.contractTypeRepository.findAll();
    }

    @Override
    public void save(ContractType contractType) {
        this.contractTypeRepository.save(contractType);
    }

    @Override
    public ContractType getById(long id) {
        Optional<ContractType> optional = contractTypeRepository.findById(id);
        ContractType contractType = null;
        if(optional.isPresent())
        {
            contractType = optional.get();
        }
        else
        {
            throw new RuntimeException("Not found");
        }
        return contractType;
    }
}
