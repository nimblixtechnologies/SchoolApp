package com.nimblix.SchoolPEPProject.Response;


import com.nimblix.SchoolPEPProject.Model.assignments;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParentDashboardResponse {

    private String parentName;
    private String studentName;
    private String grade;

    private double attendancePercentage;
    private long absentDays;

    private List<assignments> upcomingAssignments;
    private List<assignments> overdueAssignments;
}
