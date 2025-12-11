package com.nimblix.SchoolPEPProject.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParentSettingsResponse {

    private boolean pushNotifications;
    private boolean emailSummaries;
    private boolean biometricLogin;
    private String notificationCategory;
}
