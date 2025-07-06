package com.sds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class SarrikaSolutionBackendApplication {

	public static void main(String[] args) {
		
		Dotenv dotenv = Dotenv.load();

		System.setProperty("spring.application.name", dotenv.get("SPRING_APPLICATION_NAME"));
		System.setProperty("spring.datasource.driver-class-name", dotenv.get("SPRING_DATASOURCE_DRIVER_CLASS_NAME"));
		System.setProperty("spring.datasource.url", dotenv.get("SPRING_DATASOURCE_URL"));
		System.setProperty("spring.datasource.username", dotenv.get("SPRING_DATASOURCE_USERNAME"));
		System.setProperty("spring.datasource.password", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
		System.setProperty("spring.jpa.hibernate.ddl-auto", dotenv.get("SPRING_JPA_HIBERNATE_DDL_AUTO"));
		System.setProperty("spring.jpa.show-sql", dotenv.get("SPRING_JPA_SHOW_SQL"));
		System.setProperty("spring.jpa.properties.hibernate.format_sql", dotenv.get("SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL"));

		System.setProperty("server.port", dotenv.get("SERVER_PORT"));
		System.setProperty("spring.servlet.multipart.enabled", dotenv.get("SPRING_SERVLET_MULTIPART_ENABLED"));
		System.setProperty("spring.servlet.multipart.max-file-size", dotenv.get("SPRING_SERVLET_MULTIPART_MAX_FILE_SIZE"));
		System.setProperty("spring.servlet.multipart.max-request-size", dotenv.get("SPRING_SERVLET_MULTIPART_MAX_REQUEST_SIZE"));
		System.setProperty("file.upload-dir", dotenv.get("FILE_UPLOAD_DIR"));
		
		System.setProperty("spring.mail.host", dotenv.get("SPRING_MAIL_HOST"));
		System.setProperty("spring.mail.port", dotenv.get("SPRING_MAIL_PORT"));
		System.setProperty("spring.mail.username", dotenv.get("SPRING_MAIL_USERNAME"));
		System.setProperty("spring.mail.password", dotenv.get("SPRING_MAIL_PASSWORD"));
		System.setProperty("spring.mail.properties.mail.smtp.auth", dotenv.get("SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH"));
		System.setProperty("spring.mail.properties.mail.smtp.starttls.enable", dotenv.get("SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE"));
		System.setProperty("spring.mail.protocol", dotenv.get("SPRING_MAIL_PROTOCOL"));
		

		
 


	        // Add other required properties here
		
		SpringApplication.run(SarrikaSolutionBackendApplication.class, args);
	}

}
