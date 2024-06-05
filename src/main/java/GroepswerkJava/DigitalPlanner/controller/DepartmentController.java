package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.dto.DepartmentDto;
import GroepswerkJava.DigitalPlanner.dto.StaffDto;
import GroepswerkJava.DigitalPlanner.model.Department;
import GroepswerkJava.DigitalPlanner.service.CompanyServiceImpl;
import GroepswerkJava.DigitalPlanner.service.DepartmentServiceImpl;
import GroepswerkJava.DigitalPlanner.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DepartmentController {

    @Autowired
    private DepartmentServiceImpl departmentService;

    @Autowired
    private CompanyServiceImpl companyService;

    @Autowired
    private StaffService staffService;

    @GetMapping("/department")
    public String department(Model model)
    {
        Department department = new Department();
        List<DepartmentDto> departmentDtos;
        departmentDtos = departmentService.getAll();

        boolean isSetupDone = companyService.isSetupDone();
        boolean isDepartmentEmpty = departmentService.getAll().isEmpty();

        model.addAttribute("isDepartmentEmpty", isDepartmentEmpty);
        model.addAttribute("isSetupDone", isSetupDone);
        model.addAttribute("departments" ,departmentDtos);
        model.addAttribute("department", department);
        return "department";
    }

    @PostMapping("/department")
    public String saveDepartment(@ModelAttribute("department") DepartmentDto departmentDto,
                                 BindingResult result,
                                 Model model)
    {
        List<DepartmentDto> departmentDtos;
        departmentDtos = departmentService.getAll();
        model.addAttribute("departments" ,departmentDtos);

        DepartmentDto existing = departmentService.getByDepartmentName(departmentDto.getDepartmentName());
        if (existing.getDepartmentName() != null)
        {
            result.rejectValue("departmentName", null, "The department name has to be unique");
        }
        if (result.hasErrors())
        {
            return "/department";
        }

        departmentService.save(departmentDto);
        return "redirect:/department";
    }

    @GetMapping("/deleteDepartment")
    public String deleteDepartment(@RequestParam String departmentName,
                                   Model model)
    {
        List<DepartmentDto> departmentDtos = departmentService.getAll();
        model.addAttribute("departments", departmentDtos);
        model.addAttribute("department", new DepartmentDto());

        List<StaffDto> staffDtos = staffService.getAll();
        boolean stillLinked = false;
        for(StaffDto staffDto: staffDtos)
        {
            if (staffDto.getDepartment().getDepartmentName().equals(departmentName)) {
                stillLinked = true;
                break;
            }
        }
        if (stillLinked)
        {
            model.addAttribute("errorMessage", "There are still staff members linked to this department.");
            return "/department";
        }

        departmentService.deleteDepartmentByName(departmentName);
        return "redirect:/department";
    }

    @GetMapping("/editDepartment/{departmentName}")
    public String editDepartment(@PathVariable String departmentName,
                                 Model model)
    {
        DepartmentDto departmentDto = departmentService.getByDepartmentName(departmentName);
        model.addAttribute("department",departmentDto);
        return "/editDepartment";
    }

    @PostMapping("/editDepartment/{departmentName}")
    public String saveEditedDepartment(@PathVariable String departmentName,
                                       @ModelAttribute("department") DepartmentDto departmentDto,
                                       BindingResult result)
    {
        DepartmentDto existing = departmentService.getByDepartmentName(departmentDto.getDepartmentName());
        if (existing.getDepartmentName() != null) {
            if (departmentName.equals(departmentDto.getDepartmentName()) || existing.getDepartmentName().equals(departmentDto.getDepartmentName()))
            {
                result.rejectValue("departmentName", null, "The department name has to be unique");
            }
            if (result.hasErrors())
            {
                return "/editDepartment";
            }
        }

        DepartmentDto departmentDto1 = departmentService.getByDepartmentName(departmentName);
        departmentDto.setDepartmentId(departmentDto1.getDepartmentId());
        departmentService.save(departmentDto);
        return "redirect:/department";
    }
}
