package com.example.lablink.domain.study.service;

import com.example.lablink.domain.study.repository.StudyRepository;
import com.example.lablink.domain.study.entity.Study;
import com.example.lablink.global.exception.GlobalErrorCode;
import com.example.lablink.global.exception.GlobalException;
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
                ()-> new GlobalException(GlobalErrorCode.STUDY_NOT_FOUND)
        );
    }


}
