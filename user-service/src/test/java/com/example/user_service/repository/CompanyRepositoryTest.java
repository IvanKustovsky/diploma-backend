package com.example.user_service.repository;

import com.example.user_service.audit.AuditConfig;
import com.example.user_service.entity.Company;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(AuditConfig.class)
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepositoryTest;

    @BeforeAll
    static void setUp() {
        UserDetails userDetails = User.withUsername("testUser")
                .password("password").roles("USER").build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void findByCodeThatRegistered() {
        // given
        Company company = Company.builder().companyId(1L)
                .code("11223344").name("Company Name").address("Company address").build();
        companyRepositoryTest.save(company);
        String code = "11223344";

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