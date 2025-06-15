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
@Table(name="type_forum")
public class TypeForumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_type_foro;
    private String name_type_forum;

    @OneToMany(targetEntity = ForumEntity.class,fetch = FetchType.LAZY,mappedBy = "type_forum_id")
    private List<ForumEntity> forums;

}
