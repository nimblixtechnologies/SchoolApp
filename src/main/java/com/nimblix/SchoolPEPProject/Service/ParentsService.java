package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Model.Parent;
import com.nimblix.SchoolPEPProject.Request.*;
import com.nimblix.SchoolPEPProject.Response.*;

public interface ParentsService {



    AuthParentResponse signUp(ParentRegisterRequest request);
    AuthParentResponse signIn(ParentLoginRequest request);


    ParentDashboardResponse getDashboard(Long parentId);
    void linkStudent(Long parentId, Long studentId);

    ParentGradesResponse getGrades(Long parentId);


    ParentReportCardResponse getReportCard(Long parentId, String term);

    AttendanceSummaryResponse getChildMonthlyAttendanceSummary(
            Long parentId,
            Long studentId,
            int year,
            int month
    );



    ParentSettingsResponse getSettings(Long parentId);
    ParentSettingsResponse updateSettings(Long parentId, UpdateSettingsRequest request);



    void updateProfileByEmail(String email, ParentProfileUpdateRequest request);

    void changePasswordByEmail(String email, ParentChangePasswordRequest request);



}
