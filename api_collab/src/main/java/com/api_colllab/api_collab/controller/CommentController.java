package com.api_colllab.api_collab.controller;


import com.api_colllab.api_collab.controller.dto.CommentResponseDTO;
import com.api_colllab.api_collab.controller.dto.CommentUserDTO;
import com.api_colllab.api_collab.controller.dto.ForumsAllDTO;
import com.api_colllab.api_collab.controller.dto.ResponseMessageCustom;
import com.api_colllab.api_collab.persistence.entity.CommentUserForosDetails;
import com.api_colllab.api_collab.persistence.entity.ForumEntity;
import com.api_colllab.api_collab.persistence.entity.UserEntity;
import com.api_colllab.api_collab.persistence.repository.UserRepository;
import com.api_colllab.api_collab.services.ForumService;
import com.api_colllab.api_collab.services.ICommentService;
import com.api_colllab.api_collab.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ForumService forumService;
    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICommentService iCommentService;


    @PostMapping("/add-comment")
    public ResponseEntity<ResponseMessageCustom> addCommentUserForum(@RequestBody CommentUserDTO commentUserDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        UserEntity user = userRepository.findUser(name);

        ResponseMessageCustom message = iCommentService.addCommentUserForum(commentUserDTO.id_forum(),
                commentUserDTO.id_reaction(),user.getId_user());
        return ResponseEntity.ok(message);
    }

    @GetMapping("/comments/{forumId}")
    public ResponseEntity<?> getAllCommentsByForums(@PathVariable Long forumId,@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "3") int size){

        List<CommentUserForosDetails> comments = iCommentService.getAllComments(forumId);


        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, comments.size());
        List<CommentUserForosDetails> pagedComments = comments.subList(fromIndex, toIndex);

        List<CommentResponseDTO> commentsAll = pagedComments.stream()
                .map(comentsUser -> CommentResponseDTO.builder()
                        .idComment(comentsUser.getId_comment())
                        .name(comentsUser.getUser_id().getName())
                        .comment(comentsUser.getReaction_id().getDescription())
                        .registerComment(comentsUser.getDateComment())
                        .build()).toList();

        return ResponseEntity.ok(commentsAll);
    }



}
