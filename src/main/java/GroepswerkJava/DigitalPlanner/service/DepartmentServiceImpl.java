package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.model.Department;
import GroepswerkJava.DigitalPlanner.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Component
@Service
public class DepartmentServiceImpl implements IService<Department> {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public List<Department> getAll() {
        return this.departmentRepository.findAll();
    }

    @Override
    public void save(Department department) {
        this.departmentRepository.save(department);
    }

    @Override
    public Department getById(long id) {
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
        return department;
    }
}
