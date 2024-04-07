package GroepswerkJava.DigitalPlanner.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StaffFunctionDto {
    private long functionId;
    private String functionName;
    private String functionDescription;
}
