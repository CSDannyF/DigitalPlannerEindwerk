package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.dto.OpeningDaysDto;
import GroepswerkJava.DigitalPlanner.model.Company;
import GroepswerkJava.DigitalPlanner.model.WeeklyOpeningDay;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import GroepswerkJava.DigitalPlanner.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class CompanyController {

    @Autowired
    private IService<Company> companyService;

    @Autowired
    private WeeklyOpeningDaysService weeklyOpeningDaysService;

    @Autowired
    private WorkingDayService workingDayService;

    @GetMapping("/company")
    public String showNewCompanyForm(Model model) {
        Company company = new Company();
        model.addAttribute("company", company);
        return "newCompany";
    }

    @PostMapping("/company")
    public String saveCompany(@ModelAttribute("company") Company company, Model model) {
        this.companyService.save(company);
        OpeningDaysDto openingDaysDto = new OpeningDaysDto();
        openingDaysDto.addOpeningDay(new WeeklyOpeningDay("Monday"));
        openingDaysDto.addOpeningDay(new WeeklyOpeningDay("Tuesday"));
        openingDaysDto.addOpeningDay(new WeeklyOpeningDay("Wednesday"));
        openingDaysDto.addOpeningDay(new WeeklyOpeningDay("Thursday"));
        openingDaysDto.addOpeningDay(new WeeklyOpeningDay("Friday"));
        openingDaysDto.addOpeningDay(new WeeklyOpeningDay("Saturday"));
        openingDaysDto.addOpeningDay(new WeeklyOpeningDay("Sunday"));
        model.addAttribute("form", openingDaysDto);
        return "companyOpeningDays";
    }

    @PostMapping("company/openingdays")
    public String saveOpeningDays(@ModelAttribute OpeningDaysDto form, Model model) {
        weeklyOpeningDaysService.overWriteAll(form.getOpeningDaysList());
        model.addAttribute("openingDays", weeklyOpeningDaysService.getAll());
        workingDayService.fillRestOfYear(weeklyOpeningDaysService.getAll());
        model.addAttribute("exceptionDay", new WorkingDay());
        return "yearlyExceptions";
    }

    @PostMapping("company/exception")
    public String saveException(@ModelAttribute WorkingDay exceptionDay, Model model){
        workingDayService.updateWorkingDay(exceptionDay);
        WorkingDay workingDay= new WorkingDay();
        model.addAttribute("exceptionDay", workingDay);
        return "yearlyExceptions";
    }
}
