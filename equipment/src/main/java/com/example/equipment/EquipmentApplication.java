package com.example.equipment;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Equipment microservice REST API Documentation",
				description = "E2Rent Equipment microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Ivan Kustovsky",
						email = "vanyakustovsky@gmail.com",
						url = "https://t.me/micromolekula11_00"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://t.me/micromolekula11_00"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "E2Rent Equipment microservice REST API Documentation",
				url = "https://www.project-domain/swagger-ui/index.html"
		)
)
public class EquipmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(EquipmentApplication.class, args);
	}

}
