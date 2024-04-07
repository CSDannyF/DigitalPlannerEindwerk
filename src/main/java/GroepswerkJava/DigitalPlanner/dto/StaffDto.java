package GroepswerkJava.DigitalPlanner.dto;

import GroepswerkJava.DigitalPlanner.model.UserLogin;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class StaffDto
{
   private long staffId;
   @NotEmpty
   private String firstName;
   @NotEmpty
   private String lastName;
   @NotEmpty
   private String addressLine1;
   private String addressLine2;
   @NotEmpty
   private String zipcode;
   @NotEmpty
   private String city;
   @NotEmpty
   private String phoneNumber;
   @NotEmpty
   private LocalDate birthDate;

   private StaffDto personResponsible;
   private DepartmentDto department;
   private StaffFunctionDto function;
   private LocalDate startDate;
   private ContractTypeDto contractType;
   private UserLogin userLogin;
   private Set<DaysOffWeekDto> fixedDaysOff;
}
