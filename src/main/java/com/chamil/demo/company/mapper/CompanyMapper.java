package com.chamil.demo.company.mapper;

import org.springframework.stereotype.Component;

import com.chamil.demo.company.Company;

@Component
public class CompanyMapper {

    public void updateEntity(Company target, Company source) {
        target.setDescription(source.getDescription());
        target.setName(source.getName());
        target.setJobs(source.getJobs());
    }
}
