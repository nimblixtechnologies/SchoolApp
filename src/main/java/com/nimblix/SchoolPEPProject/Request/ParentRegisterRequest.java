package com.nimblix.SchoolPEPProject.Request;

import lombok.Data;

@Data
public class ParentRegisterRequest {

    private String fullName;
    private String emailId;
    private String password;
    private String contactNumber;
    private String address;
    private Long schoolId;
    private String role;
}
