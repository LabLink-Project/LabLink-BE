package com.example.lablink.study.service;

import com.example.lablink.study.entity.Study;
import com.example.lablink.study.exception.StudyErrorCode;
import com.example.lablink.study.exception.StudyException;
import com.example.lablink.study.repository.StudyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.lablink.support.StudyStub.Study1;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GetStudyServiceTest {

    @Mock
    private StudyRepository studyRepository;

    @Test
    public void testGetStudy() {
        // given
        Long studyId = 1L;
        Study study = Study1.of(studyId);
        given(studyRepository.findById(studyId)).willReturn(Optional.of(study));
        GetStudyService getStudyService = new GetStudyService(studyRepository);

        // when
        Study result = getStudyService.getStudy(studyId);

        // then
        assertEquals(study, result);
    }

    @Test
    public void testGetStudyNotFound() {
        // given
        Long studyId = 1L;
        given(studyRepository.findById(studyId)).willReturn(Optional.empty());
        GetStudyService getStudyService = new GetStudyService(studyRepository);

        // when, then
        StudyException exception = assertThrows(StudyException.class, () -> {
            getStudyService.getStudy(studyId);
        });
        assertEquals(StudyErrorCode.STUDY_NOT_FOUND, exception.getErrorCode());
    }

}