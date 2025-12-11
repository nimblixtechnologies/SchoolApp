package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository  extends JpaRepository<Grade,Long> {

    List<Grade> findByStudentId(Long studentId);
}
