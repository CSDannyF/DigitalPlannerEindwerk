package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.dto.DepartmentDto;
import GroepswerkJava.DigitalPlanner.service.DepartmentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DepartmentControllerTest
{
    @MockBean
    private DepartmentServiceImpl departmentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create new department")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createDepartment() throws Exception
    {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentName("Human Resources");

        when(departmentService.save(ArgumentMatchers.any(DepartmentDto.class))).thenReturn(departmentDto);
        when(departmentService.getAll()).thenReturn(Collections.EMPTY_LIST);
        when(departmentService.getByDepartmentName(ArgumentMatchers.anyString())).thenReturn(new DepartmentDto());

        this.mockMvc.perform(post("/department")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("departmentName", departmentDto.getDepartmentName()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/department"));
    }
}
