package com.api_colllab.api_collab.controller;


import com.api_colllab.api_collab.controller.dto.ForumsAllDTO;
import com.api_colllab.api_collab.controller.dto.IdForumDTO;
import com.api_colllab.api_collab.controller.dto.MyForumsDTO;
import com.api_colllab.api_collab.controller.dto.ResponseMessageCustom;
import com.api_colllab.api_collab.persistence.entity.ForumEntity;
import com.api_colllab.api_collab.persistence.entity.UserEntity;
import com.api_colllab.api_collab.persistence.repository.UserRepository;
import com.api_colllab.api_collab.services.ForumService;
import com.api_colllab.api_collab.services.IFavoriteService;
import com.api_colllab.api_collab.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FavoriteController {

    @Autowired
    private IFavoriteService iFavoriteService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ForumService forumService;
    @Autowired
    private IUserService iUserService;


    @PostMapping("/add-favorite")
    public ResponseEntity<ResponseMessageCustom> addFavorite(@RequestBody IdForumDTO foroId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        UserEntity user = userRepository.findUser(name);
        ResponseMessageCustom message = iFavoriteService.addFavorite(user.getId_user(), foroId.id_forum());
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/remove-favorite/{foroId}")
    public ResponseEntity<?> removeFavorite(@PathVariable("foroId") Long foroId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        UserEntity user = userRepository.findUser(name);
        ResponseMessageCustom message =  iFavoriteService.removeFavorite(user.getId_user(), foroId);
        return ResponseEntity.ok(message);
    }


    @GetMapping("/my-forums-favorites")
    public ResponseEntity<?> myForumsListFavorites(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        UserEntity user = userRepository.findUser(name);

        List<ForumEntity> forumFavorites = iFavoriteService.getForumFavorite(user.getId_user());

        List<ForumEntity> forums = forumService.allForumsUsers();


//        List<ForumEntity> filteredForums = forums.stream()
//                .filter(forum -> forumFavorites.stream()
//                        .anyMatch(favForum -> favForum.getId_forum().equals(forum.getId_forum())))
//                .collect(Collectors.toList());
        List<ForumEntity> filteredForums = forums.stream()
                .filter(forum -> forumFavorites.stream()
                        .anyMatch(favForum -> favForum.getId_forum().equals(forum.getId_forum())))
                .sorted((f1, f2) -> f2.getId_forum().compareTo(f1.getId_forum()))
                .toList();

        List<ForumsAllDTO> myForumList = filteredForums.stream()
                .map(forumResponse -> ForumsAllDTO.builder()
                        .idForum(forumResponse.getId_forum())
                        .type_forum(forumResponse.getType_forum_id().getName_type_forum())
                        .title(forumResponse.getTitle())
                        .name(forumResponse.getUser().getName())
                        .register(forumResponse.getRegisterForum())
                        .build()).toList();
        return ResponseEntity.ok(myForumList);
    }



}
