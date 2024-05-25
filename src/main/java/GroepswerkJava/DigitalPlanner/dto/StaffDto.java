package GroepswerkJava.DigitalPlanner.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
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
   private LocalDate birthDate;

   private StaffDto personResponsible;
   private DepartmentDto department;
   private StaffFunctionDto function;
   private LocalDate startDate;
   private LocalDate endDate;
   private ContractTypeDto contractType;
   //private UserLoginDto userLoginDto;
   private Set<DaysOffWeekDto> fixedDaysOff;



   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null || getClass() != obj.getClass()) {
         return false;
      }
      StaffDto other = (StaffDto) obj;
      return Objects.equals(firstName, other.firstName) &&
              Objects.equals(lastName, other.lastName) &&
              Objects.equals(startDate, other.startDate);
   }

   @Override
   public String toString() {
      return "StaffDto{" +
              "id='" + staffId + '\'' +
              "firstName='" + firstName + '\'' +
              ", lastName='" + lastName + '\'' +
              '}';
   }
}
