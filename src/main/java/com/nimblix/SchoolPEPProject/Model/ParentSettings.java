package com.nimblix.SchoolPEPProject.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "parent_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ParentSettings {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id", unique = true)
    private Long parentId;

    // Toggles from UI
    private boolean pushNotifications;
    private boolean emailSummaries;
    private boolean biometricLogin;

    //  Notification Category (GENERAL, EXAM, FEE, ATTENDANCE)
    private String notificationCategory;
}
