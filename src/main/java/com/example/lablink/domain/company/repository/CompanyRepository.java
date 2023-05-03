package com.example.lablink.domain.company.repository;

import com.example.lablink.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCompanyName(String companyName);

}
