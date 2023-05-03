package com.example.lablink.domain.study.service;

import com.example.lablink.domain.study.exception.StudyErrorCode;
import com.example.lablink.domain.study.exception.StudyException;
import com.example.lablink.domain.study.repository.StudyRepository;
import com.example.lablink.domain.study.entity.Study;
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
