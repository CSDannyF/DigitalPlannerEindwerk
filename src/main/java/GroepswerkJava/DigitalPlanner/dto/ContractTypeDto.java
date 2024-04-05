package GroepswerkJava.DigitalPlanner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContractTypeDto {
    private long contractTypeId;
    private String contractTypeName;
    private int hoursPerWeek;
}
