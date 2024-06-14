package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.CompanyDto;
import GroepswerkJava.DigitalPlanner.dto.StatisticsDto;
import GroepswerkJava.DigitalPlanner.model.Company;
import GroepswerkJava.DigitalPlanner.repository.CompanyRepository;
import GroepswerkJava.DigitalPlanner.repository.ContractTypeRepository;
import GroepswerkJava.DigitalPlanner.repository.DepartmentRepository;
import GroepswerkJava.DigitalPlanner.repository.StaffFunctionRepository;
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
    private DepartmentRepository departmentRepository;

    @Autowired
    private StaffFunctionRepository staffFunctionRepository;

    @Autowired
    private ContractTypeRepository contractTypeRepository;

    @Autowired
    private StaffService staffService;

    @Autowired
    private WeeklyOpeningDaysService weeklyOpeningDaysService;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    @Override
    public List<CompanyDto> getAll()
    {
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
    public StatisticsDto getStatistics(){
        StatisticsDto statisticsDto = new StatisticsDto();
        int totalStaffContractHours = staffService.getTotalStaffContractHours();
        statisticsDto.setTotalStaffContractHours(totalStaffContractHours);
        int totalWorkingHoursNeeded = weeklyOpeningDaysService.getTotalShiftHoursPerWeek();
        statisticsDto.setTotalWorkingHoursNeeded(totalWorkingHoursNeeded);

        // Calculate percentage
        double percentage = (double) totalStaffContractHours / totalWorkingHoursNeeded * 100;
        int roundedPercentage = (int) percentage;
        statisticsDto.setPercentage(roundedPercentage);
        return statisticsDto;
    }

    @Override
    public CompanyDto getById(long id)
    {
        Optional<Company> optional = companyRepository.findById(id);
        Company company = null;
        if(optional.isPresent()){
            company = optional.get();
        } else {
            throw new RuntimeException("Company not found");
        }
        return mapToCompanyDto(company);
    }

    protected CompanyDto mapToCompanyDto(Company company)
    {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCompanyId(company.getCompanyId());
        companyDto.setName(company.getName());
        companyDto.setStaff(company.getStaff());
        return companyDto;
    }

    protected Company mapToCompany(CompanyDto companyDto)
    {
        Company company = new Company();
        company.setCompanyId(companyDto.getCompanyId());
        company.setName(companyDto.getName());
        company.setStaff(companyDto.getStaff());
        return company;
    }

    public boolean isSetupDone()
    {
        boolean isSetupDone = true;
        if (getAll().isEmpty() || departmentRepository.findAll().isEmpty() || staffFunctionRepository.findAll().isEmpty() || contractTypeRepository.findAll().isEmpty())
        {
            isSetupDone = false;
        }
        return isSetupDone;
    }
}
