package com.nimblix.SchoolPEPProject.Response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParentReportCardResponse {

    private String studentName;
    private String term;
    private List<SubjectMarkResponse> subjects;
}
