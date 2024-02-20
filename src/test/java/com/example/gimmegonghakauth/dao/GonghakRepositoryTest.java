package com.example.gimmegonghakauth.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import com.example.gimmegonghakauth.domain.AbeekDomain;
import com.example.gimmegonghakauth.domain.AbeekDomain.AbeekDomainBuilder;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import com.example.gimmegonghakauth.dto.GonghakStandardDto;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//출처: https://0soo.tistory.com/194?category=576925 [Lifealong:티스토리]
class GonghakRepositoryTest {

    @Autowired
    private GonghakRepository gonghakRepository;
    @Autowired
    private MajorsDao majorsDao;

    private final MajorsDomain testMajorsDomain = MajorsDomain.builder()
        .id(1L)
        .major("건설환경공학과").build();

    @BeforeAll
    void setTable(){
        log.info("----set table----");
        //        gonghakRepository.save(testMajorsDomain);
        majorsDao.save(testMajorsDomain);

        AbeekDomainBuilder abeek1 = AbeekDomain.builder()
            .id(1L)
            .abeekType(AbeekTypeConst.MAJOR)
            .majorsDomain(testMajorsDomain)
            .note("this is a test note")
            .year(19)
            .minCredit(54);
        AbeekDomainBuilder abeek2 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.MSC)
            .majorsDomain(testMajorsDomain)
            .note("this is a test note")
            .year(19)
            .minCredit(30);
        AbeekDomainBuilder abeek3 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.DESIGN)
            .majorsDomain(testMajorsDomain)
            .note("this is a test note")
            .year(19)
            .minCredit(12);
        AbeekDomainBuilder abeek4 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.PROFESSIONAL_NON_MAJOR)
            .majorsDomain(testMajorsDomain)
            .note("this is a test note")
            .year(19)
            .minCredit(14);
        AbeekDomainBuilder abeek5 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.MINIMUM_CERTI)
            .majorsDomain(testMajorsDomain)
            .note("this is a test note")
            .year(19)
            .minCredit(103);

        gonghakRepository.save(abeek1.build());
        gonghakRepository.save(abeek2.build());
        gonghakRepository.save(abeek3.build());
        gonghakRepository.save(abeek4.build());
        gonghakRepository.save(abeek5.build());
    }

    //GonghakStandardDto 상태 확인
    @Test
    @Rollback
    @DisplayName("GonghakStandardDto 5가지 상태 모두 포함되어있는지 확인")
    void findStandardKeySetTest(){
        Optional<GonghakStandardDto> standard = gonghakRepository.findStandard(19011706L,
            testMajorsDomain);
        log.info("testStandard status ={}",standard.get().getStandards());
        Map<AbeekTypeConst, Integer> testStandard = standard.get().getStandards();
        assertThat(testStandard.keySet()).contains(AbeekTypeConst.values());
    }

    @Test
    @DisplayName("GonhakStandardDto ")
    void setTest(){

    }


}