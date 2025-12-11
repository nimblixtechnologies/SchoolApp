package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent,Long> {


    Optional<Parent> findByEmailId(String emailId);

//    Optional<Parent> findByEmailIdAndSchoolId(String emailId, Long schoolId);

    boolean existsByEmailId(String emailId);

//    boolean existsByParentIdAndStudentsStudentId(Long parentId, Long studentId);

}
