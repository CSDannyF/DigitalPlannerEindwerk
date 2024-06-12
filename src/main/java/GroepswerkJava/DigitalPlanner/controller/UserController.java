//package GroepswerkJava.DigitalPlanner.controller;
//
//import GroepswerkJava.DigitalPlanner.dto.CompanyDto;
//import GroepswerkJava.DigitalPlanner.dto.StaffDto;
//import GroepswerkJava.DigitalPlanner.service.StaffService;
//import GroepswerkJava.DigitalPlanner.service.UserLoginService;
//import GroepswerkJava.DigitalPlanner.service.UserLoginServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class UserController {
//
//    @Autowired
//    private StaffService staffService;
//    @Autowired
//
//    private UserLoginService userLoginService;
//
//    @GetMapping("/workSchedule")
//    public String showPersonalWorkSchedule() {
//        System.out.println("showing work work");
////        System.out.println(authentication);
////        System.out.println(authentication.getName());
////        System.out.println(authentication.getDetails());
////        System.out.println(authentication.getPrincipal());
////
////
////        StaffDto staffDto = staffService.getById(1);
////
////        model.addAttribute("staff", staffDto);
//        return "personalSchedule";
//    }
//}
