package com.api_colllab.api_collab.controller;



import com.api_colllab.api_collab.controller.dto.ProfileUserDto;
import com.api_colllab.api_collab.persistence.entity.NivelEntity;
import com.api_colllab.api_collab.persistence.entity.UserEntity;
import com.api_colllab.api_collab.persistence.repository.ForumRepository;
import com.api_colllab.api_collab.persistence.repository.NivelRepository;
import com.api_colllab.api_collab.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private NivelRepository nivelRepository;



    @GetMapping("/profile")
    public ResponseEntity<?> getProfileUserAuthenticate(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        UserEntity user = userRepository.findUser(name);

        ProfileUserDto profile = ProfileUserDto.builder()
                .name(user.getName())
                .email_upn(user.getEmail())
                .nivel_name(user.getNivel_id().getName())
                .build();
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/update-nivel")
    public ResponseEntity<?> addNivelUpdateUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        UserEntity user = userRepository.findUser(name);

        //select COUNT(id_forum) from forum where user_id_user = 3
        Integer cantForum = forumRepository.countForumsByUserId(user.getId_user());
        Nivel nuevoNivel = addNivel(cantForum);


        if (nuevoNivel != null) {

            NivelEntity levels = nivelRepository.findByName(nuevoNivel.getNombre());
            if (levels != null) {
                user.setNivel_id(levels);
                userRepository.save(user);
                return ResponseEntity.ok("Nivel actualizado correctamente.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nivel no encontrado.");
            }
    }
        return ResponseEntity.ok("El nivel no necesita actualización.");
    }


    private static Nivel addNivel(Integer cantForum) {
       if (cantForum >= 25 && cantForum < 40) {
            return Nivel.PERSPICAZ;
        } else if (cantForum >= 40 && cantForum < 65) {
            return Nivel.PROMETEDOR;
        } else if (cantForum >= 65 && cantForum < 100) {
            return Nivel.PLATINIUM;
        } else if (cantForum >= 100) {
            return Nivel.EXPERTO;
        }
        return null;
    }

    private enum Nivel {
        PERSPICAZ("INV. PERSPICAZ"),
        PROMETEDOR("INV. PROMETEDOR"),
        PLATINIUM("INV. PLATINIUM"),
        EXPERTO("INV. EXPERTO");

        private final String nombre;

        Nivel(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }
    }

}
