package GroepswerkJava.DigitalPlanner.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AvailabilityDto {
    private long availabilityId;
    private String availabilityType;

    @Override
    public String toString() {
        return "Availability{" +
                "Type='" + availabilityType + '\'' +
                '}';
    }
}
