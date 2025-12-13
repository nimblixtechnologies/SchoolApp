package com.nimblix.SchoolPEPProject.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceSummaryResponse {

    private String studentName;
    private double attendancePercentage;
    private long daysPresent;
    private long daysAbsent;
    private long tardies;
}
