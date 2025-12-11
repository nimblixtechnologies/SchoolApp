package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.ReportCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportCardRepository extends JpaRepository<ReportCard,Long> {

    List<ReportCard> findByStudentIdAndTerm(Long studentId, String term);
}
