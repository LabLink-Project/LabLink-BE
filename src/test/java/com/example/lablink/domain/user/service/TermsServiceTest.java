package com.example.lablink.domain.user.service;

import com.example.lablink.domain.user.dto.request.SignupRequestDto;
import com.example.lablink.domain.user.entity.Terms;
import com.example.lablink.domain.user.entity.User;
import com.example.lablink.domain.user.repository.TermsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TermsServiceTest {

    @InjectMocks
    private TermsService termsService;

    @Mock
    private TermsRepository termsRepository;

    private SignupRequestDto signupRequestDto = new SignupRequestDto(
        "test01@naver.com",
        "nick",
        "password",
        "010-1111-1111",
        true,
        true,
        true,
        true,
        false
    );

    @Test
    @DisplayName("약관 저장")
    void saveTerms() {
        // given
        User user = new User();
        given(termsRepository.save(any(Terms.class))).willReturn(new Terms(signupRequestDto, user));

        // when
        Terms terms = termsService.saveTerms(signupRequestDto, user);

        //then
        assertNotNull(terms);
    }

    @Test
    @DisplayName("약관 삭제")
    void deleteTerms() {
        // given
        User user = new User();

        // when
        termsService.deleteTerms(user);

        //then
        verify(termsRepository).deleteByUser(user);
    }
}