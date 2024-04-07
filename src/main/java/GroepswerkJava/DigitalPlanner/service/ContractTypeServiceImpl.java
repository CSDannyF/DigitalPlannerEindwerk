package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.ContractTypeDto;
import GroepswerkJava.DigitalPlanner.model.ContractType;
import GroepswerkJava.DigitalPlanner.repository.ContractTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Service
public class ContractTypeServiceImpl implements ContractTypeService {

    @Autowired
    private ContractTypeRepository contractTypeRepository;

    @Override
    public List<ContractTypeDto> getAll()
    {
        List<ContractType> contractTypes = contractTypeRepository.findAll();
        return contractTypes.stream()
                .map((contractType) -> mapToContractTypeDto(contractType))
                .collect(Collectors.toList());
    }

    @Override
    public void save(ContractTypeDto contract)
    {
        ContractType contractType = new ContractType();
        contractType.setContractTypeName(contract.getContractTypeName());
        contractType.setHoursPerWeek(contract.getHoursPerWeek());
        contractTypeRepository.save(contractType);
    }

    @Override
    public ContractTypeDto getById(long id)
    {
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
        return mapToContractTypeDto(contractType);
    }

    @Override
    public ContractTypeDto getByName(String name)
    {
        return mapToContractTypeDto(contractTypeRepository.findByContractTypeName(name));
    }

    //protected gebruikt zodat ze enkel kunnen worder aangeroepen in de service klassen.

    protected ContractTypeDto mapToContractTypeDto(ContractType contractType)
    {
        ContractTypeDto contractTypeDto = new ContractTypeDto();
        contractTypeDto.setContractTypeId(contractType.getContractTypeId());
        contractTypeDto.setContractTypeName(contractType.getContractTypeName());
        contractTypeDto.setHoursPerWeek(contractTypeDto.getHoursPerWeek());
        return contractTypeDto;
    }

    protected ContractType mapToContractTypeModel(ContractTypeDto contractTypeDto)
    {
        ContractType contractType = new ContractType();
        contractType.setContractTypeId(contractTypeDto.getContractTypeId());
        contractType.setContractTypeName(contractTypeDto.getContractTypeName());
        contractType.setHoursPerWeek(contractTypeDto.getHoursPerWeek());
        return contractType;
    }
}
