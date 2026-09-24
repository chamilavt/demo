package com.chamil.demo.job.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.chamil.demo.company.Company;
import com.chamil.demo.company.CompanyService;
import com.chamil.demo.job.Job;
import com.chamil.demo.job.JobRepository;
import com.chamil.demo.job.JobService;

@Service
public class JobServiceImpl implements JobService {
    JobRepository jobRepository;
    private CompanyService companyService;

    public JobServiceImpl(JobRepository jobRepository, CompanyService companyService) {
        this.jobRepository = jobRepository;
        this.companyService = companyService;
    }

    @Override
    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    @Override
    public boolean createJob(Job job) {
        Company company = resolveCompany(job);
        if (company == null) {
            return false;
        }
        job.setCompany(company);
        jobRepository.save(job);
        return true;
    }

    @Override
    public Job getJobById(long id) {
        return jobRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteJob(Long id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateJob(Long id, Job updatedJob) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            Company company = updatedJob.getCompany() == null
                    ? job.getCompany()
                    : resolveCompany(updatedJob);
            if (company == null) {
                return false;
            }
            job.setDescription(updatedJob.getDescription());
            job.setLocation(updatedJob.getLocation());
            job.setMaxSalary(updatedJob.getMaxSalary());
            job.setMinSalary(updatedJob.getMinSalary());
            job.setTitle(updatedJob.getTitle());
            job.setCompany(company);
            jobRepository.save(job);
            return true;
        }
        return false;
    }

    private Company resolveCompany(Job job) {
        if (job.getCompany() == null || job.getCompany().getId() == null) {
            return null;
        }
        return companyService.findById(job.getCompany().getId());
    }

}
