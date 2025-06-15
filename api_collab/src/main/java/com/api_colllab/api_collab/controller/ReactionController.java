package com.api_colllab.api_collab.controller;

import com.api_colllab.api_collab.controller.dto.ReactionResponseDTO;
import com.api_colllab.api_collab.persistence.entity.ReactionEntity;
import com.api_colllab.api_collab.services.IReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")

public class ReactionController {

    @Autowired
    private IReactionService iReactionService;



    @GetMapping("/all-reactions")
    public ResponseEntity<?> getAllReactions(){

        List<ReactionEntity> reactions = iReactionService.getAllReactionsComments();

        List<ReactionResponseDTO> reactionComment = reactions.stream()
                .map(reaction -> ReactionResponseDTO.builder()
                        .id_reaction(reaction.getId_reaction())
                        .comment(reaction.getDescription())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(reactionComment);

    }







}
