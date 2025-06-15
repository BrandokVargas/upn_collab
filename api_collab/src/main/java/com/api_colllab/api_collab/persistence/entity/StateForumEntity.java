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
@Table(name="state")
public class StateForumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_state_forum;
    private String nameState;

    @OneToMany(targetEntity = ForumEntity.class,fetch = FetchType.LAZY,mappedBy = "state_id")
    private List<ForumEntity> forums;

}
