//TODO OF VERWIJDEREN, OFWEL CLASSE MAKEN MET ALLE STATISCHE MAPPER METHODES
//package GroepswerkJava.DigitalPlanner.service;
//
//import GroepswerkJava.DigitalPlanner.dto.PlanningDetailDto;
//import GroepswerkJava.DigitalPlanner.dto.PlanningDto;
//import GroepswerkJava.DigitalPlanner.model.Planning;
//import GroepswerkJava.DigitalPlanner.model.PlanningDetail;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class PlanningDetailMapper {
//
//    private PlanningDetailMapper() {
//    }
//
////    public static PlanningDto mapToPlanningDto(Planning planning) {
////        PlanningDto planningDto = new PlanningDto();
////        if (planning != null) {
////            planningDto.setPlanningId(planning.getPlanningId());
////            if (planning.getPlanningStatus() != null) {
////                planningDto.setPlanningStatus(planning.getPlanningStatus().getPlanningStatusName());
////            }
////            planningDto.setPlannedByStaff(planning.getPlannedByStaff());
////            planningDto.setPlannedDate(planning.getPlannedDate());
////            List<PlanningDetailDto> planningDetailDtoList = new ArrayList<>();
////            for (PlanningDetail planningDetail : planning.getPlanningDetails()) {
////                PlanningDetailDto planningDetailDto = mapToPlanningDetailDto(planningDetail);
////                planningDetailDtoList.add(planningDetailDto);
////            }
////            planningDto.setPlanningDetails(planningDetailDtoList);
////        } else {
////            planningDto.setPlanningStatus("np");
////        }
////        return planningDto;
////    }
////
////    public static PlanningDetailDto mapToPlanningDetailDto(PlanningDetail planningDetail) {
////        PlanningDetailDto planningDetailDto = new PlanningDetailDto();
////        planningDetailDto.setPlanningDetailId(planningDetail.getPlanningDetailId());
////        planningDetailDto.setStartTime(planningDetail.getStartTime());
////        planningDetailDto.setEndTime(planningDetail.getEndTime());
////
////        planningDetailDto.setStaff(planningDetail.getStaff());
////
//////        planningDetailDto.setStaff(StaffServiceImpl.mapToStaffDto(planningDetail.getStaff()));
////        if (planningDetail.getAvailability() != null) {
////            planningDetailDto.setAvailability(planningDetail.getAvailability().getAvailabilityType());
////        }
////        return planningDetailDto;
////    }
//}
