package com.example.gimmegonghakauth.controller;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import com.example.gimmegonghakauth.dao.UserDao;
import com.example.gimmegonghakauth.domain.UserDomain;
import com.example.gimmegonghakauth.dto.IncompletedCoursesDto;
import com.example.gimmegonghakauth.dto.LoginDto;
import com.example.gimmegonghakauth.service.GonghakCalculateService;
import com.example.gimmegonghakauth.service.recommend.ElecInfoMajorGonghakRecommendService;
import com.example.gimmegonghakauth.service.recommend.GonghakRecommendService;
import com.example.gimmegonghakauth.service.recommend.RecommendServiceSelectManager;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gonghak")
@Slf4j
@RequiredArgsConstructor
public class StatusController {

    private final GonghakCalculateService gonghakCalculateService;
    private final RecommendServiceSelectManager recommendServiceSelectManager;
    private final UserDao userDao;


    @GetMapping("/status")
    public String sendStudentId(Authentication authentication, Model model){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long studentId = Long.parseLong(userDetails.getUsername());

        UserDomain student = userDao.findByStudentId(studentId).get();

        /*if(student==null){
            return "/gonghak/statusInputForm";
        }*/

        log.info("studentId= {}",student.getStudentId());

        Map<AbeekTypeConst, Double> userResultRatio = gonghakCalculateService
            .getResultRatio(student).get().getUserResultRatio();

        GonghakRecommendService gonghakRecommendService = recommendServiceSelectManager.selectRecommendService(
            studentId);

        Map<AbeekTypeConst, List<IncompletedCoursesDto>> recommendCoursesByAbeekType = gonghakRecommendService
            .createRecommendCourses(student).getRecommendCoursesByAbeekType();

        model.addAttribute("userResultRatio", userResultRatio);
        model.addAttribute("recommendCoursesByAbeekType", recommendCoursesByAbeekType);
        return "/gonghak/statusForm";
    }
}
