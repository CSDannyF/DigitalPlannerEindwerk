package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.CompanyDto;
import GroepswerkJava.DigitalPlanner.model.Company;
import GroepswerkJava.DigitalPlanner.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Service
public class CompanyServiceImpl implements CompanyService{

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    @Override
    public List<CompanyDto> getAll() {
        List<Company> companyList = companyRepository.findAll();
        List<CompanyDto> companyDtoList = new ArrayList<>();
        for (Company company: companyList){
            CompanyDto companyDto = mapToCompanyDto(company);
            companyDtoList.add(companyDto);
        }

        return companyDtoList;
    }

    @Override
    public void save(CompanyDto companyDto) {
        companyRepository.save(mapToCompany(companyDto));
    }

    @Override
    public CompanyDto getById(long id) {
        Optional<Company> optional = companyRepository.findById(id);
        Company company = null;
        if(optional.isPresent()){
            company = optional.get();
        } else {
            throw new RuntimeException("Company not found");
        }
        return mapToCompanyDto(company);
    }

    private CompanyDto mapToCompanyDto(Company company){
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCompanyId(company.getCompanyId());
        companyDto.setName(company.getName());
        companyDto.setStaff(company.getStaff());
        return companyDto;
    }

    private Company mapToCompany(CompanyDto companyDto){
        Company company = new Company();
        company.setCompanyId(companyDto.getCompanyId());
        company.setName(companyDto.getName());
        company.setStaff(companyDto.getStaff());
        return company;
    }
}
