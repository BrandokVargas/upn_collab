package com.api_colllab.api_collab;

import com.api_colllab.api_collab.persistence.entity.*;
import com.api_colllab.api_collab.persistence.repository.*;
import com.api_colllab.api_collab.services.tokenMessagin.FirebaseAuthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class ApiCollabApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiCollabApplication.class, args);

	}


	@Autowired
	private PasswordEncoder passwordEncoder;
	@Bean
	CommandLineRunner init(UserRepository userRepository, NivelRepository nivelRepository,
						   CareerRepository careerRepository, StateRepository stateRepository,
						   TypeForumRepository typeForumRepository, ForumRepository forumRepository,
						   RoleRepository roleRepository,PermissionRepository permissionRepository){
		return args -> {

//			///NIVELES
			NivelEntity aprendiz = NivelEntity.builder()
					.name("INV. APRENDIZ")
					.build();

			NivelEntity perspicaz = NivelEntity.builder()
					.name("INV. PERSPICAZ")
					.build();

			NivelEntity prometedor = NivelEntity.builder()
					.name("INV. PROMETEDOR")
					.build();

			NivelEntity platinium = NivelEntity.builder()
					.name("INV. PLATINIUM")
					.build();

			NivelEntity experto = NivelEntity.builder()
					.name("INV. EXPERTO")
					.build();

			//nivelRepository.saveAll(List.of(aprendiz,perspicaz,prometedor,platinium,experto));
			//////////////////////////////////////////////////////////////


//			//Carreras Ingenieria
			CareerEntity sistemas = CareerEntity.builder()
					.name("ING. SISTEMAS")
					.build();

			CareerEntity industrial = CareerEntity.builder()
					.name("ING. INDUSTRIAL")
					.build();

			CareerEntity civil = CareerEntity.builder()
					.name("ING. CIVIL")
					.build();
			CareerEntity minas = CareerEntity.builder()
					.name("ING. MINAS")
					.build();
			CareerEntity electronica = CareerEntity.builder()
					.name("ING. ELECTRONICA")
					.build();
//
			//careerRepository.saveAll(List.of(sistemas,industrial,civil,minas,electronica));
//
//
			PermissionEntity addModCollab = PermissionEntity.builder()
					.name("ADD_MOD")
					.build();

			PermissionEntity readPermission = PermissionEntity.builder()
					.name("READ")
					.build();
			//permissionRepository.saveAll(List.of(addModCollab,readPermission));

			RoleEntity roleAdmin = RoleEntity.builder()
					.roleEnum(RoleEnum.ADMIN)
					.permissionList(Set.of(addModCollab))
					.build();
			RoleEntity roleUserUpn = RoleEntity.builder()
					.roleEnum(RoleEnum.USER)
					.permissionList(Set.of(readPermission))
					.build();
			//roleRepository.saveAll(List.of(roleAdmin,roleUserUpn));

		};

	}

}
