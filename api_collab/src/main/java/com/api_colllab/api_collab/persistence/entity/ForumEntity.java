package com.api_colllab.api_collab.persistence.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="forum")
public class ForumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_forum;

    @ManyToOne(targetEntity = UserEntity.class)
    private UserEntity user;

    private String title;
    private String url;
    private String content;


    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name="details_forums",joinColumns = @JoinColumn(name="id_forum"), inverseJoinColumns = @JoinColumn(name="carrera_id"))
    private List<CareerEntity> careersForum = new ArrayList<>();


    @ManyToOne(targetEntity = StateForumEntity.class)
    private StateForumEntity state_id;

    @ManyToOne(targetEntity = TypeForumEntity.class)
    private TypeForumEntity type_forum_id;



    @OneToMany(targetEntity = CommentUserForosDetails.class,fetch = FetchType.LAZY,
            cascade = { CascadeType.PERSIST, CascadeType.MERGE },mappedBy = "forum_id")
    private List<CommentUserForosDetails> comments = new ArrayList<>();
    
    private LocalDate registerForum;

    private LocalTime hourForum;

    @ManyToMany(mappedBy = "forumsFavorites")
    private List<UserEntity> users;


}
