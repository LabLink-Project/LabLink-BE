package com.example.lablink.study.repository;

import com.example.lablink.company.entity.Company;
import com.example.lablink.company.repository.CompanyRepository;
import com.example.lablink.study.entity.Study;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static com.example.lablink.support.CompanyStub.Company1;
import static com.example.lablink.support.StudyStub.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StudyRepositoryTest {
    @Autowired
    private StudyRepository studyRepository;

    // 여기서도 companyRepository가아니라 companyservice에서 가져와서 확인해야하나 ?
    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void createStudy() {
        // given
        // Company 먼저 저장하지 않으면 에러 발생
        // Study 엔티티 객체에서 Company 객체를 참조하고 있는데,
        // Company 객체가 아직 저장되지 않았을 때 해당 객체가 아직 영속화(persist)되지 않았기 때문에 예외 발생
        Company company = Company1.of();
        companyRepository.save(company);

        Study study = Study1.of(company);

        // when
        Study dbStudy = studyRepository.save(study);

        // then
        assertThat(dbStudy.getTitle()).isEqualTo("Study1 Title");
    }

    @Test
    void testFindAllByOrderByEndDateDesc() {
        // given
        Company company = Company1.of();
        companyRepository.save(company);

        List<Study> expectedList = new ArrayList<>();
        Study study1 = Study1.of(company);
        Study study2 = Study2.of(company);
        expectedList.add(study1);
        expectedList.add(study2);
        studyRepository.save(study1);
        studyRepository.save(study2);
//        given(studyRepository.findAllByOrderByEndDateDesc()).willReturn(expectedList);

        // when
        List<Study> actualList = studyRepository.findAllByOrderByEndDateDesc();

        // then
//        assertThat(actualList).isEqualTo(expectedList);
        assertThat(actualList.size()).isEqualTo(2);
        assertThat(actualList.get(0).getId()).isEqualTo(2L);
        assertThat(actualList.get(1).getId()).isEqualTo(1L);
    }

    @Test
    void findAllByOrderByCreatedAtDesc() {
    }

    @Test
    void findAllByOrderByPayDesc() {
    }

    @Test
    void findAllByOrderByCurrentApplicantCountDesc() {
    }

    @Test
    void findAllByCompany() {
    }

    @Test
    void searchStudiesNativeQuery() {
    }

    @Test
    void searchStudiesBySearchOption() {
    }
}