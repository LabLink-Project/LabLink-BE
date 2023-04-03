package com.example.hhproject0.user.service;

import com.example.hhproject0.user.dto.request.TermsRequestDto;
import com.example.hhproject0.user.entity.Terms;
import com.example.hhproject0.user.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository termsRepository;

    // 약관 저장
    public Terms saveTerms(TermsRequestDto termsRequestDto) {
        return termsRepository.save(new Terms(termsRequestDto));
    }

}
