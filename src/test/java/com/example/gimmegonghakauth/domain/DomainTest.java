package com.example.gimmegonghakauth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import com.example.gimmegonghakauth.dao.AbeekDao;
import com.example.gimmegonghakauth.dao.GonghakCorusesDao;
import com.example.gimmegonghakauth.dao.GonghakDao;
import com.example.gimmegonghakauth.dao.GonghakRepository;
import com.example.gimmegonghakauth.dao.MajorsDao;

import com.example.gimmegonghakauth.domain.DomainTest.DomainTestConfig;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
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
@Import(DomainTestConfig.class)
public class DomainTest {

    @Autowired
    private GonghakRepository gonghakRepository;
    @Autowired
    private MajorsDao majorsDao;

    @TestConfiguration
    @RequiredArgsConstructor
    public static class DomainTestConfig {
        private final AbeekDao abeekDao;
        private final GonghakCorusesDao gonghakCorusesDao;

        @Bean
        public GonghakRepository gonghakRepository(){
            return new GonghakDao(abeekDao,gonghakCorusesDao);
        }
    }

    private final MajorsDomain majorsDomain = MajorsDomain.builder()
        .id(1L)
        .major("건설환경공학과").build();

    @Transactional
    @BeforeEach
    void beforeEachInputTestData(){

        MajorsDomain majorsDomain = MajorsDomain.builder()
            .id(1L)
            .major("건설환경공학과").build();
        majorsDao.save(majorsDomain);
    }

    @Transactional
    @Test
    void normalSaveTest(){

        //given
        AbeekDomain normalAbeekDomain = buildAbeekDomain(majorsDomain);

        //when
        AbeekDomain savedAbeekDomain = gonghakRepository.save(normalAbeekDomain);

        //then
        assertThat(savedAbeekDomain.getMajorsDomain().getMajor()).isEqualTo("건설환경공학과");
        assertThat(savedAbeekDomain.getYear()).isEqualTo(19);
        assertThat(savedAbeekDomain.getMinCredit()).isEqualTo(20);
        assertThat(savedAbeekDomain.getAbeekType()).isEqualTo(AbeekTypeConst.MAJOR);
    }

    @Test
    @DisplayName("범위 밖의 학년")
    void outOfRangeYear(){
        AbeekDomain errorAbeekDomain = buildAbeekDomain(majorsDomain);
        errorAbeekDomain.setYear(25);

        assertThatThrownBy(() -> gonghakRepository.save(errorAbeekDomain))
            .isInstanceOf(RuntimeException.class);
    }

    private AbeekDomain buildAbeekDomain(MajorsDomain majorsDomain){
        return AbeekDomain.builder()
            .id(1L)
            .majorsDomain(majorsDomain)
            .year(19)
            .abeekType(AbeekTypeConst.MAJOR)
            .minCredit(20)
            .note("이것은 테스트 abeek 도메인 입니다.").build();
    }
}
