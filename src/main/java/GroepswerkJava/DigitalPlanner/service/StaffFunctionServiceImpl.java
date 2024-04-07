package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.DepartmentDto;
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
    public void save(StaffFunctionDto staffFunction) {
        StaffFunction function = new StaffFunction();
        function.setFunctionName(staffFunction.getFunctionName());
        function.setFunctionDescription(staffFunction.getFunctionDescription());
        staffFunctionRepository.save(function);
    }

    @Override
    public StaffFunctionDto getById(long id) {
        Optional<StaffFunction> optional = staffFunctionRepository.findById(id);
        StaffFunction staffFunction = null;
        if(optional.isPresent())
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
    public StaffFunctionDto getByName(String name) {
        return mapToStaffFunctionDto(staffFunctionRepository.findByFunctionName(name));
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
