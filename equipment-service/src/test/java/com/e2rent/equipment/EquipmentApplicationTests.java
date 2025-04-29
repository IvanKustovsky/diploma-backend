package com.e2rent.equipment;

import com.e2rent.equipment.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoBean(types = {JwtDecoder.class, SecurityConfig.class})
class EquipmentApplicationTests {

	@Test
	void contextLoads() {
	}

}
