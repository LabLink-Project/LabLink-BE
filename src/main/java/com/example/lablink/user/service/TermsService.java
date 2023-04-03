package com.example.lablink.user.service;

import com.example.lablink.user.dto.request.TermsRequestDto;
import com.example.lablink.user.entity.Terms;
import com.example.lablink.user.repository.TermsRepository;
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
