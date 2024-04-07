package GroepswerkJava.DigitalPlanner.dto;

import GroepswerkJava.DigitalPlanner.model.Staff;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {
    private long companyId;

    @NotEmpty(message = "Email should not be empty")
    private String name;

    private List<Staff> staff;

}
