package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.model.UserLogin;
import GroepswerkJava.DigitalPlanner.service.UserLoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserLoginController {

//
//    @Autowired
//    private UserLoginServiceImpl userLoginService;
//
//    @PostMapping("/loginAdmin")
//    public String dashboardAdmin(@ModelAttribute("userlogin") UserLogin userlogin)
//    {
//        this.userLoginService.getByEmail(userlogin.getEmail());
//        return "dashboard";
//    }
}
