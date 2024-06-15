package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.DepartmentDto;
import GroepswerkJava.DigitalPlanner.model.Department;
import GroepswerkJava.DigitalPlanner.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {
    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Mock
    private DepartmentRepository departmentRepository;

    private Department department1;
    private Department department2;
    private Department department3;

    @BeforeEach
    void init()
    {
        department1 = new Department();
        department1.setDepartmentName("Human Resources");

        department2 = new Department();
        department2.setDepartmentName("Production");

        department3 = new Department();
        department2.setDepartmentName("IT");
    }

    @Test
    @DisplayName("Should save Department")
    void save()
    {
        when(departmentRepository.save(any(Department.class))).thenReturn(department1);

        Department newDepartment = departmentRepository.save(department1);

        assertNotNull(department1);
        assertThat(newDepartment.getDepartmentName()).isEqualTo("Human Resources");
    }

    @Test
    @DisplayName("Should return a list of departments")
    void getDepartments()
    {
        List<Department> departments = new ArrayList<>();
        departments.add(department1);
        departments.add(department2);
        departments.add(department3);

        when(departmentRepository.findAll()).thenReturn(departments);

        assertNotNull(departmentService.getAll());
        assertEquals(3,departmentService.getAll().size());
    }

    @Test
    @DisplayName("Should return department by Id")
    void getById()
    {
        department1.setDepartmentId(1L);
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department1));
        DepartmentDto existingDepartment = departmentService.getById(1L);

        assertNotNull(existingDepartment);
        assertThat(existingDepartment.getDepartmentId()).isEqualTo(1L);
    }
    @Test
    @DisplayName("Should throw runtime exception")
    void getByIdShouldReturnException()
    {
        department1.setDepartmentId(1L);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department1));

        assertThrows(RuntimeException.class,() -> {
            departmentService.getById(2L);
        });
    }
}
