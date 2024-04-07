package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.model.*;
import GroepswerkJava.DigitalPlanner.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Set;

@Controller
public class NewEmployeeController {

    @Autowired
    private StaffServiceImpl staffService;
/*
    @Autowired
    private UserLoginService userLoginService;
*/
    @Autowired
    private ContractTypeServiceImpl contractTypeService;

    @Autowired
    private DepartmentServiceImpl departmentService;

    @Autowired
    private StaffFunctionServiceImpl staffFunctionService;

    @Autowired
    private DaysOfWeekServiceImpl daysOfWeekService;

    @GetMapping("/staff")
    public String newEmployee(Model model)
    {
        Staff staff = new Staff();
        model.addAttribute("staff", staff);
        model.addAttribute("staffMembers", staffService.getAll()); //moet ik via dto doen
/*
        UserLogin userLogin = new UserLogin();
        model.addAttribute("userLogin", userLogin);
*/
        model.addAttribute("department", new Department());
        model.addAttribute("departments", departmentService.getAll());

        model.addAttribute("staffFunction", new StaffFunction());
        model.addAttribute("staffFunctions", staffFunctionService.getAll());

        model.addAttribute("contractType", new ContractType());
        model.addAttribute("contractTypes", contractTypeService.getAll());

        model.addAttribute("dayOfWeek", new DaysOfWeek());
        model.addAttribute("daysOffWeek", daysOfWeekService.getAll());
        return "newEmployee";
    }

    @PostMapping("/Staff")
    public String saveStaff(@ModelAttribute("staff") Staff staff,
                            @RequestParam("departmentId") long departmentId,
                            @RequestParam("staffFunctionId") long staffFunctionId,
                            @RequestParam("staffId")long staffId,
                            @RequestParam("contractTypeId") long contractTypeId,
                            @RequestParam("daysOfWeekId") long daysOfWeekId,
                            Model model)
    {
        Department department = departmentService.getById(departmentId);
        StaffFunction staffFunction = staffFunctionService.getById(staffFunctionId);
        Staff staffResponsible = staffService.getById(staffId);
        ContractType contractType = contractTypeService.getById(contractTypeId);

        //deze moet met checkboxes gedaan worden en nog andere reutemeteut
        DaysOfWeek day = daysOfWeekService.getById(daysOfWeekId);
        Set<DaysOfWeek> days = new HashSet<>();
        days.add(day);
        staff.setDaysOfWeek(days);


        staff.setDepartment(department);
        staff.setStaffFunction(staffFunction);
        staff.setPersonResponsible(staffResponsible);
        staff.setContractType(contractType);

        this.staffService.save(staff);
        return "dashboard";
    }
}
