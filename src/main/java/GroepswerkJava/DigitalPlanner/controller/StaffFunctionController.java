package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.model.StaffFunction;
import GroepswerkJava.DigitalPlanner.service.StaffFunctionServiceImpl;
import GroepswerkJava.DigitalPlanner.service.StaffServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

//controller moet nog aangepast worden om volledig beheer te doen
@Controller
public class StaffFunctionController {

    @Autowired
    private StaffFunctionServiceImpl staffFunctionService;

    @GetMapping("/staff/function")
    public String staffFunction(Model model)
    {
        StaffFunction staffFunction = new StaffFunction();
        model.addAttribute("staffFunction", staffFunction);
        return "staffFunction";
    }

    @PostMapping("/staff/function")
    public String saveStaffFunction(@ModelAttribute("staffFunction") StaffFunction staffFunction, Model model)
    {
        this.staffFunctionService.save(staffFunction);
        return "dashboard";
    }
}
