package com.nimblix.SchoolPEPProject.Request;

import lombok.Data;

@Data
public class ParentChangePasswordRequest {

    private String oldPassword;
    private String newPassword;
}
