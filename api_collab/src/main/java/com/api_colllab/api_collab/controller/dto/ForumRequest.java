package com.api_colllab.api_collab.controller.dto;

import com.api_colllab.api_collab.persistence.entity.*;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record ForumRequest(String title, String url, String content, TypeForumEntity typeForumEntity,
             @Valid CareersRequest careersRequest) {
}
