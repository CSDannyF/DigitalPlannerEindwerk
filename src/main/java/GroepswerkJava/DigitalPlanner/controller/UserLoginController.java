package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.model.UserLogin;
import GroepswerkJava.DigitalPlanner.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserLoginController {


    @Autowired
    private UserLoginService userLoginService;

    @PostMapping("/loginAdmin")
    public String dashboardAdmin(@ModelAttribute("userlogin") UserLogin userlogin)
    {
        this.userLoginService.getByUserName(userlogin.getUsername());
        return "dashboard_employee";
    }
}
