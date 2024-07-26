package com.example.gimmegonghakauth.service.recommend;

import static com.example.gimmegonghakauth.service.recommend.MajorName.*;

import com.example.gimmegonghakauth.dao.UserDao;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import com.example.gimmegonghakauth.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RecommendServiceSelectManager {
    private final ApplicationContext applicationContext;
    private final UserDao userDao;

    // 학과에 따른 추천 서비스를 설정한다.
    public GonghakRecommendService selectRecommendService(Long studentId){
        MajorsDomain majorsDomain = userDao.findByStudentId(studentId).get()
                .getMajorsDomain();
        if(majorsDomain.getMajor().contains(ELEC_INFO.getName())){
            log.info("ElecInfoMajorGonghakRecommendService");
            return applicationContext.getBean("elecInfoMajorGonghakRecommendService",
                    ElecInfoMajorGonghakRecommendService.class);
        }
        log.info("ComputerMajorGonghakRecommendService");
        return applicationContext.getBean("computerMajorGonghakRecommendService",
                ComputerMajorGonghakRecommendService.class);
    }
}
