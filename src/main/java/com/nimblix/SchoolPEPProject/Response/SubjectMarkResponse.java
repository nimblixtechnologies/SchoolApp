package com.nimblix.SchoolPEPProject.Response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectMarkResponse {

    private String subject;
    private Integer marks;
    private String grade;
}
