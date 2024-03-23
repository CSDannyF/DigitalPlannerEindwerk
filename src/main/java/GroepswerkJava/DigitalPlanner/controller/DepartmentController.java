package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.model.Department;
import GroepswerkJava.DigitalPlanner.service.DepartmentServiceImpl;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

//controller moet nog aangepast worden om volledig beheer te doen

@Controller
public class DepartmentController {

    @Autowired
    private DepartmentServiceImpl departmentService;

    @GetMapping("/department")
    public String department(Model model)
    {
        Department department = new Department();
        model.addAttribute("department", department);
        return "department";
    }

    @PostMapping("/saveDepartment")
    public String saveDepartment(@ModelAttribute("department") Department department, Model model)
    {
        this.departmentService.save(department);
        //moet veranderd worden naar admindashboard indien klaar
        return "dashboardEmployee";
    }
}
