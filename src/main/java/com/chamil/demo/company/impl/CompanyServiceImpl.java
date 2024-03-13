package com.chamil.demo.company.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.chamil.demo.company.Company;
import com.chamil.demo.company.CompanyRepository;
import com.chamil.demo.company.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

    CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public boolean update(Long id, Company companyUpdated) {
        Optional<Company> comOptional = companyRepository.findById(id);
        if (comOptional.isPresent()) {
            Company company = comOptional.get();
            company.setDescription(companyUpdated.getDescription());
            company.setName(companyUpdated.getName());
            company.setJobs(companyUpdated.getJobs());
            companyRepository.save(company);
            return true;
        }
        return false;
    }

    @Override
    public void create(Company company) {
        companyRepository.save(company);
    }

    @Override
    public boolean delete(Long id) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Company findById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

}
