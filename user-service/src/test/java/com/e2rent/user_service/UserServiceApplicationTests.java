package com.e2rent.user_service;

import com.e2rent.user_service.security.SecurityConfig;
import com.e2rent.user_service.service.impl.KeycloakUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@SpringBootTest
@MockBeans({
		@MockBean(KeycloakUserServiceImpl.class),
		@MockBean(Keycloak.class),
		@MockBean(JwtDecoder.class),
		@MockBean(SecurityConfig.class)
})
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
