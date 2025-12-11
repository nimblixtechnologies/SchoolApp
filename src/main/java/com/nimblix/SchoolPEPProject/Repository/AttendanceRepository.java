package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository  extends JpaRepository<Attendance,Long> {



//    long countByStudentStudentId(Long studentId);

//    long countByStudentStudentIdAndPresentFalse(Long studentId);


    long countByStudentId(Long id);

    long countByStudentIdAndAttendanceStatus(Long id, String attendanceStatus);



    // Monthly (calendar-based) counts — ADD THIS

    long countByStudentIdAndAttendanceStatusAndAttendanceDateBetween(
            Long studentId,
            String attendanceStatus,
            String startDate,
            String endDate
    );
}
