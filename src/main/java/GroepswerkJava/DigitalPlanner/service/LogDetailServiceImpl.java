package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.PlanningGenerator.LogDetailMapper;
import GroepswerkJava.DigitalPlanner.dto.LogDetailDto;
import GroepswerkJava.DigitalPlanner.model.LogDetail;
import GroepswerkJava.DigitalPlanner.model.LogType;
import GroepswerkJava.DigitalPlanner.repository.LogDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

//*** Mappers getest met MapStruct ***

@Service
public class LogDetailServiceImpl implements LogDetailService {

    @Autowired
    LogDetailRepository logDetailRepository;

    private final LogDetailMapper logDetailMapper = LogDetailMapper.INSTANCE;

    @Override
    public void saveLogDetail(LogDetailDto logDetailDto) {
        LogDetail logDetail = logDetailMapper.logDetailDtoToLogDetail(logDetailDto);
        logDetailRepository.save(logDetail);
    }

    @Override
    public LogDetailDto findById(Long logDetailId) {
        return logDetailRepository.findById(logDetailId).map(logDetailMapper::logDetailToLogDetailDto).orElse(null);
    }

    @Override
    public List<LogDetailDto> findAllLogDetails() {
        return logDetailRepository.findAll().stream()
                .map(logDetailMapper::logDetailToLogDetailDto)
                .collect(Collectors.toList());
    }

    @Override
    public LogDetailDto updateLogDetail(Long logDetailId, LogDetailDto logDetailDto) {
        if (logDetailRepository.existsById(logDetailId)) {
            LogDetail logDetail = logDetailMapper.logDetailDtoToLogDetail(logDetailDto);
            logDetail.setLogDetailId(logDetailId);
            logDetail = logDetailRepository.save(logDetail);
            return logDetailMapper.logDetailToLogDetailDto(logDetail);
        }
        return null;
    }

    @Override
    public void deleteLogDetail(Long logDetailId) {
        if (logDetailRepository.existsById(logDetailId)) {
            logDetailRepository.deleteById(logDetailId);
        } else {
            throw new RuntimeException("Log detail not found.");
        }
    }

    @Override
    public void addLog(List<LogDetailDto> logging, String message, LogType logType) {
        LogDetailDto logDetailDto = new LogDetailDto();
        logDetailDto.setMessage(message);
        logDetailDto.setLogType(logType);
        logDetailDto.setTimestamp(LocalDate.now());
        logging.add(logDetailDto);
    }
}