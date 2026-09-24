package com.chamil.demo.company;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

	List<Company> findByIndustry(CompanyIndustry industry);

}
