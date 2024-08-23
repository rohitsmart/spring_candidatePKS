package com.candidate.pks.Interview.dto;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleResponseList {
    private boolean data;
    private List<ScheduleResponseDTO> scheduleResponseDTO;
}
