package com.api_colllab.api_collab.controller;


import com.api_colllab.api_collab.controller.dto.*;
import com.api_colllab.api_collab.persistence.entity.*;
import com.api_colllab.api_collab.persistence.repository.CareerRepository;
import com.api_colllab.api_collab.persistence.repository.StateRepository;
import com.api_colllab.api_collab.persistence.repository.UserRepository;
import com.api_colllab.api_collab.services.ForumService;
import com.api_colllab.api_collab.services.IStateForumService;
import com.api_colllab.api_collab.services.ITypeForumService;
import com.api_colllab.api_collab.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ForumController {

    @Autowired
    private CareerRepository careerRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private ForumService forumService;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ITypeForumService iTypeForumService;
    @Autowired
    private IStateForumService iStateForumService;

    @PostMapping("/add-forum")
    public ResponseEntity<?> addForum(@RequestBody ForumRequest forumRequest) throws URISyntaxException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        //Obtener data de las carreras que vamos a añadir al foro
        List<String> careers = forumRequest.careersRequest().nameCareers();

        List<CareerEntity> careersEntity = careerRepository.findCareerEntitiesByNameIn(careers);

        if(careersEntity.isEmpty()){
            throw new IllegalArgumentException("Alguna de las carreras especificadas no existen");
        }
        UserEntity user = userRepository.findUser(name);

        //Trayendo pendiente
        StateForumEntity pending = stateRepository.findByName("PENDIENTE")
                .orElseThrow(() -> new RuntimeException("Estado no econtrado"));


        ForumEntity forumEntity = ForumEntity.builder()
                .user(user)
                .title(forumRequest.title())
                .url(forumRequest.url())
                .content(forumRequest.content())
                .type_forum_id(forumRequest.typeForumEntity())
                .state_id(pending)
                .careersForum(careersEntity)
                .registerForum(LocalDate.now())
                .hourForum(LocalTime.now())
                .build();

        forumService.createdForum(forumEntity);
        return ResponseEntity.created(new URI("/api/add-forum")).build();
    }


    @GetMapping("/all-forum")
    public ResponseEntity<?> getAllForums(@RequestParam(defaultValue = "2") Long state,@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "5") int size){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        UserEntity user = userRepository.findUser(name);

        List<ForumEntity> forums = forumService.allForumsUsers();
        List<ForumEntity> favoriteForums = user.getForumsFavorites();

        List<ForumEntity> filteredForums  = new java.util.ArrayList<>(forums.stream()
                .filter(forum -> forum.getState_id().getId_state_forum().equals(state))
                .toList());

        filteredForums.sort(Comparator.comparing(ForumEntity::getHourForum));

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, filteredForums .size());
        List<ForumEntity> pagedForums = filteredForums.subList(fromIndex, toIndex);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        List<ForumsAllDTO> forumsList = pagedForums .stream()
                .map(forumResponse -> ForumsAllDTO.builder()
                        .idForum(forumResponse.getId_forum())
                        .type_forum(forumResponse.getType_forum_id().getName_type_forum())
                        .title(forumResponse.getTitle())
                        .name(forumResponse.getUser().getName())
                        .register(forumResponse.getRegisterForum())
                        .hour(forumResponse.getHourForum().format(formatter))
                        .isFavorite(favoriteForums.contains(forumResponse))
                        .build()).toList();
        return ResponseEntity.ok(forumsList);
    }

    ///SEARCH ENDPOINTS
    @GetMapping("/search-forum/{text}")
    public ResponseEntity<?> getSearchForumByText(@PathVariable String text){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        UserEntity user = userRepository.findUser(name);

        List<ForumEntity> forums = forumService.allForumsUsers();
        List<ForumEntity> favoriteForums = user.getForumsFavorites();

        forums.sort(Comparator.comparing(ForumEntity::getHourForum));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        forums = forums.stream()
                .filter(forum -> forum.getType_forum_id().getName_type_forum().equalsIgnoreCase(text) ||
                        forum.getTitle().equalsIgnoreCase(text) ||
                        forum.getUser().getName().equalsIgnoreCase(text) ||
                        forum.getRegisterForum().toString().equalsIgnoreCase(text) ||
                        forum.getHourForum().format(formatter).equalsIgnoreCase(text) ||
                        forum.getCareersForum().stream().anyMatch(career -> career.getName().equalsIgnoreCase("Ing. "+text)))
                .toList();

        List<ForumsAllDTO> forumsList = forums.stream()
                .map(forumResponse -> ForumsAllDTO.builder()
                        .idForum(forumResponse.getId_forum())
                        .type_forum(forumResponse.getType_forum_id().getName_type_forum())
                        .title(forumResponse.getTitle())
                        .name(forumResponse.getUser().getName())
                        .register(forumResponse.getRegisterForum())
                        .hour(forumResponse.getHourForum().format(formatter))
                        .isFavorite(favoriteForums.contains(forumResponse))
                        .build()).toList();
        return ResponseEntity.ok(forumsList);
    }

    @GetMapping("/search-my-forum/{text}")
    public ResponseEntity<?> getSearchMyForumByText(@PathVariable String text){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

       UserEntity user = userRepository.findUser(name);

        List<ForumEntity> forums = forumService.allForumsUsers();

        List<ForumEntity> myForumUser = forums.stream()
                .filter(forum -> forum.getUser().getId_user().equals(user.getId_user()) )
                .toList();


       List<ForumEntity> listForumsSearchAuthenticate = myForumUser.stream()
               .filter(fauth -> fauth.getType_forum_id().getName_type_forum().equalsIgnoreCase(text) ||
                       fauth.getTitle().equalsIgnoreCase(text) ||
                       fauth.getUser().getName().equalsIgnoreCase(text) ||
                       fauth.getState_id().getNameState().equalsIgnoreCase(text) ||
                       fauth.getRegisterForum().toString().equalsIgnoreCase(text) ||
                       fauth.getCareersForum().stream().anyMatch(career -> career.getName().equalsIgnoreCase("Ing. "+text)))
               .toList();

        List<MyForumsDTO> myForumList = listForumsSearchAuthenticate.stream()
                .map(forumResponse -> MyForumsDTO.builder()
                        .type_forum(forumResponse.getType_forum_id().getName_type_forum())
                        .title(forumResponse.getTitle())
                        .name(forumResponse.getUser().getName())
                        .register(forumResponse.getRegisterForum())
                        .name_state(forumResponse.getState_id().getNameState())
                        .build()).toList();
        return ResponseEntity.ok(myForumList);
    }




    @GetMapping("/my-forums")
    public ResponseEntity<?> myForumsUserAuthenticate(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "5") int size){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        UserEntity user = userRepository.findUser(name);
        List<ForumEntity> forums = forumService.allForumsUsers();


        List<ForumEntity> filteredForums = forums.stream()
                .filter(forum -> forum.getUser().getId_user().equals(user.getId_user()))
                .toList();

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, filteredForums .size());
        List<ForumEntity> pagedForums = filteredForums.subList(fromIndex, toIndex);

        List<MyForumsDTO> myForumList = pagedForums.stream()
                .map(forumResponse -> MyForumsDTO.builder()
                        .type_forum(forumResponse.getType_forum_id().getName_type_forum())
                        .title(forumResponse.getTitle())
                        .name(forumResponse.getUser().getName())
                        .register(forumResponse.getRegisterForum())
                        .name_state(forumResponse.getState_id().getNameState())
                        .build()).toList();
        return ResponseEntity.ok(myForumList);
    }




    //ADMINDS
    @GetMapping("/all-forum-pending")
    public ResponseEntity<?> getAllForumsPending(@RequestParam(defaultValue = "1") Long state){

        List<ForumEntity> forums = forumService.allForumsUsers();

        forums = forums.stream()
                .filter(forum -> forum.getState_id().getId_state_forum().equals(state))
                .toList();

        List<ForumPedingDTO> forumsList = forums.stream()
                .map(forumResponse -> ForumPedingDTO.builder()
                        .idForum(forumResponse.getId_forum())
                        .type_forum(forumResponse.getType_forum_id().getName_type_forum())
                        .title(forumResponse.getTitle())
                        .name(forumResponse.getUser().getName())
                        .url(forumResponse.getUrl())
                        .register(forumResponse.getRegisterForum())
                        .id_user(forumResponse.getUser().getId_user())
                        .build()).toList();
        return ResponseEntity.ok(forumsList);
    }

    @PutMapping("/update-state/{forumId}")
    public ResponseEntity<?> updatingStateForum(@PathVariable Long forumId, @RequestBody ForumUpdateStateRequest stateForum){

        Optional<ForumEntity> forums = forumService.findById(forumId);
        Optional<StateForumEntity> state = iStateForumService.findById(stateForum.id_state());
        //UPDATE `forum` SET `state_id_id_state_forum` = '2' WHERE `forum`.`id_forum` = 10;
        if(forums.isPresent()){
            ForumEntity forumEntity = forums.get();
            forumEntity.setState_id(state.get());
            forumService.createdForum(forumEntity);
            return ResponseEntity.ok("Estado actualizado con exito");
        }

        return ResponseEntity.notFound().build();
    }


    @GetMapping("/entry-forum-comment/{id_forum}")
    public ResponseEntity<?> getEntryForumComment(@PathVariable Long id_forum){

        List<ForumEntity> forums = forumService.allForumsUsers();

        forums = forums.stream()
                .filter(forum -> forum.getId_forum().equals(id_forum))
                .toList();

        List<ForumEntryDTO> forumsList = forums.stream()
                .map(forumResponse -> ForumEntryDTO.builder()
                        .id_forum(forumResponse.getId_forum())
                        .title(forumResponse.getTitle())
                        .content(forumResponse.getContent())
                        .url(forumResponse.getUrl())
                        .name_user(forumResponse.getUser().getName())
                        .date_register(forumResponse.getRegisterForum())
                        .build()).toList();
        return ResponseEntity.ok(forumsList);
    }






}
