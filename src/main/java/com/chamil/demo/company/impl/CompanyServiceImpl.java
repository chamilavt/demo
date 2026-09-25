package com.chamil.demo.company.impl;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.chamil.demo.company.Company;
import com.chamil.demo.company.CompanyIndustry;
import com.chamil.demo.company.CompanyRepository;
import com.chamil.demo.company.CompanyService;
import com.chamil.demo.company.DuplicateRegistrationNumberException;
import com.chamil.demo.company.InvalidRegistrationNumberException;

@Service
public class CompanyServiceImpl implements CompanyService {

    private static final Pattern REGISTRATION_NUMBER_PATTERN =
            Pattern.compile("^[A-Z]{2}-\\d{6}$");

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
        validateRegistrationNumber(company.getRegistrationNumber());
        if (companyRepository.existsByRegistrationNumber(company.getRegistrationNumber())) {
            throw new DuplicateRegistrationNumberException(company.getRegistrationNumber());
        }
        companyRepository.save(company);
    }

    private void validateRegistrationNumber(String registrationNumber) {
        if (registrationNumber == null
                || !REGISTRATION_NUMBER_PATTERN.matcher(registrationNumber).matches()) {
            throw new InvalidRegistrationNumberException();
        }
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
