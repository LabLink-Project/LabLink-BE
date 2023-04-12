package com.example.lablink.feedback.service;

import com.example.lablink.company.security.CompanyDetailsImpl;
import com.example.lablink.feedback.dto.Request.FeedBackRequestDto;
import com.example.lablink.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedBackService {
    @Transactional
    public void addFeedBack(UserDetailsImpl userDetails, Long studyId, FeedBackRequestDto feedBackRequestDto) {

    }
    @Transactional(readOnly = true)
    public Object getFeedBack(CompanyDetailsImpl companyDetails, Long studyId, FeedBackRequestDto feedBackRequestDto) {
        return null;
    }
    @Transactional(readOnly = true)
    public Object getDetailFeedBack(CompanyDetailsImpl companyDetails, Long studyId, Long feedbackId, FeedBackRequestDto feedBackRequestDto) {
        return null;
    }
}
