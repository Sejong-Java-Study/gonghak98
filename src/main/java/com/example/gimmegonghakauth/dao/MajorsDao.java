package com.example.gimmegonghakauth.dao;

import com.example.gimmegonghakauth.domain.MajorsDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorsDao  extends JpaRepository<MajorsDomain, Long> {
    MajorsDomain findByMajor(String Major);

}
