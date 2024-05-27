package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.dto.*;
import GroepswerkJava.DigitalPlanner.model.ContractType;
import GroepswerkJava.DigitalPlanner.model.StaffFunction;
import GroepswerkJava.DigitalPlanner.repository.StaffRepository;
import GroepswerkJava.DigitalPlanner.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Controller
@Validated
public class  NewStaffController {

    @Autowired
    private StaffServiceImpl staffService;

    @Autowired
    private UserLoginServiceImpl userLoginService;

    @Autowired
    private ContractTypeServiceImpl contractTypeService;

    @Autowired
    private DepartmentServiceImpl departmentService;

    @Autowired
    private StaffFunctionServiceImpl staffFunctionService;

    @Autowired
    private DaysOfWeekServiceImpl daysOfWeekService;

    @Autowired
    private RoleServiceImpl roleService;

    @GetMapping("/staff")
    public String newStaff(Model model)
    {
        StaffDto staffDto = new StaffDto();
        model.addAttribute("staffDto", staffDto);
        model.addAttribute("staffMembers", staffService.getAll()); //moet ik via dto doen

        UserLoginDto userLoginDto = new UserLoginDto();
        model.addAttribute("userLogin", userLoginDto);

        model.addAttribute("departmentDto", new DepartmentDto());
        model.addAttribute("departments", departmentService.getAll());

        model.addAttribute("staffFunctionDto", new StaffFunctionDto());
        model.addAttribute("staffFunctions", staffFunctionService.getAll());

        model.addAttribute("contractTypeDto", new ContractTypeDto());
        model.addAttribute("contractTypes", contractTypeService.getAll());

        model.addAttribute("dayOffWeekDto", new DaysOffWeekDto());
        model.addAttribute("daysOffWeek", daysOfWeekService.getAll());
        return "staff";
    }

    @PostMapping("/Staff")
    public String saveStaff(@Valid @ModelAttribute("staffDto") StaffDto staffDto,
                            @ModelAttribute("userLogin") UserLoginDto userLoginDto,
                            @RequestParam(value = "departmentId", required = false) long departmentId,
                            @RequestParam("functionId") long functionId,
                            @RequestParam(value = "staffId", required = false)long staffId,
                            @RequestParam("contractTypeId") long contractTypeId,
                            @RequestParam(value = "daysOffWeekId", required = false) String daysOffWeekId,
                            BindingResult result, Model model)
    {

        //Model attributen teruggeven, anders kan hij de pagina niet opnieuw inladen
        model.addAttribute("departmentDto", new DepartmentDto());
        model.addAttribute("staffFunctionDto", new StaffFunction());
        model.addAttribute("contractTypeDto", new ContractType());
        model.addAttribute("dayOffWeekDto", new DaysOffWeekDto());
        model.addAttribute("useLogin", new UserLoginDto());
        model.addAttribute("staffMembers", staffService.getAll());
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("staffFunctions", staffFunctionService.getAll());
        model.addAttribute("contractTypes", contractTypeService.getAll());
        model.addAttribute("daysOffWeek", daysOfWeekService.getAll());

        /*hier moet ik de userlogin ophalen via de ingegeven emailadres en dan in een if statement checken als die
        uniek is of niet en reject doen ingeval van niet uniek
        */
        UserLoginDto existing = userLoginService.findByEmail(userLoginDto.getEmail());

        if(existing.getEmail() != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors())
        {
            return "staff";
        }

/*
        //in deze blok wordt nagekeken als een bepaald veld al dan niet in ingevuld + geef ik een error boodschap mee
        if(staffDto.getFirstName() == null || staffDto.getFirstName().isEmpty())
        {
            result.rejectValue("firstName", "NotEmpty", "First name cannot be empty");
        }
        if(staffDto.getLastName() == null || staffDto.getLastName().isEmpty())
        {
            result.rejectValue("lastName", "NotEmpty", "Last name cannot be empty");
        }
        if(staffDto.getAddressLine1() == null || staffDto.getAddressLine1().isEmpty())
        {
            result.rejectValue("addressLine1", "NotEmpty", "Address line 1 name cannot be empty");
        }
        if(staffDto.getZipcode() == null || staffDto.getZipcode().isEmpty())
        {
            result.rejectValue("zipcode", "NotEmpty", "Zipcode name cannot be empty");
        }
        if(staffDto.getCity() == null || staffDto.getCity().isEmpty())
        {
            result.rejectValue("city", "NotEmpty", "City name cannot be empty");
        }
        if(staffDto.getPhoneNumber() == null || staffDto.getPhoneNumber().isEmpty())
        {
            result.rejectValue("phoneNumber", "NotEmpty", "Phone number name cannot be empty");
        }
        if(staffDto.getBirthDate() == null)
        {
            result.rejectValue("birthDate", "NotEmpty", "Birth date cannot be empty");
        }
*/
        //indien er een foutmelding is ga ik gewoon returnen naar dezelfde html



        //Hier check ik als een model al dan niet ingevuld voor de staff via de dropdown menu's.
        //Als er een niks is aangeduid gaat de id 0 of null zijn, dan gaat het specifieke model/object niet worden toegevoegd aan de Staff
        //Indien er wel iets aangeduidt is gaat dit 1 of meer zijn en wordt di gebonden aan het Staff object
        if (departmentId != 0)
        {
            DepartmentDto department = departmentService.getById(departmentId);
            staffDto.setDepartment(department);
        }
        if (contractTypeId != 0)
        {
            ContractTypeDto contractType = contractTypeService.getById(contractTypeId);
            staffDto.setContractType(contractType);
        }
        if (staffId != 0)
        {
            StaffDto staffResponsible = staffService.getById(staffId);
            staffDto.setPersonResponsible(staffResponsible);
        }
        if (functionId != 0)
        {
            StaffFunctionDto staffFunction = staffFunctionService.getById(functionId);
            staffDto.setFunction(staffFunction);
        }
        if (daysOffWeekId != null) {
            Set<DaysOffWeekDto> daysOff = new HashSet<>();
            String days[] = daysOffWeekId.split(",");

            for(String day: days)
            {
                DaysOffWeekDto day2 = daysOfWeekService.getById(Long.parseLong(day));
                daysOff.add(day2);
            }
            staffDto.setFixedDaysOff(daysOff);
        }

        userLoginDto.setStaffDto(staffDto);
        userLoginDto.setPassword(staffDto.getFirstName()+staffDto.getLastName());

        //staffDTO object zit in de userloginDTO object en wordt er samen mee opgeslagen.
        //als de staff een aparte save doet staat die er 2x in
        this.userLoginService.saveUser(userLoginDto);

        return "dashboard";
    }
}
