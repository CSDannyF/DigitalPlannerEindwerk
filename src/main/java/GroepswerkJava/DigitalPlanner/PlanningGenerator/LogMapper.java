package GroepswerkJava.DigitalPlanner.PlanningGenerator;
import GroepswerkJava.DigitalPlanner.dto.LogDto;
import GroepswerkJava.DigitalPlanner.model.Log;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LogMapper {
    // Singleton instance of mapper
    // Shared instance can be used in the entire application
    LogMapper INSTANCE = Mappers.getMapper(LogMapper.class);

    // Ignore logId when mapping from LogDto to Log
    @Mapping(target = "logId", ignore = true)
    Log logDtoToLog(LogDto logDto);
    LogDto logToLogDto(Log log);
}