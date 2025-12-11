package com.nimblix.SchoolPEPProject.Response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthStudentResponse {
    private Long userId;
    private String fullName;
    private String email;
    private String role;
    private String token;
    private String message;
}
