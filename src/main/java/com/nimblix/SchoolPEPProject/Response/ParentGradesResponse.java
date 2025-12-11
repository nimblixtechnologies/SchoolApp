package com.nimblix.SchoolPEPProject.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParentGradesResponse {

    private String studentName;
    private String overallGrade;
    private List<SubjectGrade> subjectGrades;
}
