package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.StaffFunctionDto;
import GroepswerkJava.DigitalPlanner.model.StaffFunction;
import GroepswerkJava.DigitalPlanner.repository.StaffFunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Service
public class StaffFunctionServiceImpl implements StaffFunctionService {

    @Autowired
    private StaffFunctionRepository staffFunctionRepository;

    @Override
    public List<StaffFunctionDto> getAll()
    {
        List<StaffFunction> staffFunctions = staffFunctionRepository.findAll();
        return staffFunctions.stream()
                .map((staffFunction) -> mapToStaffFunctionDto(staffFunction))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteStaffFunctionByName(String staffFunctionName)
    {
        StaffFunction staffFunction = staffFunctionRepository.findByFunctionName(staffFunctionName);
        staffFunctionRepository.delete(staffFunction);
    }

    @Override
    public StaffFunctionDto save(StaffFunctionDto staffFunctionDto)
    {
        StaffFunction staffFunction = new StaffFunction();
        StaffFunction newStaffFunction = new StaffFunction();
        //hier moet er nog nagekeken worden als het object wel uniek is, indien wel krijgt hij geen id, anders niet
        if (staffFunctionRepository.findByFunctionName(staffFunctionDto.getFunctionName()) == null)
        {
            staffFunction = mapToStaffFunctionModel(staffFunctionDto);
            newStaffFunction = staffFunctionRepository.save(staffFunction);
        } else {
            staffFunction = staffFunctionRepository.findByFunctionName(staffFunctionDto.getFunctionName());
            staffFunction.setFunctionName(staffFunctionDto.getFunctionName());
            staffFunction.setFunctionDescription(staffFunctionDto.getFunctionDescription());
            newStaffFunction = staffFunctionRepository.save(staffFunction);
        }
        return mapToStaffFunctionDto(newStaffFunction);
    }

    @Override
    public StaffFunctionDto getById(long id)
    {
        Optional<StaffFunction> optional = staffFunctionRepository.findById(id);
        StaffFunction staffFunction = null;
        if (optional.isPresent())
        {
            staffFunction = optional.get();
        }
        else
        {
            throw new RuntimeException("Not found");
        }
        return mapToStaffFunctionDto(staffFunction);
    }

    @Override
    public StaffFunctionDto getByStaffFunctionName(String staffFunctionName)
    {
        StaffFunction staffFunction = staffFunctionRepository.findByFunctionName(staffFunctionName);
        StaffFunctionDto staffFunctionDto = new StaffFunctionDto();
        if (staffFunction != null)
        {
            staffFunctionDto = mapToStaffFunctionDto(staffFunction);
        }
        return staffFunctionDto;
    }

    //protected gebruikt zodat ze enkel kunnen worder aangeroepen in de service klassen.
    protected StaffFunctionDto mapToStaffFunctionDto(StaffFunction staffFunction)
    {
        StaffFunctionDto staffFunctionDto = new StaffFunctionDto();
        staffFunctionDto.setFunctionId(staffFunction.getFunctionId());
        staffFunctionDto.setFunctionName(staffFunction.getFunctionName());
        staffFunctionDto.setFunctionDescription(staffFunction.getFunctionDescription());
        return staffFunctionDto;
    }

    protected StaffFunction mapToStaffFunctionModel(StaffFunctionDto staffFunctionDto)
    {
        StaffFunction staffFunction = new StaffFunction();
        staffFunction.setFunctionId(staffFunctionDto.getFunctionId());
        staffFunction.setFunctionName(staffFunctionDto.getFunctionName());
        staffFunction.setFunctionDescription(staffFunctionDto.getFunctionDescription());
        return staffFunction;
    }
}
