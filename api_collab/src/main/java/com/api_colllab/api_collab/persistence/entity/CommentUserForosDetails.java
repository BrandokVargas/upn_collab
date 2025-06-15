package com.api_colllab.api_collab.persistence.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="comments_users")
public class CommentUserForosDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_comment;

    @ManyToOne(targetEntity = UserEntity.class)
    private UserEntity user_id;

    @ManyToOne(targetEntity = ForumEntity.class)
    private ForumEntity forum_id;

    @ManyToOne(targetEntity = ReactionEntity.class)
    private ReactionEntity reaction_id;

    private LocalDate dateComment;




}
