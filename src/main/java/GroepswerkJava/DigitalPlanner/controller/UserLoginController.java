package GroepswerkJava.DigitalPlanner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserLoginController {


    //service moet nog gecreeerd worden
    @Autowired
    //private UserLoginService userLoginService;

    @GetMapping("/loginAdmin")
    public String dashboardAdmin(Model model)
    {

        return "";
    }
}
