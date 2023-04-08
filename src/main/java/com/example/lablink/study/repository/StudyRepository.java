package com.example.lablink.study.repository;

import com.example.lablink.company.entity.Company;
import com.example.lablink.company.repository.CompanyRepository;
import com.example.lablink.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Long> {
    List<Study> findAllByOrderByEndDateDesc();
    Optional<Study> findByIdAndCompany(Long id, Company company);
    List<Study> findAllByOrderByCreatedAtDesc();
    List<Study> findAllByOrderByPayDesc();


}
