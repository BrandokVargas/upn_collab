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
			/////////////////////////////////////////////////////

		/*
			//Estado del foro
			StateForumEntity aceptado = StateForumEntity.builder()
					.nameState("ACEPTADO")
					.build();

			StateForumEntity pendiente = StateForumEntity.builder()
					.nameState("PENDIENTE")
					.build();

			StateForumEntity negado = StateForumEntity.builder()
					.nameState("NEGADO")
					.build();

			stateRepository.saveAll(List.of(aceptado,pendiente,negado));
			///////////////////////////////////////////////////////

			//Tipos de foro
			TypeForumEntity articulo = TypeForumEntity.builder()
					.name_type_forum("ARTICULO")
					.build();
			TypeForumEntity tesis = TypeForumEntity.builder()
					.name_type_forum("TESIS")
					.build();
			typeForumRepository.saveAll(List.of(articulo,tesis));*/
			///////////////////////////////////////////////////////
			//USERS

//			UserEntity userKevin = UserEntity.builder()
//					.cod_upn("N00112233")
//					.email("N00112233@UPN.PE")
//					.password(passwordEncoder.encode("kevin123"))
//					.name("Kevin")
//					.lastname("Kiwi")
//					.isEnabled(true)
//					.dateRegister(LocalDate.now())
//					.accountNoExpired(true)
//					.accountNoLocked(true)
//					.credentialNoExpired(true)
//					.nivel_id(aprendiz)
//					.careers(List.of(sistemas,industrial))
//					.roles(Set.of(roleAdmin))
//					.build();
//
//			UserEntity userStudentDiego = UserEntity.builder()
//					.cod_upn("N00256242")
//					.email("N00256242@UPN.PE")
//					.password(passwordEncoder.encode("diego123"))
//					.name("Diego")
//					.lastname("Castañeda")
//					.isEnabled(true)
//					.dateRegister(LocalDate.now())
//					.accountNoExpired(true)
//					.accountNoLocked(true)
//					.nivel_id(perspicaz)
//					.careers(List.of(sistemas))
//					.credentialNoExpired(true)
//					.roles(Set.of(roleUserUpn))
//					.build();
//
//			userRepository.saveAll(List.of(userKevin, userStudentDiego));
			/////////////////////////////////////////////////////

/*
			//CREACION DE FOROS
			ForumEntity primerForo = ForumEntity.builder()
					.user(userKevin)
					.title("MACHINE LEARNING")
					.url("https://example.test.com")
					.content("Espero de ayude este link que acabo de publicar")
					.type_forum_id(articulo)
					.state_id(aceptado)
					.registerForum(LocalDate.now())
					.build();

			ForumEntity segundoForo = ForumEntity.builder()
					.user(userStudentDiego)
					.title("APLICATIVO WEB")
					.url("https://example.test.com")
					.content("Espero de ayude este link que acabo de publicar")
					.type_forum_id(tesis)
					.state_id(pendiente)
					.registerForum(LocalDate.now())
					.build();
			*/






		};

	}

}
