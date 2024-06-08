package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.DaysOffWeekDto;
import GroepswerkJava.DigitalPlanner.dto.StaffDto;
import GroepswerkJava.DigitalPlanner.model.DaysOffWeek;
import GroepswerkJava.DigitalPlanner.model.Staff;
import GroepswerkJava.DigitalPlanner.model.UserLogin;
import GroepswerkJava.DigitalPlanner.repository.CompanyRepository;
import GroepswerkJava.DigitalPlanner.repository.RoleRepository;
import GroepswerkJava.DigitalPlanner.repository.StaffRepository;
import GroepswerkJava.DigitalPlanner.repository.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Service
public class StaffServiceImpl implements StaffService{

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public StaffServiceImpl(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public List<StaffDto> getAll()
    {
        List<Staff> staff = staffRepository.findAll();
        return staff.stream()
                .map((staff1) -> mapToStaffDto(staff1))
                .collect(Collectors.toList());
    }

    /*
    Hier bij de save wordt alles eigenlijk gemapt van de DTO naar de model, dit kan misschien nog in een aparte methode als we een bestaande staff gaan updaten (voor toekomstige daniel)
     */
    @Override
    public void saveStaff(StaffDto staffDto)
    {
        Staff staff = new Staff();
        staff = mapToStaffModel(staffDto);
        staffRepository.save(staff);
    }

    @Transactional
    @Override
    public void saveOrUpdateStaff(Staff staff) {
        System.out.println(staff.getStaffId() + " ID, STAFF; " + staff);

        Staff existingStaff = staffRepository.findByStaffLastNameAndStaffFirstName(staff.getStaffLastName(), staff.getStaffFirstName());
        System.out.println("EXISITING STAFF / " + existingStaff);
        if (existingStaff != null) {
            existingStaff.setHoursInDebt(staff.getHoursInDebt());
            // Update other fields as needed
            staffRepository.save(existingStaff);
        } else {
            staffRepository.save(staff);
        }
    }

//    @Override
//    public void saveAllStaff(List<StaffDto> staffDtoList) {
//        List<Staff> staffList = new ArrayList<>();
//        for(StaffDto staffDto: staffDtoList){
//            Staff staff = mapToStaffModel(staffDto);
//            staffList.add(staff);
//        }
//        staffRepository.saveAll(staffList);
//    }

    @Override
    public void saveAllStaff(List<StaffDto> staffDtoList) {
        for(StaffDto staffDto: staffDtoList){
            Staff existingStaff = staffRepository.findById(staffDto.getStaffId()).orElse(null);

            if(existingStaff != null) {
                // Update existing staff
                Staff updatedStaff = mapToStaffModel(staffDto);
                existingStaff.setHoursInDebt(updatedStaff.getHoursInDebt());
//                existingStaff.setName(updatedStaff.getName());
//                existingStaff.setEmail(updatedStaff.getEmail());
                // Update other fields

                staffRepository.save(existingStaff);
            } else {
                // Create and save new staff
                Staff newStaff = mapToStaffModel(staffDto);
                staffRepository.save(newStaff);
            }
        }
    }


    //wordt niet gebruikt save en update wordt gedaan via userLogin
    @Override
    public void updateStaff(StaffDto staffDto)
    {
        //staffRepository.save(mapToUpdate(staffDto));
    }

    @Override
    public StaffDto getById(long id)
    {
        Optional<Staff> optional = staffRepository.findById(id);
        Staff staff = null;
        if(optional.isPresent()){
            staff = optional.get();
        } else {
            throw new RuntimeException("Staff not found.");
        }
        return mapToStaffDto(staff);
    }

    protected StaffDto mapToStaffDto(Staff staff)
    {
        StaffDto staffDto = new StaffDto();
        staffDto.setStaffId(staff.getStaffId());
        staffDto.setFirstName(staff.getStaffFirstName());
        staffDto.setLastName(staff.getStaffLastName());
        staffDto.setAddressLine1(staff.getAddressLine1());
        staffDto.setAddressLine2(staff.getAddressLine2());
        staffDto.setZipcode(staff.getZipCode());
        staffDto.setCity(staff.getCity());
        staffDto.setPhoneNumber(staff.getPhoneNumber());
        staffDto.setBirthDate(staff.getBirthDate());
        staffDto.setStartDate(staff.getStartDate());
        staffDto.setEmail(staff.getUserLogin().getEmail());
        staffDto.setStartDate(staff.getStartDate());
        if(staff.getEndDate() != null){
            staffDto.setEndDate(staff.getEndDate());
        }

        staffDto.setHoursInDebt(staff.getHoursInDebt());

        DepartmentServiceImpl departmentService = new DepartmentServiceImpl();
        ContractTypeServiceImpl contractTypeService = new ContractTypeServiceImpl();
        StaffFunctionServiceImpl staffFunctionService = new StaffFunctionServiceImpl();
        DaysOfWeekServiceImpl daysOfWeekService = new DaysOfWeekServiceImpl();
        CompanyServiceImpl companyService = new CompanyServiceImpl(companyRepository);

        staffDto.setDepartment(departmentService.mapToDepartmentDto(staff.getDepartment()));
        staffDto.setContractType(contractTypeService.mapToContractTypeDto(staff.getContractType()));
        staffDto.setFunction(staffFunctionService.mapToStaffFunctionDto(staff.getStaffFunction()));
        //staffDto.setCompanyDto(companyService.mapToCompanyDto(staff.getCompany()));

        if (staff.getEndDate() != null)
        {
            staffDto.setEndDate(staff.getEndDate());
        }

        if (staff.getDaysOffWeek() != null)
        {
            Set<DaysOffWeekDto> daysOffWeekDto = new HashSet<>();
            for(DaysOffWeek day : staff.getDaysOffWeek())
            {
                daysOffWeekDto.add(daysOfWeekService.mapToDaysOffWeekDto(day));
            }
            staffDto.setFixedDaysOff(daysOffWeekDto);
        }


        return staffDto;
    }

    public Staff mapToStaffModel(StaffDto staffDto)  //tijdelijk op public gezet, moet protected zijn ofwel extra methode de lijst van staff trg geeft
    {
        Staff staff = new Staff();

        if (staffDto.getStaffId() != 0)
        {
            staff.setStaffId(staffDto.getStaffId());
        }

        staff.setStaffFirstName(staffDto.getFirstName());
        staff.setStaffLastName(staffDto.getLastName());
        staff.setAddressLine1(staffDto.getAddressLine1());
        staff.setAddressLine2(staffDto.getAddressLine2());
        staff.setZipCode(staffDto.getZipcode());
        staff.setCity(staffDto.getCity());
        staff.setPhoneNumber(staffDto.getPhoneNumber());
        staff.setStartDate(staffDto.getStartDate());
        staff.setBirthDate(staffDto.getBirthDate());
        staff.setHoursInDebt(staffDto.getHoursInDebt());

        //validaties indien null, als ze null zijn sla ik ze over en voeg ik ze niet toe
        if (staffDto.getDepartment() != null)
        {
            DepartmentServiceImpl departmentService = new DepartmentServiceImpl();
            staff.setDepartment(departmentService.mapToDepartmentModel(staffDto.getDepartment()));
        }
        if (staffDto.getContractType() != null)
        {
            ContractTypeServiceImpl contractTypeService = new ContractTypeServiceImpl();
            staff.setContractType(contractTypeService.mapToContractTypeModel(staffDto.getContractType()));
        }
        if (staffDto.getFunction() != null)
        {
            StaffFunctionServiceImpl staffFunctionService = new StaffFunctionServiceImpl();
            staff.setStaffFunction(staffFunctionService.mapToStaffFunctionModel(staffDto.getFunction()));
        }

        CompanyServiceImpl companyService = new CompanyServiceImpl(companyRepository);
        //staff.setCompany(companyService.mapToCompany(staffDto.getCompanyDto()));

        //verantwoordelijke aanduiden
        //validaties indien null, als ze null zijn sla ik ze over en voeg ik ze niet toe
        if (staffDto.getStaffId() != 0)
        {
            //TODO in comments, er is ergens een fout in de controller
            Staff staff1 = new Staff();
            //staff1.setStaffId(getById(staffDto.getPersonResponsible().getStaffId()).getStaffId());
            //staff.setPersonResponsible(staff1);
        }
        if (staffDto.getEndDate() != null)
        {
            staff.setEndDate(staffDto.getEndDate());
        }

        //indien er geen vaste vrije dagen zijn doorgegeven gaat deze if blok niet worden uitgevoerd
        if (staffDto.getFixedDaysOff() != null)
        {
            Set<DaysOffWeek> daysOffWeeks = new HashSet<>();
            DaysOfWeekServiceImpl daysOfWeekService = new DaysOfWeekServiceImpl();

            for (DaysOffWeekDto days: staffDto.getFixedDaysOff())
            {
                DaysOffWeek daysOffWeek = daysOfWeekService.mapToDaysOffWeekModel(days);
                daysOffWeeks.add(daysOffWeek);
            }
            staff.setDaysOffWeek(daysOffWeeks);
        }

        return staff;
    }

    /*
    Gebruik van 2 mappers: om naar StaffDto te mappen en om naar Staff te mappen.

    in de mapToStaffDto() gebruik ik controle op nullwaardes, anders als deze niet worden meegegeven loopt het mis.
    Als ze null zijn worden deze niet ingevuld.

    Bij de mappToStaffModel() map ik terug naar  het model, maar hier gebruik ik voorlopig enkel het id omdat dit nu
    enkel wordt toegepast bij het creëren van de hiërarchie. Deze kan uitgebreidt worden wanneer de Staff zijn velden worden aangepast.
     */
}
