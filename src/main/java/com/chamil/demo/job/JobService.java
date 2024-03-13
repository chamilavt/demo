package com.chamil.demo.job;

import java.util.List;

public interface JobService {

    List<Job> findAll();

    void createJob(Job job);

    Job getJobById(long id);

    boolean deleteJob(Long id);

    boolean updateJob(Long id, Job job);

}
