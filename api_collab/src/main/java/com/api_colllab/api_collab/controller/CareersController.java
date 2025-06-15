package com.api_colllab.api_collab.controller;


import com.api_colllab.api_collab.controller.dto.CareersDTO;
import com.api_colllab.api_collab.services.ICareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CareersController {

    @Autowired
    private ICareerService iCareerService;

    @GetMapping("/careers")
    public ResponseEntity<?> findAllCareers(){
        List<CareersDTO> careersList = iCareerService.findAll()
                .stream()
                .map(carrerDto -> CareersDTO.builder()
                        .name(carrerDto.getName())
                        .build()).toList();
        return ResponseEntity.ok(careersList);
    }



}
