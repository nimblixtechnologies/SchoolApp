package com.nimblix.SchoolPEPProject.Request;

import lombok.Data;

@Data
public class ParentLoginRequest {

    private String emailId;
    private String password;
    private Long schoolId;
}
