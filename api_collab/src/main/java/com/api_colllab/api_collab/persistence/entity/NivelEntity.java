package com.api_colllab.api_collab.persistence.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="niveles")
public class NivelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_nivel;

    private String name;

    @OneToMany(targetEntity = UserEntity.class,fetch = FetchType.LAZY,mappedBy = "nivel_id",cascade = CascadeType.MERGE)
    private List<UserEntity> users;

}
