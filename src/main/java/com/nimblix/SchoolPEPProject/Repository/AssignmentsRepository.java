package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.assignments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AssignmentsRepository extends JpaRepository<assignments,Long> {

    List<assignments> findByStudent_IdAndDueDateAfter(
            Long id, LocalDate date);

    List<assignments> findByStudent_IdAndDueDateBeforeAndCompletedFalse(
            Long id, LocalDate date);
}
