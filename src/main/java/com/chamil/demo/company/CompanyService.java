package com.chamil.demo.company;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface CompanyService {

    List<Company> findAll();

    List<Company> findAllByIndustry(CompanyIndustry industry);

    boolean update(Long id, Company company);

    void create(Company company);

    boolean registrationNumberExists(String registrationNumber);

    boolean delete(Long id);

    Company findById(Long id);

}
