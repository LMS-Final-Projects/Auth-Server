package com.lms.authserver.major.repository;
import com.lms.authserver.major.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MajorRepository
        extends JpaRepository<Major, Long> {
    @Query("SELECT m.id from Major as m where m.majorName = :majorName")
    Long findIdByMajorName(@Param("majorName")String majorName);
}
