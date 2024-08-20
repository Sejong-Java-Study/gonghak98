package com.example.gimmegonghakauth;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import com.example.gimmegonghakauth.constant.CourseCategoryConst;
import com.example.gimmegonghakauth.dao.AbeekDao;
import com.example.gimmegonghakauth.dao.CompletedCoursesDao;
import com.example.gimmegonghakauth.dao.CoursesDao;
import com.example.gimmegonghakauth.dao.GonghakCoursesDao;
import com.example.gimmegonghakauth.dao.MajorsDao;
import com.example.gimmegonghakauth.dao.UserDao;
import com.example.gimmegonghakauth.domain.AbeekDomain;
import com.example.gimmegonghakauth.domain.AbeekDomain.AbeekDomainBuilder;
import com.example.gimmegonghakauth.domain.CompletedCoursesDomain;
import com.example.gimmegonghakauth.domain.CoursesDomain;
import com.example.gimmegonghakauth.domain.GonghakCoursesDomain;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import com.example.gimmegonghakauth.domain.UserDomain;
import com.example.gimmegonghakauth.service.recommend.MajorName;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Profile("!(prod || release)")
public class InitData {

    private final MajorsDao majorsDao;
    private final AbeekDao abeekDao;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin1}")
    private Long admin1;

    @Value("${admin2}")
    private Long admin2;

    @Value("${admin3}")
    private Long admin3;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initDatabase() {
        MajorsDomain computerMajor = MajorsDomain.builder()
            .id(1L)
            .major(MajorName.COMPUTER.getName()).build();
        MajorsDomain elecInfoMajor = MajorsDomain.builder()
            .id(2L)
            .major(MajorName.ELEC_INFO.getName()).build();
        MajorsDomain softwareMajor = MajorsDomain.builder()
            .id(3L)
            .major(MajorName.SOFTWARE.getName()).build();
        MajorsDomain dataScienceMajor = MajorsDomain.builder()
            .id(4L)
            .major(MajorName.DATA_SCIENCE.getName()).build();
        majorsDao.save(computerMajor);
        majorsDao.save(elecInfoMajor);
        majorsDao.save(softwareMajor);
        majorsDao.save(dataScienceMajor);

        //24학년도 computerMajor
        AbeekDomainBuilder abeek1 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.BSM)
            .majorsDomain(computerMajor)
            .note("this is a test note")
            .year(24)
            .minCredit(18);
        AbeekDomainBuilder abeek2 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.PROFESSIONAL_NON_MAJOR)
            .majorsDomain(computerMajor)
            .note("this is a test note")
            .year(24)
            .minCredit(14);
        AbeekDomainBuilder abeek3 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.DESIGN)
            .majorsDomain(computerMajor)
            .note("this is a test note")
            .year(24)
            .minCredit(10);
        AbeekDomainBuilder abeek4 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.MAJOR)
            .majorsDomain(computerMajor)
            .note("this is a test note")
            .year(24)
            .minCredit(45);
        AbeekDomainBuilder abeek5 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.MINIMUM_CERTI)
            .majorsDomain(computerMajor)
            .note("this is a test note")
            .year(24)
            .minCredit(77);

        abeekDao.save(abeek1.build());
        abeekDao.save(abeek2.build());
        abeekDao.save(abeek3.build());
        abeekDao.save(abeek4.build());
        abeekDao.save(abeek5.build());

        //24학년도 전정통
        AbeekDomainBuilder abeek21 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.MSC)
            .majorsDomain(elecInfoMajor)
            .note("this is a test note")
            .year(24)
            .minCredit(27);
        AbeekDomainBuilder abeek22 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.PROFESSIONAL_NON_MAJOR)
            .majorsDomain(elecInfoMajor)
            .note("this is a test note")
            .year(24)
            .minCredit(14);
        AbeekDomainBuilder abeek23 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.DESIGN)
            .majorsDomain(elecInfoMajor)
            .note("this is a test note")
            .year(24)
            .minCredit(9);
        AbeekDomainBuilder abeek24 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.MAJOR)
            .majorsDomain(elecInfoMajor)
            .note("this is a test note")
            .year(24)
            .minCredit(45);
        AbeekDomainBuilder abeek25 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.MINIMUM_CERTI)
            .majorsDomain(elecInfoMajor)
            .note("this is a test note")
            .year(24)
            .minCredit(86);

        abeekDao.save(abeek21.build());
        abeekDao.save(abeek22.build());
        abeekDao.save(abeek23.build());
        abeekDao.save(abeek24.build());
        abeekDao.save(abeek25.build());

        //24학년도 소프트웨어
        AbeekDomainBuilder abeek31 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.BSM)
            .majorsDomain(softwareMajor)
            .note("this is a test note")
            .year(24)
            .minCredit(18);
        AbeekDomainBuilder abeek32 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.PROFESSIONAL_NON_MAJOR)
            .majorsDomain(softwareMajor)
            .note("this is a test note")
            .year(24)
            .minCredit(14);
        AbeekDomainBuilder abeek33 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.DESIGN)
            .majorsDomain(softwareMajor)
            .note("this is a test note")
            .year(24)
            .minCredit(12);
        AbeekDomainBuilder abeek34 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.MAJOR)
            .majorsDomain(softwareMajor)
            .note("this is a test note")
            .year(24)
            .minCredit(45);
        AbeekDomainBuilder abeek35 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.MINIMUM_CERTI)
            .majorsDomain(softwareMajor)
            .note("this is a test note")
            .year(24)
            .minCredit(77);

        abeekDao.save(abeek31.build());
        abeekDao.save(abeek32.build());
        abeekDao.save(abeek33.build());
        abeekDao.save(abeek34.build());
        abeekDao.save(abeek35.build());

        //24학년도 데이터사이언스
        AbeekDomainBuilder abeek41 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.BSM)
            .majorsDomain(dataScienceMajor)
            .note("this is a test note")
            .year(24)
            .minCredit(15);
        AbeekDomainBuilder abeek42 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.PROFESSIONAL_NON_MAJOR)
            .majorsDomain(dataScienceMajor)
            .note("this is a test note")
            .year(24)
            .minCredit(14);
        AbeekDomainBuilder abeek43 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.DESIGN)
            .majorsDomain(dataScienceMajor)
            .note("this is a test note")
            .year(24)
            .minCredit(9);
        AbeekDomainBuilder abeek44 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.MAJOR)
            .majorsDomain(dataScienceMajor)
            .note("this is a test note")
            .year(24)
            .minCredit(45);
        AbeekDomainBuilder abeek45 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.MINIMUM_CERTI)
            .majorsDomain(dataScienceMajor)
            .note("this is a test note")
            .year(24)
            .minCredit(74);

        abeekDao.save(abeek41.build());
        abeekDao.save(abeek42.build());
        abeekDao.save(abeek43.build());
        abeekDao.save(abeek44.build());
        abeekDao.save(abeek45.build());

        //User
        UserDomain user1 = UserDomain.builder()
            .email("testEmail1@sju.ac.kr")
            .name("조태현")
            .password(passwordEncoder.encode("qwer"))
            .studentId(admin1)
            .majorsDomain(elecInfoMajor).build();
        userDao.save(user1);

        UserDomain user2 = UserDomain.builder()
            .email("testEmail2@sju.ac.kr")
            .name("이희수")
            .password(passwordEncoder.encode("qwer"))
            .studentId(admin2)
            .majorsDomain(dataScienceMajor).build();
        userDao.save(user2);

        UserDomain user3 = UserDomain.builder()
            .email("testEmail3@sju.ac.kr")
            .name("홍지섭")
            .password(passwordEncoder.encode("qwer"))
            .studentId(admin3)
            .majorsDomain(computerMajor).build();
        userDao.save(user3);

    }


}
