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
@RequestMapping("/staff")
public class StaffController
{
    @Autowired
    private StaffService staffService;

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

    @GetMapping("/staffList")
    public String showStaffList(Model model)
    {
        StaffDto staffDto = new StaffDto();
        List<StaffDto> staffDtos = staffService.getAll();
        model.addAttribute("staffMembers", staffDtos);
        model.addAttribute("staffDto", staffDto);
        return "staffList";
    }

    @GetMapping("/staffDetail/{staffId}")
    public String staffDetail(@PathVariable long staffId,
                              Model model)
    {
        StaffDto staffDto = staffService.getById(staffId);
        UserLoginDto userLoginDto = userLoginService.getByEmail(staffDto.getEmail());
        model.addAttribute("staff", staffDto);
        model.addAttribute("userLogin", userLoginDto);
        return "/staffDetail";
    }

    @GetMapping("/editStaff/{staffId}")
    public String editStaff(@PathVariable long staffId,
                             Model model)
    {
        StaffDto staffDto = staffService.getById(staffId);
        List<StaffDto> staffDtos = staffService.getAll();
        model.addAttribute("staffDto", staffDto);
        model.addAttribute("staffMembers", staffDtos);

        UserLoginDto userLoginDto = userLoginService.getByEmail(staffDto.getEmail());
        model.addAttribute("userLogin", userLoginDto);

        model.addAttribute("departmentDto", new DepartmentDto());
        model.addAttribute("departments", departmentService.getAll());

        model.addAttribute("staffFunctionDto", new StaffFunctionDto());
        model.addAttribute("staffFunctions", staffFunctionService.getAll());

        model.addAttribute("contractTypeDto", new ContractTypeDto());
        model.addAttribute("contractTypes", contractTypeService.getAll());

        model.addAttribute("dayOffWeekDto", new DaysOffWeekDto());
        model.addAttribute("daysOffWeek", daysOfWeekService.getAll());
        return "editStaff";
    }

    @PostMapping("/editStaff/{staffId}")
    public String saveEditedStaff(@PathVariable long staffId,
                                  @ModelAttribute StaffDto staffDto,
                                  @ModelAttribute UserLoginDto userLoginDto,
                                  @RequestParam("departmentId") long departmentId,
                                  @RequestParam("functionId") long functionId,
                                  @RequestParam("contractTypeId") long contractTypeId,
                                  @RequestParam(value = "daysOffWeekId", required = false) String daysOffWeekId,
                                  BindingResult result,
                                  Model model)
    {
        StaffDto staffFromDb = staffService.getById(staffId);
        model.addAttribute("userLogin",userLoginDto);
        model.addAttribute("staffDto", staffFromDb);

        model.addAttribute("departmentDto", new DepartmentDto());
        model.addAttribute("departments", departmentService.getAll());

        model.addAttribute("staffFunctionDto", new StaffFunctionDto());
        model.addAttribute("staffFunctions", staffFunctionService.getAll());

        model.addAttribute("contractTypeDto", new ContractTypeDto());
        model.addAttribute("contractTypes", contractTypeService.getAll());

        model.addAttribute("dayOffWeekDto", new DaysOffWeekDto());
        model.addAttribute("daysOffWeek", daysOfWeekService.getAll());

        //wordt niet gebruikt omdat person responsible nu niet gebruikt word
        model.addAttribute("staffMembers", staffService.getAll());

        if (departmentId == 0 || functionId == 0 || contractTypeId == 0)
        {
            model.addAttribute("error", "The fields department, contract type and staff function are required");
            return "editStaff";
        }

        UserLoginDto existing = userLoginService.getByEmail(userLoginDto.getEmail());
        if (existing.getEmail() != null &! staffFromDb.getEmail().equals(staffDto.getEmail()))
        {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors())
        {
            return "editStaff";
        }

        DepartmentDto department = departmentService.getById(departmentId);
        staffDto.setDepartment(department);

        ContractTypeDto contractType = contractTypeService.getById(contractTypeId);
        staffDto.setContractType(contractType);

        StaffFunctionDto staffFunction = staffFunctionService.getById(functionId);
        staffDto.setFunction(staffFunction);

        if (staffId != 0)
        {
            StaffDto staffResponsible = staffService.getById(staffId);
            staffDto.setPersonResponsible(staffResponsible);
        }

        if (daysOffWeekId != null)
        {
            Set<DaysOffWeekDto> daysOff = new HashSet<>();
            String days[] = daysOffWeekId.split(",");

            for(String day: days)
            {
                DaysOffWeekDto day2 = daysOfWeekService.getById(Long.parseLong(day));
                daysOff.add(day2);
            }
            staffDto.setFixedDaysOff(daysOff);
        }

        //save user gebruikt. saveStaff kreeg ik niet klaar met een flushing probleem
        staffDto.setStaffId(staffId);
        userLoginDto.setStaffDto(staffDto);
        userLoginService.saveUser(userLoginDto);
        return "redirect:/staff/staffList";
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
                            //@RequestParam(value = "staffId")long staffId,
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
            return "newStaff";
        }


        //Check als email uniek is
        UserLoginDto existing = userLoginService.getByEmail(userLoginDto.getEmail());
        if (existing.getEmail() != null)
        {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors())
        {
            return "newStaff";
        }

        DepartmentDto department = departmentService.getById(departmentId);
        staffDto.setDepartment(department);

        ContractTypeDto contractType = contractTypeService.getById(contractTypeId);
        staffDto.setContractType(contractType);

        StaffFunctionDto staffFunction = staffFunctionService.getById(functionId);
        staffDto.setFunction(staffFunction);

        //Hier check ik als een model al dan niet ingevuld voor de staff via de dropdown menu's.
        //Als er een niks is aangeduid gaat de ID 0 of null zijn, dan gaat het specifieke model/object niet worden toegevoegd aan de Staff
        //Indien er wel iets aangeduid wordt dit 1 of meer zijn en wordt dit gebonden aan het Staff object
        //TODO word op dit moment niet gebruikt
        /*
        if (staffId != 0)
        {
            StaffDto staffResponsible = staffService.getById(staffId);
            staffDto.setPersonResponsible(staffResponsible);
        }
         */

        if (daysOffWeekId != null)
        {
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

        //Het wachtwoord van een nieuwe staff member is samenvoeging van de firstName en lastName
        userLoginDto.setStaffDto(staffDto);
        userLoginDto.setPassword(staffDto.getFirstName()+staffDto.getLastName());

        //staffDTO object zit in de userloginDTO object en wordt er samen mee opgeslagen.
        //Als de staff een aparte save doet staat die er 2x in
        userLoginService.saveUser(userLoginDto);
        return "staffList";
    }
}
