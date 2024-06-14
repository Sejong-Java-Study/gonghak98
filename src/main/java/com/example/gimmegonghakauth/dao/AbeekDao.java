package com.example.gimmegonghakauth.dao;

import com.example.gimmegonghakauth.domain.AbeekDomain;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbeekDao extends JpaRepository<AbeekDomain, Long> {

//    @Query("select new com.example.gimmegonghakauth.domain.AbeekDomain(AD.id, AD.majorsDomain, AD.year, AD.abeekType, AD.minCredit, AD.note) from AbeekDomain AD"
//        + " where AD.year = :year and AD.majorsDomain = :majorsDomain")
//    List<AbeekDomain> findAllByYearAndMajorsDomain(@Param("year") int year,@Param("majorsDomain") MajorsDomain majorsDomain);

    List<AbeekDomain> findAllByYearAndMajorsDomain(int year, MajorsDomain majorsDomain);
}
