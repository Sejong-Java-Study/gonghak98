package com.example.gimmegonghakauth.config;

import com.example.gimmegonghakauth.dao.AbeekDao;
import com.example.gimmegonghakauth.dao.GonghakCorusesDao;
import com.example.gimmegonghakauth.dao.GonghakDao;
import com.example.gimmegonghakauth.dao.GonghakRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GonghakConfig {

    private final AbeekDao abeekDao;
    private final GonghakCorusesDao gonghakCorusesDao;

    @Bean
    public GonghakRepository gonghakRepository(){
        return new GonghakDao(abeekDao,gonghakCorusesDao);
    }

//    @Bean
//    public GonghakCalculateService gonghakCalculateService(){
//        return new GonghakCalculateService(gonghakRepository());
//    }
}
