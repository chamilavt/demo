package com.chamil.demo.company;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface CompanyService {

    List<Company> findAll();

    boolean update(Long id, Company company);

    void create(Company company);

    boolean delete(Long id);

    Company findById(Long id);

}
