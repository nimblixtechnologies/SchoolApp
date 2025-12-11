package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Request.*;
import com.nimblix.SchoolPEPProject.Response.*;
import com.nimblix.SchoolPEPProject.Service.ParentsService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parent/auth")
@RequiredArgsConstructor
public class ParentsController {



    private final ParentsService parentsService;


    @PostMapping("/signup")
    public ResponseEntity<AuthParentResponse> signUp(@RequestBody ParentRegisterRequest request){

        return ResponseEntity.ok(parentsService.signUp(request));

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthParentResponse> signIn(@RequestBody ParentLoginRequest request){
        return ResponseEntity.ok(parentsService.signIn(request));
    }



    @GetMapping("/dashboard/{parentId}")
    public ResponseEntity<ParentDashboardResponse> getDashboard(@PathVariable Long parentId){

        return ResponseEntity.ok(
                parentsService.getDashboard(parentId)
        );


    }




    @PostMapping("/link-student")
    public ResponseEntity<String> linkStudent(
            @RequestParam Long parentId,
            @RequestParam Long studentId) {

        parentsService.linkStudent(parentId, studentId);
        return ResponseEntity.ok("Student linked successfully");
    }




    @GetMapping("/grades/{parentId}")
    public ResponseEntity<ParentGradesResponse> getGrades(@PathVariable Long parentId) {
        return ResponseEntity.ok(parentsService.getGrades(parentId));
    }



    @GetMapping("/report-card/{parentId}")
    public ResponseEntity<ParentReportCardResponse> getReportCard(
            @PathVariable Long parentId,
            @RequestParam String term)
    {
        return ResponseEntity.ok(parentsService.getReportCard(parentId, term));
    }



    @GetMapping("/{parentId}/attendance/{studentId}/monthly")
    public ResponseEntity<AttendanceSummaryResponse> getMonthlyAttendance(
            @PathVariable Long parentId,
            @PathVariable Long studentId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ResponseEntity.ok(
                parentsService.getChildMonthlyAttendanceSummary(
                        parentId, studentId, year, month
                )
        );
    }



    // SETTINGS APIs (AS YOU REQUESTED)

    // GET SETTINGS
    @GetMapping("/settings/{parentId}")
    public ResponseEntity<ParentSettingsResponse> getSettings(
            @PathVariable Long parentId) {

        return ResponseEntity.ok(
                parentsService.getSettings(parentId)
        );
    }

    //  UPDATE SETTINGS (Push, Email, Biometric, Category)
    @PutMapping("/settings/{parentId}")
    public ResponseEntity<ParentSettingsResponse> updateSettings(
            @PathVariable Long parentId,
            @RequestBody UpdateSettingsRequest request) {

        return ResponseEntity.ok(
                parentsService.updateSettings(parentId, request)
        );
    }



    // UPDATE PROFILE (JWT BASED)
    @PutMapping("/settings/profile")
    public ResponseEntity<String> updateProfile(
            @RequestBody ParentProfileUpdateRequest request,
            Authentication authentication) {

        String email = authentication.getName(); //  from JWT
        parentsService.updateProfileByEmail(email, request);

        return ResponseEntity.ok("Profile updated successfully");
    }

    // CHANGE PASSWORD


    @PutMapping("/settings/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody ParentChangePasswordRequest request,
            Authentication authentication) {

        String email = authentication.getName(); //  FROM JWT
        parentsService.changePasswordByEmail(email, request);

        return ResponseEntity.ok("Password updated successfully");
    }



}
