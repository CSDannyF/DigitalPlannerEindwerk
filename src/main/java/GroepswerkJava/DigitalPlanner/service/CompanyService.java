package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.model.Company;

import java.util.List;

public interface CompanyService {
    List<Company> getAllCompanies();

    void saveCompany(Company company);

    Company getCompanyById(long id);
}
