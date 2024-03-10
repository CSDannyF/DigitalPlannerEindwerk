package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.model.Company;
import GroepswerkJava.DigitalPlanner.service.CompanyServiceImpl;
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

    @GetMapping("/new/company")
    public String showNewCompanyForm(Model model){
        Company company = new Company();
        model.addAttribute("company", company);
        return "new_company";
    }

    @PostMapping("/new/company")
    public String saveCompany(@ModelAttribute("company") Company company) {
        this.companyService.save(company);
        return "company_opening_days";
    }


}
