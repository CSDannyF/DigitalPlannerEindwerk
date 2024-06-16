package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.dto.UserLoginDto;
import GroepswerkJava.DigitalPlanner.service.UserLoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserLoginService userLoginService;

    public AuthController(UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "index";
    }

    // handler method to handle user registration request
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {

        UserLoginDto userLoginDto = new UserLoginDto();
        model.addAttribute("user", userLoginDto);
        return "register";
    }

    // handler method to handle register user form submit request
    //nu is er een userlogin die niet gelinkt is aan een staff member:
    //deze later linken aan een leeg staff object die achteraf kan worden aangevuld
    @PostMapping("/register")
    public String registration(@Valid @ModelAttribute("user") UserLoginDto userLoginDto,
                               BindingResult result,
                               Model model) {

        UserLoginDto existing = userLoginService.getByEmail(userLoginDto.getEmail());

        if (existing.getEmail() != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", userLoginDto);
            return "register";
        }
        userLoginService.saveUser(userLoginDto);
        return "redirect:/login";
    }
}
