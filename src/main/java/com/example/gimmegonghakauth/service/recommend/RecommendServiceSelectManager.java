package com.example.gimmegonghakauth.service.recommend;

import static com.example.gimmegonghakauth.service.recommend.MajorName.*;

import com.example.gimmegonghakauth.dao.UserDao;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import com.example.gimmegonghakauth.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendServiceSelectManager {
    private final ApplicationContext applicationContext;
    private final UserDao userDao;

    public GonghakRecommendService selectRecommendService(LoginDto loginDto){
        MajorsDomain majorsDomain = userDao.findByStudentId(loginDto.getStudentId()).get()
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
