package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.DaysOffWeekDto;
import GroepswerkJava.DigitalPlanner.dto.StaffDto;
import GroepswerkJava.DigitalPlanner.model.DaysOffWeek;
import GroepswerkJava.DigitalPlanner.model.Staff;
import GroepswerkJava.DigitalPlanner.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Service
public class StaffServiceImpl implements StaffService{

    @Autowired
    private StaffRepository staffRepository;

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

    // Veranderd van private naar protected voor PlanningDetailServiceImpl
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

        DepartmentServiceImpl departmentService = new DepartmentServiceImpl();
        ContractTypeServiceImpl contractTypeService = new ContractTypeServiceImpl();
        StaffFunctionServiceImpl staffFunctionService = new StaffFunctionServiceImpl();
        DaysOfWeekServiceImpl daysOfWeekService = new DaysOfWeekServiceImpl();

        if (staff.getDepartment() != null)
        {
            staffDto.setDepartment(departmentService.mapToDepartmentDto(staff.getDepartment()));
        }
        if (staff.getContractType() != null)
        {
            staffDto.setContractType(contractTypeService.mapToContractTypeDto(staff.getContractType()));
        }
        if (staff.getStaffFunction() != null)
        {
            staffDto.setFunction(staffFunctionService.mapToStaffFunctionDto(staff.getStaffFunction()));
        }
        if (staff.getEndDate() != null)
        {
            staffDto.setEndDate(staff.getEndDate());
        }

        if(staff.getDaysOffWeek() != null)
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

    protected Staff mapToStaffModel(StaffDto staffDto)
    {
        Staff staff = new Staff();
        staff.setStaffFirstName(staffDto.getFirstName());
        staff.setStaffLastName(staffDto.getLastName());
        staff.setAddressLine1(staffDto.getAddressLine1());
        staff.setAddressLine2(staffDto.getAddressLine2());
        staff.setZipCode(staffDto.getZipcode());
        staff.setCity(staffDto.getCity());
        staff.setPhoneNumber(staffDto.getPhoneNumber());
        staff.setStartDate(staffDto.getStartDate());
        staff.setBirthDate(staffDto.getBirthDate());

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
        if (staffDto.getStaffId() != 0)
        {
            Staff staff1 = new Staff();
            staff1.setStaffId(getById(staffDto.getStaffId()).getStaffId());
            staff.setPersonResponsible(staff1);
        }
        if (staffDto.getEndDate() != null)
        {
            staff.setEndDate(staffDto.getEndDate());
        }

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
