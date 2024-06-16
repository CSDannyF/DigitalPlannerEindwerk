package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.LogDetailDto;
import GroepswerkJava.DigitalPlanner.model.LogType;

import java.util.List;

public interface LogDetailService {
    void saveLogDetail(LogDetailDto logDetailDto);

    LogDetailDto findById(Long logDetailId);

    List<LogDetailDto> findAllLogDetails();

    LogDetailDto updateLogDetail(Long logDetailId, LogDetailDto logDetailDto);

    void deleteLogDetail(Long logDetailId);

    void addLog(List<LogDetailDto> logging, String message, LogType logType);
}