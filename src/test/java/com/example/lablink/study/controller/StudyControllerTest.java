package com.example.lablink.study.controller;

import com.example.lablink.S3Image.dto.S3ResponseDto;
import com.example.lablink.S3Image.entity.S3Image;
import com.example.lablink.S3Image.service.S3UploaderService;
import com.example.lablink.company.entity.Company;
import com.example.lablink.company.security.CompanyDetailsImpl;
import com.example.lablink.study.dto.requestDto.StudyRequestDto;
import com.example.lablink.study.entity.CategoryEnum;
import com.example.lablink.study.entity.Study;
import com.example.lablink.study.service.MockCompanyDetailsService;
import com.example.lablink.study.service.StudySearchService;
import com.example.lablink.study.service.StudyService;
import com.example.lablink.user.entity.UserRoleEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.lablink.support.CompanyStub.Company1;
import static com.example.lablink.support.StudyStub.Study1;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@WebMvcTest({StudyController.class})
@MockBean(JpaMetamodelMappingContext.class)
class StudyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudyService studyService;

    @MockBean
    private S3UploaderService s3UploaderService;

    @MockBean
    private StudySearchService studySearchService;

    @Autowired
    ObjectMapper objectMapper;

    private static final String BASE = "/studies";

    private final StudyRequestDto requestDto = StudyRequestDto.builder()
            .title("Study Title")
            .studyInfo("Study Info")
            .studyPurpose("Study Purpose")
            .studyAction("Study Action")
            .subjectCount(10L)
            .category(CategoryEnum.ONLINE)
            .date(LocalDateTime.now())
            .address("Study Address")
            .pay(10000)
            .subjectGender("Male")
            .subjectMinAge(20)
            .subjectMaxAge(30)
            .repeatCount(5)
            .endDate(LocalDateTime.now().plusDays(30))
            .build();

    @Test
    @WithMockUser
    @DisplayName("공고 생성")
    void createStudy() throws Exception{
        // given
        Company company = Company1.of();


        // when
        mockMvc.perform(post("/study")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("게시글 작성 성공"))
                .andExpect(jsonPath("$.data").isEmpty());

        // then
    }

    @Test
    void getStudies() {
        // given

        // when

        // then
    }

    @Test
    void searchRankList() {
        // given

        // when

        // then
    }

    @Test
    void latestSearchKeyword() {
        // given

        // when

        // then
    }

    @Test
    void getDetailStudy() {
        // given

        // when

        // then
    }

    @Test
    void updateStudy() {
        // given

        // when

        // then
    }

    @Test
    void deleteStudy() {
        // given

        // when

        // then
    }
}