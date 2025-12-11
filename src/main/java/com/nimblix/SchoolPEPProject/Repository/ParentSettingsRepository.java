package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.ParentSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentSettingsRepository  extends JpaRepository<ParentSettings,Long> {


    Optional<ParentSettings> findByParentId(Long parentId);


}
