package com.chamil.demo.company.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.chamil.demo.company.Company;
import com.chamil.demo.company.CompanyRepository;
import com.chamil.demo.company.CompanyService;
import com.chamil.demo.company.DuplicateRegistrationNumberException;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public List<Company> findAllByIndustry(CompanyIndustry industry) {
        return companyRepository.findByIndustry(industry);
    }

    @Override
    public boolean update(Long id, Company companyUpdated) {
        Optional<Company> comOptional = companyRepository.findById(id);
        if (comOptional.isPresent()) {
            Company company = comOptional.get();
            company.setDescription(companyUpdated.getDescription());
            company.setName(companyUpdated.getName());
            company.setIndustry(companyUpdated.getIndustry());
            companyRepository.save(company);
            return true;
        }
        return false;
    }

    @Override
    public void create(Company company) {
        if (company.getRegistrationNumber() != null
                && companyRepository.existsByRegistrationNumber(company.getRegistrationNumber())) {
            throw new DuplicateRegistrationNumberException(company.getRegistrationNumber());
        }
        companyRepository.save(company);
    }

    @Override
    public boolean registrationNumberExists(String registrationNumber) {
        return companyRepository.existsByRegistrationNumber(registrationNumber);
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
