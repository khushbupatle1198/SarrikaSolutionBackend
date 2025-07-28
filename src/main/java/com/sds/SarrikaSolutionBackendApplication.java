package com.sds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class SarrikaSolutionBackendApplication {

	public static void main(String[] args) {

		// Load .env file from the current working directory (default is ./)
		Dotenv dotenv = Dotenv.configure()
				.directory("./")        // You can change to "/home/ubuntu/target" if needed on EC2
				.filename(".env")
				.load();

		// Load required environment variables into system properties
		setEnv(dotenv, "spring.application.name", "SPRING_APPLICATION_NAME");
		setEnv(dotenv, "spring.datasource.driver-class-name", "SPRING_DATASOURCE_DRIVER_CLASS_NAME");
		setEnv(dotenv, "spring.datasource.url", "SPRING_DATASOURCE_URL");
		setEnv(dotenv, "spring.datasource.username", "SPRING_DATASOURCE_USERNAME");
		setEnv(dotenv, "spring.datasource.password", "SPRING_DATASOURCE_PASSWORD");

		setEnv(dotenv, "spring.jpa.hibernate.ddl-auto", "SPRING_JPA_HIBERNATE_DDL_AUTO");
		setEnv(dotenv, "spring.jpa.show-sql", "SPRING_JPA_SHOW_SQL");
		setEnv(dotenv, "spring.jpa.properties.hibernate.format_sql", "SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL");

		setEnv(dotenv, "server.port", "SERVER_PORT");
		setEnv(dotenv, "spring.servlet.multipart.enabled", "SPRING_SERVLET_MULTIPART_ENABLED");
		setEnv(dotenv, "spring.servlet.multipart.max-file-size", "SPRING_SERVLET_MULTIPART_MAX_FILE_SIZE");
		setEnv(dotenv, "spring.servlet.multipart.max-request-size", "SPRING_SERVLET_MULTIPART_MAX_REQUEST_SIZE");
		setEnv(dotenv, "file.upload-dir", "FILE_UPLOAD_DIR");

		setEnv(dotenv, "spring.mail.host", "SPRING_MAIL_HOST");
		setEnv(dotenv, "spring.mail.port", "SPRING_MAIL_PORT");
		setEnv(dotenv, "spring.mail.username", "SPRING_MAIL_USERNAME");
		setEnv(dotenv, "spring.mail.password", "SPRING_MAIL_PASSWORD");
		setEnv(dotenv, "spring.mail.properties.mail.smtp.auth", "SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH");
		setEnv(dotenv, "spring.mail.properties.mail.smtp.starttls.enable", "SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE");
		setEnv(dotenv, "spring.mail.protocol", "SPRING_MAIL_PROTOCOL");

		SpringApplication.run(SarrikaSolutionBackendApplication.class, args);
	}

	/**
	 * Helper method to safely set system properties
	 */
	private static void setEnv(Dotenv dotenv, String systemKey, String envKey) {
		String value = dotenv.get(envKey);
		if (value == null) {
			throw new RuntimeException("Missing required environment variable: " + envKey);
		}
		System.setProperty(systemKey, value);
	}
}
