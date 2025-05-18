package com.example.Library;

import com.example.Library.entity.User;
import com.example.Library.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository repository, PasswordEncoder encoder){
		return args -> {
			if(repository.findByUsername("admin").isEmpty()) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setPassword(encoder.encode("admin123"));
				admin.setEmail("admin@mail.com");
				admin.setRole("ROLE_ADMIN");
				repository.save(admin);
			}
		};
	}

}
