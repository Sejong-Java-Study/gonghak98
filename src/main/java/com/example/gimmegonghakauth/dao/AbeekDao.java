package com.example.gimmegonghakauth.dao;

import com.example.gimmegonghakauth.domain.AbeekDomain;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbeekDao extends JpaRepository<AbeekDomain, Long> {

    List<AbeekDomain> findAllByYearAndMajorsDomain(int year, MajorsDomain majorsDomain);
}
