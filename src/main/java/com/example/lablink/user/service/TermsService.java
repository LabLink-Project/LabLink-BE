package com.example.lablink.user.service;

import com.example.lablink.user.dto.request.SignupRequestDto;
import com.example.lablink.user.entity.Terms;
import com.example.lablink.user.entity.User;
import com.example.lablink.user.repository.TermsRepository;
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

    //약관 삭제
    public void deleteTerms (User user) {
        termsRepository.deleteByUser(user);
    }
}
