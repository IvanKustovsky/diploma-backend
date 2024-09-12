package com.example.user_service.repository;

import com.example.user_service.entity.Company;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepositoryTest;

    @Test
    void findByCodeThatRegistered() {
        // given
        String code = "11223344";
        Company company = Company.builder().companyId(1L)
                .code(code).name("Company Name").address("Company address").build();
        companyRepositoryTest.save(company);

        // when
        Optional<Company> optionalCompany = companyRepositoryTest.findByCode(code);

        // then
        assertTrue(optionalCompany.isPresent());
    }

    @Test
    void findByCodeThatIsNotRegistered() {
        // given
        String code = "44332211";

        // when
        Optional<Company> optionalCompany = companyRepositoryTest.findByCode(code);

        // then
        assertFalse(optionalCompany.isPresent());
    }

    @Test
    void findByCodeEqualsNull() {
        // when
        Optional<Company> optionalCompany = companyRepositoryTest.findByCode(null);

        // then
        assertFalse(optionalCompany.isPresent());
    }
}