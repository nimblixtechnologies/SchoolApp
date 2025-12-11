package com.nimblix.SchoolPEPProject.Request;


import lombok.Data;

@Data
public class ParentProfileUpdateRequest {


    private String fullName;
    private String emailId;
    private String contactNumber;
    private String address;
}
