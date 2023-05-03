package com.example.lablink.domain.user.service;

import com.example.lablink.domain.user.repository.TermsRepository;
import com.example.lablink.domain.user.dto.request.SignupRequestDto;
import com.example.lablink.domain.user.dto.request.TermsRequestDto;
import com.example.lablink.domain.user.entity.Terms;
import com.example.lablink.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository termsRepository;

    // 약관 저장
    public Terms saveTerms(SignupRequestDto signupRequestDto, User user) {
        return termsRepository.save(new Terms(signupRequestDto, user));
    }

    // 약관 저장
    public Terms saveSocialTerms(TermsRequestDto termsRequestDto, User user) {
        return termsRepository.save(new Terms(termsRequestDto, user));
    }

    //약관 삭제
    public void deleteTerms (User user) {
        termsRepository.deleteByUser(user);
    }
}
