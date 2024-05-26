package com.example.gimmegonghakauth.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import com.example.gimmegonghakauth.dao.AbeekDao;
import com.example.gimmegonghakauth.dao.GonghakCorusesDao;
import com.example.gimmegonghakauth.dao.GonghakDao;
import com.example.gimmegonghakauth.dao.GonghakRepository;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import com.example.gimmegonghakauth.domain.UserDomain;
import com.example.gimmegonghakauth.dto.GonghakResultDto.ResultPointDto;
import com.example.gimmegonghakauth.service.GonghakCalculateServiceTest.CalculateTestConfig;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(CalculateTestConfig.class)
@Slf4j
class GonghakCalculateServiceTest {

    @Autowired
    private GonghakCalculateService gonghakCalculateService;

    private static final MajorsDomain TEST_MAJORSDOMAIN = MajorsDomain.builder()
        .id(1L)
        .major("컴퓨터공학과").build();

    private static final UserDomain TEST_USERDOMAIN = UserDomain.builder()
        .email("testEmail")
        .name("홍지섭")
        .password("qwer")
        .studentId(19011706L)
        .majorsDomain(TEST_MAJORSDOMAIN).build();

    @TestConfiguration
    @RequiredArgsConstructor
    static class CalculateTestConfig{
        private final AbeekDao abeekDao;
        private final GonghakCorusesDao gonghakCorusesDao;

        @Bean
        public GonghakRepository gonghakRepository(){
            return new GonghakDao(abeekDao,gonghakCorusesDao);
        }

        @Bean
        public GonghakCalculateService gonghakCalculateService(){
            return new GonghakCalculateService(gonghakRepository());
        }
    }

    @Test
    @DisplayName("check log gonghakCalculateServiceTest")
    void logGonghakCalculateServiceTest(){
            Map<AbeekTypeConst, ResultPointDto> userResultRatio = gonghakCalculateService.getResultRatio(
            TEST_USERDOMAIN).get().getUserResultRatio();

        log.info("userResultRatio = {}",userResultRatio);
    }

    @Test
    @DisplayName("컴퓨터공학과 GonghakCalculateService 계산 결과 체크")
    void correctGonghakCalculateServiceTestCom(){
        Map<AbeekTypeConst, ResultPointDto> userResultRatio = gonghakCalculateService.getResultRatio(
            TEST_USERDOMAIN).get().getUserResultRatio();

        userResultRatio.keySet().forEach(
            abeekTypeConst -> {
                Double userPoint = userResultRatio.get(abeekTypeConst).getUserPoint();
                assertThat(userPoint).isLessThanOrEqualTo(1);
                assertThat(userPoint).isGreaterThanOrEqualTo(0);
            }
        );
    }

<<<<<<< HEAD
}
=======
    private double getExpectedCredit(int credit,Map<AbeekTypeConst, Integer> standard,AbeekTypeConst abeekTypeConst) {
        return Double.valueOf(String.format("%.4f",(double) credit / standard.get(abeekTypeConst)));
    }
}
>>>>>>> heesu-test
