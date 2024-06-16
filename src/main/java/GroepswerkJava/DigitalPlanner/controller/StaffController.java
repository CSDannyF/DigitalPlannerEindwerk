package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.dto.*;
import GroepswerkJava.DigitalPlanner.model.ContractType;
import GroepswerkJava.DigitalPlanner.model.StaffFunction;
import GroepswerkJava.DigitalPlanner.repository.CompanyRepository;
import GroepswerkJava.DigitalPlanner.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
public class StaffController {
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

    @Autowired
    private WeeklyOpeningDaysService weeklyOpeningDaysService;

    @Autowired
    private CompanyService companyService;

    @GetMapping("/staffList")
    public String showStaffList(Model model,
                                Authentication authentication) {

        StaffDto staffDto = new StaffDto();
        List<StaffDto> staffDtos = staffService.getAll();
        UserLoginDto loggedInUser = userLoginService.getByEmail(authentication.getName());

        boolean isAdmin = isAdmin(authentication);
        String firstName = authentication.getName().split("@")[0];
        StatisticsDto statisticsDto = companyService.getStatistics();

        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("staffMembers", staffDtos);
        model.addAttribute("staffDto", staffDto);
        model.addAttribute("statistics", statisticsDto);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);

        return "staffList";
    }


    @GetMapping("/staffDetail/{staffId}")
    public String staffDetail(@PathVariable long staffId,
                              Model model,
                              Authentication authentication) {

        StaffDto staffDto = staffService.getById(staffId);
        UserLoginDto userLogin = userLoginService.getByEmail(staffDto.getEmail());
        UserLoginDto loggedInUser = userLoginService.getByEmail(authentication.getName());

        boolean isAdmin = isAdmin(authentication);
        String firstName = authentication.getName().split("@")[0];
        StatisticsDto statisticsDto = companyService.getStatistics();

        model.addAttribute("staff", staffDto);
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("userLogin", userLogin);
        model.addAttribute("statistics", statisticsDto);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);

        return "/staffDetail";
    }

    @GetMapping("/myProfile")
    public String myProfile(Authentication authentication,
                            Model model) {

        UserLoginDto loggedInUser = userLoginService.getByEmail(authentication.getName());
        StaffDto staffDto = loggedInUser.getStaffDto();

        boolean isAdmin = isAdmin(authentication);
        String firstName = authentication.getName().split("@")[0];
        StatisticsDto statisticsDto = companyService.getStatistics();

        model.addAttribute("statistics", statisticsDto);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);
        model.addAttribute("staff", staffDto);
        model.addAttribute("loggedInUser", loggedInUser);

        return "myProfile";
    }

    @GetMapping("/editStaff/{staffId}")
    public String editStaff(@PathVariable long staffId,
                            Model model,
                            Authentication authentication) {

        UserLoginDto loggedInUser = userLoginService.getByEmail(authentication.getName());

        StaffDto staffDto = staffService.getById(staffId);
        List<StaffDto> staffDtos = staffService.getAll();
        UserLoginDto userLoginDto = userLoginService.getByEmail(staffDto.getEmail());

        boolean isAdmin = isAdmin(authentication);
        String firstName = authentication.getName().split("@")[0];
        StatisticsDto statisticsDto = companyService.getStatistics();

        model.addAttribute("staffDto", staffDto);
        model.addAttribute("staffMembers", staffDtos);
        model.addAttribute("userLogin", userLoginDto);
        model.addAttribute("loggedInUser", loggedInUser);

        model.addAttribute("departmentDto", new DepartmentDto());
        model.addAttribute("departments", departmentService.getAll());

        model.addAttribute("staffFunctionDto", new StaffFunctionDto());
        model.addAttribute("staffFunctions", staffFunctionService.getAll());

        model.addAttribute("contractTypeDto", new ContractTypeDto());
        model.addAttribute("contractTypes", contractTypeService.getAll());

        model.addAttribute("dayOffWeekDto", new DaysOffWeekDto());
        model.addAttribute("daysOffWeek", daysOfWeekService.getAll());

        model.addAttribute("statistics", statisticsDto);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);

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
                                  Authentication authentication,
                                  Model model) {

        UserLoginDto loggedInUser = userLoginService.getByEmail(authentication.getName());
        StaffDto staffFromDb = staffService.getById(staffId);

        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("userLogin", userLoginDto);
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

        if (departmentId == 0 || functionId == 0 || contractTypeId == 0) {
            model.addAttribute("error", "The fields department, contract type and staff function are required");
            return "editStaff";
        }

        UserLoginDto existing = userLoginService.getByEmail(userLoginDto.getEmail());
        if (existing.getEmail() != null & !staffFromDb.getEmail().equals(staffDto.getEmail())) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            return "editStaff";
        }

        DepartmentDto department = departmentService.getById(departmentId);
        staffDto.setDepartment(department);

        ContractTypeDto contractType = contractTypeService.getById(contractTypeId);
        staffDto.setContractType(contractType);

        StaffFunctionDto staffFunction = staffFunctionService.getById(functionId);
        staffDto.setFunction(staffFunction);

        if (staffId != 0) {
            StaffDto staffResponsible = staffService.getById(staffId);
            staffDto.setPersonResponsible(staffResponsible);
        }

        if (daysOffWeekId != null) {
            Set<DaysOffWeekDto> daysOff = new HashSet<>();
            String days[] = daysOffWeekId.split(",");

            for (String day : days) {
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
    public String newStaff(Model model, Authentication authentication) {
        UserLoginDto loggedInUser = userLoginService.getByEmail(authentication.getName());

        UserLoginDto userLoginDto = new UserLoginDto();
        StaffDto staffDto = new StaffDto();
        List<StaffDto> staffDtos = staffService.getAll();

        boolean isAdmin = isAdmin(authentication);
        String firstName = authentication.getName().split("@")[0];
        StatisticsDto statisticsDto = companyService.getStatistics();

        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("staffDto", staffDto);
        model.addAttribute("staffMembers", staffDtos);
        model.addAttribute("userLogin", userLoginDto);

        model.addAttribute("departmentDto", new DepartmentDto());
        model.addAttribute("departments", departmentService.getAll());

        model.addAttribute("staffFunctionDto", new StaffFunctionDto());
        model.addAttribute("staffFunctions", staffFunctionService.getAll());

        model.addAttribute("contractTypeDto", new ContractTypeDto());
        model.addAttribute("contractTypes", contractTypeService.getAll());

        model.addAttribute("dayOffWeekDto", new DaysOffWeekDto());
        model.addAttribute("daysOffWeek", daysOfWeekService.getAll());

        model.addAttribute("statistics", statisticsDto);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);

        return "newStaff";
    }

    @PostMapping("/newStaff")
    public String saveStaff(@Valid @ModelAttribute("staffDto") StaffDto staffDto,
                            @ModelAttribute("userLogin") UserLoginDto userLoginDto,
                            @RequestParam(value = "departmentId") long departmentId,
                            @RequestParam("functionId") long functionId,
                            @RequestParam("contractTypeId") long contractTypeId,
                            @RequestParam(value = "daysOffWeekId", required = false) String daysOffWeekId,
                            BindingResult result,
                            Model model, Authentication authentication) {

        UserLoginDto loggedInUser = userLoginService.getByEmail(authentication.getName());
        boolean isAdmin = isAdmin(authentication);
        String firstName = authentication.getName().split("@")[0];
        StatisticsDto statisticsDto = companyService.getStatistics();

        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("userLogin", new UserLoginDto());
        model.addAttribute("staffMembers", staffService.getAll());

        model.addAttribute("departmentDto", new DepartmentDto());
        model.addAttribute("departments", departmentService.getAll());

        model.addAttribute("staffFunctionDto", new StaffFunction());
        model.addAttribute("staffFunctions", staffFunctionService.getAll());

        model.addAttribute("contractTypeDto", new ContractType());
        model.addAttribute("contractTypes", contractTypeService.getAll());

        model.addAttribute("dayOffWeekDto", new DaysOffWeekDto());
        model.addAttribute("daysOffWeek", daysOfWeekService.getAll());

        model.addAttribute("statistics", statisticsDto);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);

        //check/validatie als departement, contract type en functie niet null zijn
        if (departmentId == 0 || functionId == 0 || contractTypeId == 0) {
            model.addAttribute("error", "The fields department, contract type and staff function are required");
            return "newStaff";
        }

        //Check als email uniek is
        UserLoginDto existing = userLoginService.getByEmail(userLoginDto.getEmail());
        if (existing.getEmail() != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            return "newStaff";
        }

        DepartmentDto department = departmentService.getById(departmentId);
        staffDto.setDepartment(department);

        ContractTypeDto contractType = contractTypeService.getById(contractTypeId);
        staffDto.setContractType(contractType);

        StaffFunctionDto staffFunction = staffFunctionService.getById(functionId);
        staffDto.setFunction(staffFunction);

        //set fixed days off
        if (daysOffWeekId != null) {
            Set<DaysOffWeekDto> daysOff = new HashSet<>();
            String days[] = daysOffWeekId.split(",");

            for (String day : days) {
                DaysOffWeekDto day2 = daysOfWeekService.getById(Long.parseLong(day));
                daysOff.add(day2);
            }
            staffDto.setFixedDaysOff(daysOff);
        }

        //Het wachtwoord van een nieuwe staff member is samenvoeging van de firstName en lastName
        userLoginDto.setStaffDto(staffDto);
        userLoginDto.setPassword(staffDto.getFirstName() + staffDto.getLastName());

        //staffDTO object zit in de userloginDTO object en wordt er samen mee opgeslagen.
        //Als de staff een aparte save doet staat die er 2x in
        userLoginService.saveUser(userLoginDto);
        return "staffList";
    }

    private boolean isAdmin(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        return isAdmin;
    }
}
