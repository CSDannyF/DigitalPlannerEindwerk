package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.dto.ContractTypeDto;
import GroepswerkJava.DigitalPlanner.model.ContractType;
import GroepswerkJava.DigitalPlanner.service.ContractTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

//controller moet nog aangepast worden om volledig beheer te doen
@Controller
public class ContractTypeController {

    @Autowired
    private ContractTypeServiceImpl contractTypeService;

    @GetMapping("/contractType")
    public String contractType(Model model)
    {
        ContractType contractType = new ContractType();
        model.addAttribute("contractType", contractType);
        return "contractType";
    }

    @PostMapping("/contractType")
    public String saveContractType(@ModelAttribute("contractType") ContractTypeDto contractType, Model model)  //model mag weg
    {
        this.contractTypeService.save(contractType);
        //moet veranderd worden naar admindashboard indien klaar
        return "dashboard";
    }
}
