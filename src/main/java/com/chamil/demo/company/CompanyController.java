package com.chamil.demo.company;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(path = "/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<List<Company>> findAll(
            @RequestParam(required = false) CompanyIndustry industry) {
        if (industry != null) {
            return ResponseEntity.ok(companyService.findAllByIndustry(industry));
        }
        List<Company> companies = companyService.findAll();
        return ResponseEntity.ok().body(companies);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable(value = "id") Long id, @RequestBody Company company) {
        boolean updatedCompany = companyService.update(id, company);
        if (updatedCompany) {
            return ResponseEntity.ok().body("Updated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Company company) {
        if (!companyService.create(company)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Registration number already exists");
        }
        return new ResponseEntity<>("Company created sussfully", HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable(value = "id") Long id) {
        boolean deleted = companyService.delete(id);
        if (deleted) {
            return ResponseEntity.ok().body("Deleted " + id);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getById(@PathVariable Long id) {
        Company company = companyService.findById(id);
        if (company != null) {
            return ResponseEntity.ok().body(company);
        }
        return ResponseEntity.notFound().build();
    }

}
