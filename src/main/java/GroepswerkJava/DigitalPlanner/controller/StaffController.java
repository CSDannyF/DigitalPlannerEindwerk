package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.dto.*;
import GroepswerkJava.DigitalPlanner.model.ContractType;
import GroepswerkJava.DigitalPlanner.model.StaffFunction;
import GroepswerkJava.DigitalPlanner.repository.CompanyRepository;
import GroepswerkJava.DigitalPlanner.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@Validated
public class StaffController {

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

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("staffList")
    public String showStaffList(Model model)
    {
        StaffDto staffDto = new StaffDto();
        List<StaffDto> staffDtos = staffService.getAll();
        model.addAttribute("staffDto", staffDto);
        model.addAttribute("staffMembers", staffDtos);
        return "/staffList";
    }

    @GetMapping("/newStaff")
    public String newStaff(Model model)
    {
        StaffDto staffDto = new StaffDto();
        List<StaffDto> staffDtos = staffService.getAll();
        model.addAttribute("staffDto", staffDto);
        model.addAttribute("staffMembers", staffDtos);

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
        return "newStaff";
    }

    @PostMapping("/newStaff")
    public String saveStaff(@Valid @ModelAttribute("staffDto") StaffDto staffDto,
                            @ModelAttribute("userLogin") UserLoginDto userLoginDto,
                            @RequestParam(value = "departmentId") long departmentId,
                            @RequestParam("functionId") long functionId,
                            @RequestParam(value = "staffId", required = false)long staffId,
                            @RequestParam("contractTypeId") long contractTypeId,
                            @RequestParam(value = "daysOffWeekId", required = false) String daysOffWeekId,
                            BindingResult result,
                            Model model)
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

        //check/validatie als departement, contract type en functie niet null zijn
        if (departmentId == 0 || functionId == 0 || contractTypeId == 0)
        {
            model.addAttribute("error", "The fields department, contract type and staff function are required");
            return "staff";
        }

        /*
        hier moet ik de userlogin ophalen via de ingegeven emailadres en dan in een if statement checken als die
        uniek is of niet en reject doen ingeval van niet uniek
        */
        UserLoginDto existing = userLoginService.findByEmail(userLoginDto.getEmail());
        if (existing.getEmail() != null)
        {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors())
        {
            return "newStaff";
        }

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

        //toevoegen van company aan staff
        /*
        CompanyServiceImpl companyService = new CompanyServiceImpl(companyRepository);
        List<CompanyDto> companyDtos = companyService.getAll();
        staffDto.setCompanyDto(companyDtos.getFirst());
         */

        userLoginDto.setStaffDto(staffDto);
        userLoginDto.setPassword(staffDto.getFirstName()+staffDto.getLastName());

        //staffDTO object zit in de userloginDTO object en wordt er samen mee opgeslagen.
        //Als de staff een aparte save doet staat die er 2x in
        userLoginService.saveUser(userLoginDto);
        return "dashboard";
    }
}
