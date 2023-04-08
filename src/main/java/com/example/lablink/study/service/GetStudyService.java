package com.example.lablink.study.service;

import com.example.lablink.company.entity.Company;
import com.example.lablink.company.exception.CompanyErrorCode;
import com.example.lablink.company.exception.CompanyException;
import com.example.lablink.study.entity.Study;
import com.example.lablink.study.exception.StudyErrorCode;
import com.example.lablink.study.exception.StudyException;
import com.example.lablink.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetStudyService {
    private final StudyRepository studyRepository;
    // getStudy 매서드
    @Transactional(readOnly = true)
    public Study getStudy(Long studyId){
        return studyRepository.findById(studyId).orElseThrow(
                ()-> new StudyException(StudyErrorCode.STUDY_NOT_FOUND)
        );
    }


}
