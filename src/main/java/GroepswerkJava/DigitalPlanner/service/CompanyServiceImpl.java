package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.model.Company;
import GroepswerkJava.DigitalPlanner.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class CompanyServiceImpl implements IService<Company>{

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public List<Company> getAll() {
        return companyRepository.findAll();
    }

    @Override
    public void save(Company company) {
        this.companyRepository.save(company);
    }

    @Override
    public Company getById(long id) {
        Optional<Company> optional = companyRepository.findById(id);
        Company company = null;
        if(optional.isPresent()){
            company = optional.get();
        } else {
            throw new RuntimeException("Company not found");
        }
        return company;
    }



}
