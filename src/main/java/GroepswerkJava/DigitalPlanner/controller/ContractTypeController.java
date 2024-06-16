package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.dto.ContractTypeDto;
import GroepswerkJava.DigitalPlanner.dto.StaffDto;
import GroepswerkJava.DigitalPlanner.dto.StatisticsDto;
import GroepswerkJava.DigitalPlanner.dto.UserLoginDto;
import GroepswerkJava.DigitalPlanner.model.ContractType;
import GroepswerkJava.DigitalPlanner.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ContractTypeController {

    @Autowired
    private ContractTypeServiceImpl contractTypeService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private CompanyServiceImpl companyService;

    @Autowired
    private WeeklyOpeningDaysService weeklyOpeningDaysService;

    @Autowired
    private UserLoginService userLoginService;

    @GetMapping("/contractType")
    public String contractType(Model model,
                               Authentication authentication) {

        checkSetup(model);
        UserLoginDto loggedInUser = userLoginService.getByEmail(authentication.getName());
        model.addAttribute("loggedInUser", loggedInUser);

        boolean isAdmin = isAdmin(authentication);
        String firstName = authentication.getName().split("@")[0];
        StatisticsDto statisticsDto = companyService.getStatistics();

        ContractType contractType = new ContractType();
        List<ContractTypeDto> contractTypeDtos;
        contractTypeDtos = contractTypeService.getAll();

        model.addAttribute("statistics", statisticsDto);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);
        model.addAttribute("contractTypes", contractTypeDtos);
        model.addAttribute("contractType", contractType);
        return "contractType";
    }

    @PostMapping("/contractType")
    public String saveContractType(@ModelAttribute("contractType") ContractTypeDto contractTypeDto,
                                   BindingResult result,
                                   Model model,
                                   Authentication authentication) {

        checkSetup(model);
        UserLoginDto loggedInUser = userLoginService.getByEmail(authentication.getName());

        boolean isAdmin = isAdmin(authentication);
        String firstName = authentication.getName().split("@")[0];
        StatisticsDto statisticsDto = companyService.getStatistics();

        List<ContractTypeDto> contractTypeDtos;
        contractTypeDtos = contractTypeService.getAll();

        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("contractTypes", contractTypeDtos);
        model.addAttribute("statistics", statisticsDto);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);

        //Check als niet 2x hetzelfde type wordt aangemaakt
        ContractTypeDto existing = contractTypeService.getByContractTypeName(contractTypeDto.getContractTypeName());
        if (existing.getContractTypeName() != null) {
            result.rejectValue("contractTypeName", null, "The contract type name has to be unique");
        }
        if (result.hasErrors()) {
            return "/contractType";
        }

        contractTypeService.save(contractTypeDto);
        return "redirect:/contractType";
    }

    @GetMapping("deleteContractType")
    public String deleteContractType(@ModelAttribute("contractType") ContractTypeDto contractTypeDto,
                                     Model model,
                                     Authentication authentication) {

        checkSetup(model);
        UserLoginDto loggedInUser = userLoginService.getByEmail(authentication.getName());
        List<ContractTypeDto> contractTypeDtos = contractTypeService.getAll();

        boolean isAdmin = isAdmin(authentication);
        String firstName = authentication.getName().split("@")[0];
        StatisticsDto statisticsDto = companyService.getStatistics();

        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("contractTypes", contractTypeDtos);
        model.addAttribute("statistics", statisticsDto);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);

        //hier vergelijken als het een staff member niet afhankelijk is van referentiële integriteit (anders fout op de referentie bij delete)
        List<StaffDto> staffDtos = staffService.getAll();
        boolean stillLinked = false;
        for (StaffDto staffDto : staffDtos) {
            if (staffDto.getContractType().getContractTypeName().equals(contractTypeDto.getContractTypeName())) {
                stillLinked = true;
                break;
            }
        }
        if (stillLinked) {
            model.addAttribute("errorMessage", "There are still staff members linked to this contract type.");
            return "/contractType";
        }

        contractTypeService.deleteContractTypeByName(contractTypeDto.getContractTypeName());
        return "redirect:/contractType";
    }

    @GetMapping("/editContractType/{contractTypeName}")
    public String editContractType(@PathVariable String contractTypeName,
                                   Model model,
                                   Authentication authentication) {

        ContractTypeDto contractTypeDto = contractTypeService.getByContractTypeName(contractTypeName);
        UserLoginDto loggedInUser = userLoginService.getByEmail(authentication.getName());

        boolean isAdmin = isAdmin(authentication);
        String firstName = authentication.getName().split("@")[0];
        StatisticsDto statisticsDto = companyService.getStatistics();

        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("contractType", contractTypeDto);
        model.addAttribute("statistics", statisticsDto);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);

        return "/editContractType";
    }

    //om de edit te doen moet ik via de name van de aan te passen object de Id uit de database halen om ze zo te kunnen updaten
    @PostMapping("/editContractType/{contractTypeName}")
    public String saveEditedContractType(@PathVariable String contractTypeName,
                                         @ModelAttribute("contractType") ContractTypeDto contractTypeDto,
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

        //bestaande type wijzigen en checken als deze al dan niet bestaat (moet uniek zijn)
        ContractTypeDto existing = contractTypeService.getByContractTypeName(contractTypeDto.getContractTypeName());
        if (existing.getContractTypeName() != null) {
            if (existing.getContractTypeName().equals(contractTypeDto.getContractTypeName()) && !contractTypeDto.getContractTypeName().equals(contractTypeName)) {
                result.rejectValue("contractTypeName", null, "The contracttype name has to be unique");
            }
            if (result.hasErrors()) {
                return "/editContractType";
            }
        }

        ContractTypeDto contractTypeDto1 = contractTypeService.getByContractTypeName(contractTypeName);
        contractTypeDto.setContractTypeId(contractTypeDto1.getContractTypeId());
        contractTypeService.save(contractTypeDto);
        return "redirect:/contractType";
    }

    private void checkSetup(Model model) {
        boolean isSetupDone = companyService.isSetupDone();
        boolean isContractTypeEmpty = contractTypeService.getAll().isEmpty();

        model.addAttribute("isContractTypeEmpty", isContractTypeEmpty);
        model.addAttribute("isSetupDone", isSetupDone);
    }

    private boolean isAdmin(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        return isAdmin;
    }
}
