package com.Streaming.StreamingSystem;

import com.Streaming.StreamingSystem.Model.Enums.PlanEnum;
import com.Streaming.StreamingSystem.Model.Enums.RoleEnum;
import com.Streaming.StreamingSystem.Model.RoleEntity;
import com.Streaming.StreamingSystem.Model.UserEntity;
import com.Streaming.StreamingSystem.Repository.RoleRepository;
import com.Streaming.StreamingSystem.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class StreamingSystemApplication {
	@Value("${admin.password}")
	private String adminPassword;

	public static void main(String[] args) {
		SpringApplication.run(StreamingSystemApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository,
						   RoleRepository roleRepository,
						   PasswordEncoder passwordEncoder){
		return args -> {


			RoleEntity roleAdmin = roleRepository.findByRoleEnum(RoleEnum.ADMIN)
					.orElseGet(()->roleRepository.save(RoleEntity.builder()
									.roleEnum(RoleEnum.ADMIN)
							.build()));


			RoleEntity roleCustomer = roleRepository.findByRoleEnum(RoleEnum.CUSTOMER)
					.orElseGet(()->roleRepository.save(RoleEntity.builder().roleEnum(RoleEnum.CUSTOMER).build()));


			RoleEntity rolePremium = roleRepository.findByRoleEnum(RoleEnum.PREMIUM)
					.orElseGet(()->roleRepository.save(RoleEntity.builder().roleEnum(RoleEnum.PREMIUM).build()));

			if(userRepository.findByUsername("admin").isEmpty()) {

				UserEntity userAdmin = UserEntity.builder()
						.username("admin")
						.email("adminempresa@gmail.com")
						.password(passwordEncoder.encode(adminPassword))
						.roleEntity(roleAdmin)
						.isEnabled(true)
						.build();

				userRepository.save(userAdmin);
			}
		};
	}
}
