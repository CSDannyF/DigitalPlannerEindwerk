package GroepswerkJava.DigitalPlanner.repository;

import GroepswerkJava.DigitalPlanner.model.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class DepartmentRepositoryTest
{
    @Autowired
    private DepartmentRepository departmentRepository;

    private Department department1;
    private Department department2;
    private Department returnFromSave;

    @BeforeEach
    void init()
    {
        department1 = new Department();
        department1.setDepartmentName("Human Resources");

        department2 = new Department();
        department2.setDepartmentName("Production");
    }

    @Test
    @DisplayName("Should save department to database and id should not be null")
    void save()
    {
        returnFromSave = departmentRepository.save(department1);

        assertNotNull(returnFromSave);
        assertThat(returnFromSave.getDepartmentId()).isNotEqualTo(null);
    }

    @Test
    @DisplayName("Should return record with Id number 2")
    void getById()
    {
        departmentRepository.save(department2);
        Optional<Department> newDepartment = departmentRepository.findById(department2.getDepartmentId());
        assertTrue(newDepartment.isPresent());
    }

    @Test
    @DisplayName("Should return all records")
    void getAllDepartments()
    {
        departmentRepository.save(department1);
        departmentRepository.save(department2);
        List<Department> departments = departmentRepository.findAll();

        assertNotNull(departments);
        assertEquals(2,departments.size());
    }

    @Test
    @DisplayName("Should return null from the saved record")
    void delete()
    {
        departmentRepository.save(department1);

        int onceInserted = departmentRepository.findAll().size();
        departmentRepository.delete(department1);
        int ondeDeleted = departmentRepository.findAll().size();

        assertEquals(onceInserted, ondeDeleted+1);
    }
}
