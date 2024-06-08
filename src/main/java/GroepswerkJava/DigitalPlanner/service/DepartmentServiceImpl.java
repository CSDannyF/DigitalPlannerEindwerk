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
    public void deleteDepartmentByName(String departmentName)
    {
        Department department = departmentRepository.findByDepartmentName(departmentName);
        this.departmentRepository.delete(department);
    }

    @Override
    public void save(DepartmentDto departmentDto)
    {
        Department department = new Department();

        if (departmentRepository.findByDepartmentName(departmentDto.getDepartmentName()) == null)
        {
            department = mapToDepartmentModel(departmentDto);
            departmentRepository.save(department);
        } else {
            department = departmentRepository.findByDepartmentName(departmentDto.getDepartmentName());
            department.setDepartmentName(departmentDto.getDepartmentName());
            departmentRepository.save(department);
        }
    }

    @Override
    public DepartmentDto getByDepartmentName(String departmentName)
    {
        Department department = departmentRepository.findByDepartmentName(departmentName);
        DepartmentDto departmentDto = new DepartmentDto();
        if (department != null)
        {
            departmentDto = mapToDepartmentDto(department);
        }
        return departmentDto;
    }

    @Override
    public DepartmentDto getById(long id)
    {
        Optional<Department> optional = departmentRepository.findById(id);
        Department department = null;
        if (optional.isPresent())
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
        if(department != null){

            departmentDto.setDepartmentName(department.getDepartmentName());
            if (department.getDepartmentId() != 0)
            {
                departmentDto.setDepartmentId(department.getDepartmentId());
            }
        }


        return departmentDto;
    }

    protected Department mapToDepartmentModel(DepartmentDto departmentDto)
    {
        Department department = new Department();
        department.setDepartmentId(departmentDto.getDepartmentId());
        department.setDepartmentName(departmentDto.getDepartmentName());
        return department;
    }
}
