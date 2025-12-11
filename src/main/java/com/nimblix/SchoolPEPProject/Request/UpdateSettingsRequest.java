package com.nimblix.SchoolPEPProject.Request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSettingsRequest {


    private boolean pushNotifications;
    private boolean emailSummaries;
    private boolean biometricLogin;
    private String notificationCategory;
}
