package com.example.gimmegonghakauth.config;

import com.example.gimmegonghakauth.dao.AbeekDao;
import com.example.gimmegonghakauth.dao.CompletedCoursesDao;
import com.example.gimmegonghakauth.dao.GonghakCorusesDao;
import com.example.gimmegonghakauth.dao.GonghakDao;
import com.example.gimmegonghakauth.dao.GonghakRepository;
import com.example.gimmegonghakauth.dao.MajorsDao;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GonghakConfig {

    private final AbeekDao abeekDao;
    private final CompletedCoursesDao completedCoursesDao;
    private final GonghakCorusesDao gonghakCorusesDao;
    private final MajorsDao majorsDao;

    @Bean
    public GonghakRepository gonghakRepository(){
        return new GonghakDao(abeekDao,completedCoursesDao,gonghakCorusesDao,majorsDao);
    }

//    @Bean
//    public GonghakCalculateService gonghakCalculateService(){
//        return new GonghakCalculateService(gonghakRepository());
//    }
}
