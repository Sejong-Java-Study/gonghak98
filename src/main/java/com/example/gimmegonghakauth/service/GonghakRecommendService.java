package com.example.gimmegonghakauth.service;

import com.example.gimmegonghakauth.dao.GonghakRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GonghakRecommendService {

    private final GonghakRepository gonghakRepository;


}
