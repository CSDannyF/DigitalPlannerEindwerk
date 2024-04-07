package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.DepartmentDto;
import GroepswerkJava.DigitalPlanner.model.Department;
import GroepswerkJava.DigitalPlanner.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public List<DepartmentDto> getAll()
    {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map((department) -> mapToDepartmentDto(department))
                .collect(Collectors.toList());
    }

    @Override
    public void save(DepartmentDto departmentDto)
    {
        Department department = new Department();
        department.setDepartmentName(departmentDto.getDepartmentName());
        departmentRepository.save(department);
    }

    @Override
    public DepartmentDto getByName(String name) {
        return mapToDepartmentDto(departmentRepository.findByDepartmentName(name));
    }

    @Override
    public DepartmentDto getById(long id) {
        Optional<Department> optional = departmentRepository.findById(id);
        Department department = null;
        if(optional.isPresent())
        {
            department = optional.get();
        }
        else
        {
            throw new RuntimeException("Not found");
        }
        return mapToDepartmentDto(department);
    }

    //protected gebruikt zodat ze enkel kunnen worder aangeroepen in de service klassen.

    protected DepartmentDto mapToDepartmentDto(Department department)
    {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentId(department.getDepartmentId());
        departmentDto.setDepartmentName(department.getDepartmentName());
        return departmentDto;
    }

    protected Department mapToDepartmentModel(DepartmentDto departmentDto)
    {
        Department department = new Department();
        department.setDepartmentId(departmentDto.getDepartmentId());
        department.setDepartmentName(department.getDepartmentName());
        return department;
    }
}
