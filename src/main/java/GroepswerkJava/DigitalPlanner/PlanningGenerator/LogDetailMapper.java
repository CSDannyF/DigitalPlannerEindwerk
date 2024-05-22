package GroepswerkJava.DigitalPlanner.PlanningGenerator;
import GroepswerkJava.DigitalPlanner.dto.LogDetailDto;
import GroepswerkJava.DigitalPlanner.model.LogDetail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LogDetailMapper {
    // Singleton instance of mapper
    // Shared instance can be used in the entire application
    LogDetailMapper INSTANCE = Mappers.getMapper(LogDetailMapper.class);

    LogDetailDto logDetailToLogDetailDto(LogDetail logDetail);
    LogDetail logDetailDtoToLogDetail(LogDetailDto logDetailDto);
}