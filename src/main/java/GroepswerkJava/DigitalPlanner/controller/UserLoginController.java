package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.dto.StatisticsDto;
import GroepswerkJava.DigitalPlanner.dto.UserLoginDto;
import GroepswerkJava.DigitalPlanner.service.CompanyService;
import GroepswerkJava.DigitalPlanner.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class UserLoginController {
    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private CompanyService companyService;

    @GetMapping("/editUserLogin/{id}")
    public String editUserLogin(@PathVariable long id,
                                Authentication authentication,
                                Model model) {

        UserLoginDto loggedInUser = userLoginService.getByEmail(authentication.getName());
        UserLoginDto userLoginDto = userLoginService.getById(id);

        boolean isAdmin = isAdmin(authentication);
        String firstName = authentication.getName().split("@")[0];
        StatisticsDto statisticsDto = companyService.getStatistics();

        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("statistics", statisticsDto);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);
        model.addAttribute("userLogin", userLoginDto);

        return "editUserLogin";
    }

    @GetMapping("/newUserPassword")
    public String newPassword(Model model) {
        model.addAttribute("userLogin", new UserLoginDto());
        return "newUserPassword";
    }

    @PostMapping("/saveImage")
    public String saveImage(@ModelAttribute("loggedInUser") UserLoginDto userLogin,
                            BindingResult result,
                            Authentication authentication,
                            Model model) throws IOException {

        UserLoginDto loggedInUser = userLoginService.getByEmail(authentication.getName());
        userLogin.setId(loggedInUser.getId());
        userLogin.setEmail(loggedInUser.getEmail());

        boolean isAdmin = isAdmin(authentication);
        String firstName = authentication.getName().split("@")[0];
        StatisticsDto statisticsDto = companyService.getStatistics();

        model.addAttribute("staff", userLogin.getStaffDto());
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("statistics", statisticsDto);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);

        // Verwerken van de avatar
        if (userLogin.getAvatar().getOriginalFilename() == "") {
            result.rejectValue("avatar", null, "Failed to upload the image.");
        }
        if (result.hasErrors()) {
            return "myProfile";
        }

        userLoginService.saveImage(userLogin);
        return "myProfile";
    }

    @PostMapping("/newUserPassword")
    public String saveNewPassword(@ModelAttribute("loggedInUser") UserLoginDto userLogin,
                                  @RequestParam("password2") String password2,
                                  BindingResult result,
                                  Authentication authentication,
                                  Model model) {

        UserLoginDto loggedInUser = userLoginService.getByEmail(authentication.getName());
        boolean isAdmin = isAdmin(authentication);
        String firstName = authentication.getName().split("@")[0];
        StatisticsDto statisticsDto = companyService.getStatistics();

        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("statistics", statisticsDto);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);

        if (!userLogin.getPassword().equals(password2)) {
            result.rejectValue("password", null, "Password 1 and 2 are not the same");
        }
        if (result.hasErrors()) {
            return "newUserPassword";
        }

        //Hier komt de user terecht als hij een dummy wachtwoord heeft en hij deze nog moet veranderen
        loggedInUser = userLoginService.getByEmail(authentication.getName());
        userLogin.setId(loggedInUser.getId());
        userLogin.setEmail(loggedInUser.getEmail());

        userLoginService.updateUserLogin(userLogin);
        return "dashboard";
    }

    @PostMapping("/editUserLogin/{id}")
    public String saveEditedUserLogin(@PathVariable long id,
                                      @ModelAttribute UserLoginDto userLogin,
                                      Authentication authentication,
                                      BindingResult result,
                                      Model model) {

        //user via id uit db gehaalt, dat is degene waar de update wordt op uitgevoerd
        UserLoginDto userFromDb = userLoginService.getById(id);
        model.addAttribute("userLogin", userFromDb);

        /*
        via de userLogin (ingegeven email) wordt er gecheckt als deze al niet in gebruik is
        userFromDb vergelijkt de nieuwe email met de ingegeven email (userLogin) indien deze hetzelfde zijn is hij niet veranderd en mag de flow niet in deze if-statement komen
        deze 2 samen checken als de ingegeven email geldig is
         */
        UserLoginDto existing = userLoginService.getByEmail(userLogin.getEmail());
        if (existing.getEmail() != null & !userFromDb.getEmail().equals(userLogin.getEmail())) {  //deze melding komt er nog niet op
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            return "/editUserLogin";
        }

        userLoginService.updateUserLogin(userLogin);
        return "redirect:/staff/staffList";
    }

    private boolean isAdmin(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        return isAdmin;
    }
}
