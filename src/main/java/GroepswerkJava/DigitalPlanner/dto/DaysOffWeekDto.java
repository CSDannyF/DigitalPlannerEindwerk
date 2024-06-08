package GroepswerkJava.DigitalPlanner.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DaysOffWeekDto {
    private long daysOffWeekId;
    private String dayName;

    @Override
    public String toString() {
        return "DaysOffWeekDto{" +
                "Id=" + daysOffWeekId +
                ", dayName='" + dayName + '\'' +
                '}';
    }
}
