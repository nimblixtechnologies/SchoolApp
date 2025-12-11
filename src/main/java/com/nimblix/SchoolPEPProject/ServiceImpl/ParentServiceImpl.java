package com.nimblix.SchoolPEPProject.ServiceImpl;


import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.*;
import com.nimblix.SchoolPEPProject.Repository.*;

import com.nimblix.SchoolPEPProject.Request.*;
import com.nimblix.SchoolPEPProject.Response.*;

import com.nimblix.SchoolPEPProject.Security.JwtUtil;
import com.nimblix.SchoolPEPProject.Service.ParentsService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;





@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentsService {


    private final ParentRepository parentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final StudentRepository studentRepository;

    private final AssignmentsRepository assignmentsRepository;
    private final AttendanceRepository attendanceRepository;

    private final GradeRepository gradeRepository;
    private final ReportCardRepository reportCardRepository;


    private final  ParentSettingsRepository parentSettingsRepository;


    // SignUp

    @Override
    public AuthParentResponse signUp(ParentRegisterRequest request) {


        if (parentRepository.existsByEmailId(request.getEmailId())) {
            return new AuthParentResponse(false, "Email already registered", null);
        }
        Parent parent = new Parent();
        parent.setFullName(request.getFullName());
        parent.setEmailId(request.getEmailId());
        parent.setContactNumber(request.getContactNumber());
        parent.setAddress(request.getAddress());
        parent.setSchoolId(request.getSchoolId());
        parent.setRole(ParentRole.valueOf(request.getRole().toUpperCase()));


        // Encrypt password
        parent.setPassword(passwordEncoder.encode(request.getPassword()));

        parentRepository.save(parent);


        return new AuthParentResponse(true, "Parent register successfully", null);
    }


    // signIn

    @Override
    public AuthParentResponse signIn(ParentLoginRequest request) {

        Parent parent = parentRepository.findByEmailId(request.getEmailId())
                .orElse(null);

        if (parent == null) {
            return new AuthParentResponse(false, "Email not found", null);
        }

        if (!passwordEncoder.matches(request.getPassword(), parent.getPassword())) {
            return new AuthParentResponse(false, "Invalid password", null);
        }

        // CREATE UserDetails FOR JWT

        org.springframework.security.core.userdetails.UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername(parent.getEmailId())
                        .password(parent.getPassword())
                        .authorities(parent.getRole().name())   // MOTHER, FATHER, etc.
                        .build();


        // GENERATE JWT TOKEN USING YOUR EXISTING JwtUtil
        String token = jwtUtil.generateToken(userDetails);


        return new AuthParentResponse(true, "Login successfully", token);
    }






    @Override
    public ParentDashboardResponse getDashboard(Long parentId) {


        Parent parent=parentRepository.findById(parentId)
                .orElseThrow(()-> new RuntimeException("Parent not found"));

        Student student=parent.getStudent();
        if (student == null) {
            throw new RuntimeException("No student is linked to this parent yet");
        }

        long totalDays = attendanceRepository.countByStudentId(student.getId());

        long absentDays = attendanceRepository
                .countByStudentIdAndAttendanceStatus(
                        student.getId(), "ABSENT");


        double attendancePercentage =
                totalDays == 0 ? 0 :
                        ((totalDays - absentDays) * 100.0) / totalDays;


        List<assignments> upcoming = assignmentsRepository
                .findByStudent_IdAndDueDateAfter(student.getId(), LocalDate.now());

        List<assignments> overdue = assignmentsRepository
                .findByStudent_IdAndDueDateBeforeAndCompletedFalse(student.getId(), LocalDate.now());



        return new ParentDashboardResponse(

                parent.getFullName(),
                student.getFullName(),
                student.getGrade(),
                attendancePercentage,
                absentDays,
                upcoming,
                overdue
        );
    }

    @Override
    public void linkStudent(Long parentId, Long studentId) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        parent.setStudent(student);
        parentRepository.save(parent);
    }



    @Override
    public ParentGradesResponse getGrades(Long parentId) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        Student student = parent.getStudent();
        if (student == null) {
            throw new RuntimeException("No student linked");
        }

        List<Grade> gradeList = gradeRepository.findByStudentId(student.getId());

        List<SubjectGrade> subjectGrades = gradeList.stream()
                .map(g -> new SubjectGrade(g.getSubject(), g.getGrade()))
                .toList();

        // Optional: Calculate average grade
        String overall = calculateOverallGrade(subjectGrades);

        return new ParentGradesResponse(
                student.getFullName(),
                overall,
                subjectGrades
        );

    }

    @Override
    public ParentReportCardResponse getReportCard(Long parentId, String term) {


        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        Student student = parent.getStudent();

        if (student == null) {
            throw new RuntimeException("No student linked to this parent");
        }

        List<ReportCard> reportCards =
                reportCardRepository.findByStudentIdAndTerm(student.getId(), term);

        List<SubjectMarkResponse> subjects = reportCards.stream()
                .map(r -> SubjectMarkResponse.builder()
                        .subject(r.getSubject())
                        .marks(r.getMarks())
                        .grade(r.getGrade())
                        .build())
                .toList();

        return ParentReportCardResponse.builder()
                .studentName(student.getFullName())
                .term(term)
                .subjects(subjects)
                .build();

    }

    @Override
    public AttendanceSummaryResponse getChildMonthlyAttendanceSummary
            (Long parentId, Long studentId, int year, int month) {

        //  1. Security check: student must belong to this parent
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

//        if (!student.getParent().getParentId().equals(parentId)) {
//            throw new RuntimeException("Unauthorized access to student attendance");
//        }
//        Parent parent = parentRepository.findById(parentId)
//                .orElseThrow(() -> new RuntimeException("Parent not found"));
//
//        if (parent.getStudent() == null ||
//                !parent.getStudent().getId().equals(studentId)) {
//            throw new RuntimeException("Unauthorized access to student attendance");
//        }

        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        if (parent.getStudent() == null ||
                !parent.getStudent().getId().equals(studentId)) {
            throw new RuntimeException("Unauthorized access to student attendance");
        }




        // 2. Build calendar date range
        String startDate = getMonthStartDate(year, month);
        String endDate = getMonthEndDate(year, month);

        // 3. Monthly attendance counts
        long present = attendanceRepository
                .countByStudentIdAndAttendanceStatusAndAttendanceDateBetween(
                        studentId,
                        SchoolConstants.PRESENT,
                        startDate,
                        endDate
                );

        long absent = attendanceRepository
                .countByStudentIdAndAttendanceStatusAndAttendanceDateBetween(
                        studentId,
                        SchoolConstants.ABSENT,
                        startDate,
                        endDate
                );

        long tardy = attendanceRepository
                .countByStudentIdAndAttendanceStatusAndAttendanceDateBetween(
                        studentId,
                        SchoolConstants.TARDY,
                        startDate,
                        endDate
                );

        // 4. Response for UI
        return AttendanceSummaryResponse.builder()
                .daysPresent(present)
                .daysAbsent(absent)
                .tardies(tardy)
                .build();

    }

          // GET SETTINGS
    @Override
    public ParentSettingsResponse getSettings(Long parentId) {


        ParentSettings settings = parentSettingsRepository
                .findByParentId(parentId)
                .orElseGet(() -> {

                    parentRepository.findById(parentId)
                            .orElseThrow(() -> new RuntimeException("Parent not found"));

                    return parentSettingsRepository.save(
                            ParentSettings.builder()
                                    .parentId(parentId)
                                    .pushNotifications(true)
                                    .emailSummaries(true)
                                    .biometricLogin(false)
                                    .notificationCategory("GENERAL")
                                    .build()
                    );
                });

        return ParentSettingsResponse.builder()
                .pushNotifications(settings.isPushNotifications())
                .emailSummaries(settings.isEmailSummaries())
                .biometricLogin(settings.isBiometricLogin())
                .notificationCategory(settings.getNotificationCategory())
                .build();
    }

           // UPDATE SETTINGS
    @Override
    public ParentSettingsResponse updateSettings(Long parentId, UpdateSettingsRequest request) {




        ParentSettings settings = parentSettingsRepository
                .findByParentId(parentId)
                .orElseThrow(() -> new RuntimeException("Settings not found"));

        settings.setPushNotifications(request.isPushNotifications());
        settings.setEmailSummaries(request.isEmailSummaries());
        settings.setBiometricLogin(request.isBiometricLogin());
        settings.setNotificationCategory(request.getNotificationCategory());

        parentSettingsRepository.save(settings);

        return getSettings(parentId);

    }

    @Override
    public void updateProfileByEmail(String email, ParentProfileUpdateRequest request) {
        Parent parent = parentRepository.findByEmailId(email)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        parent.setFullName(request.getFullName());
        parent.setEmailId(request.getEmailId());
        parent.setContactNumber(request.getContactNumber());
        parent.setAddress(request.getAddress());

        parentRepository.save(parent);

    }

    @Override
    public void changePasswordByEmail(String email, ParentChangePasswordRequest request) {

        Parent parent = parentRepository.findByEmailId(email)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), parent.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        parent.setPassword(passwordEncoder.encode(request.getNewPassword()));
        parentRepository.save(parent);

    }


    // UPDATE PROFILE
//    @Override
//    public void updateProfile(Long parentId, ParentProfileUpdateRequest request) {
//
//        Parent parent = parentRepository.findById(parentId)
//                .orElseThrow(() -> new RuntimeException("Parent not found"));
//
//        parent.setFullName(request.getFullName());
//        parent.setEmailId(request.getEmailId());
//        parent.setContactNumber(request.getContactNumber());
//        parent.setAddress(request.getAddress());
//
//        parentRepository.save(parent);
//
//    }

       // CHANGE PASSWORD

//    @Override
//    public void changePassword(Long parentId, ParentChangePasswordRequest request) {
//
//
//        Parent parent = parentRepository.findById(parentId)
//                .orElseThrow(() -> new RuntimeException("Parent not found"));
//
//        if (!passwordEncoder.matches(request.getOldPassword(), parent.getPassword())) {
//            throw new RuntimeException("Old password is incorrect");
//        }
//
//        parent.setPassword(passwordEncoder.encode(request.getNewPassword()));
//        parentRepository.save(parent);
//
//    }












    // Add Helper Method Here

    private String calculateOverallGrade(List<SubjectGrade> grades) {
        if (grades.isEmpty()) return "N/A";
        return grades.get(0).getGrade(); // Simple logic — update later
    }


   // added helper method

    private String getMonthStartDate(int year, int month) {
        return year + "-" + String.format("%02d", month) + "-01";
    }

    private String getMonthEndDate(int year, int month) {
        java.time.YearMonth yearMonth = java.time.YearMonth.of(year, month);
        return year + "-" + String.format("%02d", month) + "-" + yearMonth.lengthOfMonth();
    }



}
