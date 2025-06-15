package com.api_colllab.api_collab.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="reactions")
public class ReactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_reaction;
    private String description;

    @OneToMany(targetEntity = CommentUserForosDetails.class,fetch = FetchType.LAZY,mappedBy = "reaction_id")
    private List<CommentUserForosDetails> comments;

}
