package com.chamil.demo.company.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chamil.demo.company.Company;
import com.chamil.demo.company.CompanyRepository;
import com.chamil.demo.company.DuplicateRegistrationNumberException;
import com.chamil.demo.company.InvalidRegistrationNumberException;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyServiceImpl companyService;

    @Test
    void shouldRejectMissingRegistrationNumber() {
        Company company = new Company();

        assertThrows(InvalidRegistrationNumberException.class,
                () -> companyService.create(company));
        verify(companyRepository, never()).save(company);
    }

    @Test
    void shouldRejectInvalidRegistrationNumberFormat() {
        Company company = new Company();
        company.setRegistrationNumber("invalid");

        assertThrows(InvalidRegistrationNumberException.class,
                () -> companyService.create(company));
        verify(companyRepository, never()).save(company);
    }

    @Test
    void shouldRejectDuplicateRegistrationNumber() {
        Company company = new Company();
        company.setRegistrationNumber("AB-123456");
        when(companyRepository.existsByRegistrationNumber("AB-123456")).thenReturn(true);

        assertThrows(DuplicateRegistrationNumberException.class,
                () -> companyService.create(company));
        verify(companyRepository, never()).save(company);
    }

    @Test
    void shouldCreateCompanyWithValidRegistrationNumber() {
        Company company = new Company();
        company.setRegistrationNumber("AB-123456");
        when(companyRepository.existsByRegistrationNumber("AB-123456")).thenReturn(false);

        assertDoesNotThrow(() -> companyService.create(company));
        verify(companyRepository).save(company);
    }
}