package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.dto.StaffDto;
import GroepswerkJava.DigitalPlanner.dto.StaffFunctionDto;
import GroepswerkJava.DigitalPlanner.model.StaffFunction;
import GroepswerkJava.DigitalPlanner.service.CompanyServiceImpl;
import GroepswerkJava.DigitalPlanner.service.StaffFunctionServiceImpl;
import GroepswerkJava.DigitalPlanner.service.StaffService;
import GroepswerkJava.DigitalPlanner.service.WeeklyOpeningDaysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class StaffFunctionController {

    @Autowired
    private StaffFunctionServiceImpl staffFunctionService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private CompanyServiceImpl companyService;

    @Autowired
    private WeeklyOpeningDaysService weeklyOpeningDaysService;

    @GetMapping("/staffFunction")
    public String staffFunction(Model model, Authentication authentication)
    {
        StaffFunctionDto staffFunction = new StaffFunctionDto();
        List<StaffFunctionDto> staffFunctionDtos;
        staffFunctionDtos = staffFunctionService.getAll();

        boolean isSetupDone = companyService.isSetupDone();
        boolean isStaffFunctionEmpty = staffFunctionService.getAll().isEmpty();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        String email = authentication.getName();
        String firstName = email.split("@")[0];

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);

        int totalStaffContractHours = staffService.getTotalStaffContractHours();
        int totalWorkingHoursNeeded = weeklyOpeningDaysService.getTotalShiftHoursPerWeek();

        // Calculate percentage
        double percentage = (double) totalStaffContractHours / totalWorkingHoursNeeded * 100;
        int roundedPercentage = (int) percentage;

        model.addAttribute("totalStaffContractHours", totalStaffContractHours);
        model.addAttribute("totalWorkingHoursNeeded", totalWorkingHoursNeeded);
        model.addAttribute("percentage", roundedPercentage);

        model.addAttribute("isStaffFunctionEmpty", isStaffFunctionEmpty);
        model.addAttribute("isSetupDone", isSetupDone);

        model.addAttribute("staffFunctions", staffFunctionDtos);
        model.addAttribute("staffFunction", staffFunction);
        return "staffFunction";
    }

    @PostMapping("/staffFunction")
    public String saveStaffFunction(@ModelAttribute("staffFunction") StaffFunctionDto staffFunctionDto,
                                    BindingResult result,
                                    Model model)
    {
        List<StaffFunctionDto> staffFunctionDtos;
        staffFunctionDtos = staffFunctionService.getAll();
        model.addAttribute("staffFunctions", staffFunctionDtos);

        StaffFunctionDto existing = staffFunctionService.getByStaffFunctionName(staffFunctionDto.getFunctionName());
        if (existing.getFunctionName() != null)
        {
            result.rejectValue("functionName", null, "The staff function has to be unique");
        }
        if (result.hasErrors())
        {
            return "/staffFunction";
        }

        staffFunctionService.save(staffFunctionDto);
        return "redirect:/staffFunction";
    }

    @GetMapping("/deleteStaffFunction")
    public String deleteStaffFunction(@RequestParam String functionName,
                                      Model model, Authentication authentication)
    {
        List<StaffFunctionDto> staffFunctionDtos = staffFunctionService.getAll();
        model.addAttribute("staffFunctions", staffFunctionDtos);
        model.addAttribute("staffFunction", new StaffFunction());

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        String email = authentication.getName();
        String firstName = email.split("@")[0];

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);

        int totalStaffContractHours = staffService.getTotalStaffContractHours();
        int totalWorkingHoursNeeded = weeklyOpeningDaysService.getTotalShiftHoursPerWeek();

        // Calculate percentage
        double percentage = (double) totalStaffContractHours / totalWorkingHoursNeeded * 100;
        int roundedPercentage = (int) percentage;

        model.addAttribute("totalStaffContractHours", totalStaffContractHours);
        model.addAttribute("totalWorkingHoursNeeded", totalWorkingHoursNeeded);
        model.addAttribute("percentage", roundedPercentage);

        List<StaffDto> staffDtos = staffService.getAll();
        boolean stillLinked = false;
        for(StaffDto staffDto: staffDtos)
        {
            if (staffDto.getFunction().getFunctionName().equals(functionName)) {
                stillLinked = true;
                break;
            }
        }
        if (stillLinked)
        {
            model.addAttribute("errorMessage", "There are still staff members linked to this function.");
            return "/staffFunction";
        }

        staffFunctionService.deleteStaffFunctionByName(functionName);
        return "redirect:/staffFunction";
    }

    @GetMapping("/editStaffFunction/{functionName}")
    public  String editStaffFunction(@PathVariable String functionName,
                                     Model model, Authentication authentication)
    {
        StaffFunctionDto staffFunctionDto = staffFunctionService.getByStaffFunctionName(functionName);
        model.addAttribute("staffFunction", staffFunctionDto);

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        String email = authentication.getName();
        String firstName = email.split("@")[0];

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);

        int totalStaffContractHours = staffService.getTotalStaffContractHours();
        int totalWorkingHoursNeeded = weeklyOpeningDaysService.getTotalShiftHoursPerWeek();

        // Calculate percentage
        double percentage = (double) totalStaffContractHours / totalWorkingHoursNeeded * 100;
        int roundedPercentage = (int) percentage;

        model.addAttribute("totalStaffContractHours", totalStaffContractHours);
        model.addAttribute("totalWorkingHoursNeeded", totalWorkingHoursNeeded);
        model.addAttribute("percentage", roundedPercentage);

        return "/editStaffFunction";
    }

    @PostMapping("/editStaffFunction/{functionName}")
    public String saveEditedStaffFunction(@PathVariable String functionName,
                                          @ModelAttribute("staffFunction") StaffFunctionDto staffFunctionDto,
                                          BindingResult result)
    {
        StaffFunctionDto existing = staffFunctionService.getByStaffFunctionName(staffFunctionDto.getFunctionName());
        if(existing.getFunctionName() != null)
        {
            if (functionName.equals(staffFunctionDto.getFunctionName()) || existing.getFunctionName().equals(staffFunctionDto.getFunctionName()))
            {
                result.rejectValue("functionName", null, "The Staff function has to be unique");
            }
            if (result.hasErrors())
            {
                return "/editStaffFunction";
            }
        }

        StaffFunctionDto staffFunctionDto1 = staffFunctionService.getByStaffFunctionName(functionName);
        staffFunctionDto.setFunctionId(staffFunctionDto1.getFunctionId());
        staffFunctionService.save(staffFunctionDto);
        return "redirect:/staffFunction";
    }
}
