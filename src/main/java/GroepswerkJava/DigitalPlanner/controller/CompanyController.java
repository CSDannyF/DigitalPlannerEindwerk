package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.dto.OpeningDaysDto;
import GroepswerkJava.DigitalPlanner.model.Company;
import GroepswerkJava.DigitalPlanner.model.WeeklyOpeningDay;
import GroepswerkJava.DigitalPlanner.service.CompanyServiceImpl;
import GroepswerkJava.DigitalPlanner.service.WeeklyOpeningDaysServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CompanyController {

    @Autowired
    private CompanyServiceImpl companyService;

    @Autowired
    private WeeklyOpeningDaysServiceImpl weeklyOpeningDaysService;

    @GetMapping("/new/company")
    public String showNewCompanyForm(Model model) {
        Company company = new Company();
        model.addAttribute("company", company);
        return "new_company";
    }

    @PostMapping("/openingDays")
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
        return "company_opening_days";
    }

    @PostMapping("saveOpeningDays")
    public String saveOpeningDays(@ModelAttribute OpeningDaysDto form, Model model) {
        weeklyOpeningDaysService.truncateAll();
        weeklyOpeningDaysService.saveAll(form.getOpeningDaysList());

        model.addAttribute("openingDays", weeklyOpeningDaysService.getAll());
        return "index";
    }


}
