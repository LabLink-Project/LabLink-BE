package com.example.lablink.study.service;

import com.example.lablink.company.entity.Company;
import com.example.lablink.company.security.CompanyDetailsImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static com.example.lablink.support.CompanyStub.Company1;

public class MockCompanyDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Company company = Company1.of();

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_BUSINESS"));
        return new CompanyDetailsImpl(company, email);
    }
}
