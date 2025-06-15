package com.api_colllab.api_collab.controller;


import com.api_colllab.api_collab.controller.dto.TypeForumDTO;
import com.api_colllab.api_collab.services.ITypeForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TypeForumController {

    @Autowired
    private ITypeForumService iTypeForumService;

    @GetMapping("/type-forums")
    public ResponseEntity<?> getAllTypeForums(){
        List<TypeForumDTO> typeForums = iTypeForumService.getTypeForums()
                .stream()
                .map(typeForumDTO -> TypeForumDTO.builder()
                        .id_type_foro(typeForumDTO.getId_type_foro())
                        .nameTypeForum(typeForumDTO.getName_type_forum())
                        .build())
                .toList();
        return ResponseEntity.ok(typeForums);
    }




}
