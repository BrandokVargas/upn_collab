package com.api_colllab.api_collab.persistence.entity;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_user;

    private String cod_upn;

    private String name;

    private String lastname;

    private String email;

    private String password;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "account_No_Expired")
    private boolean accountNoExpired;

    @Column(name = "account_No_Locked")
    private boolean accountNoLocked;

    @Column(name = "credential_No_Expired")
    private boolean credentialNoExpired;

    @ManyToOne(targetEntity = NivelEntity.class)
    private NivelEntity nivel_id;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name="user_careers",joinColumns = @JoinColumn(name="id_user"), inverseJoinColumns = @JoinColumn(name="carrera_id"))
    private List<CareerEntity> careers = new ArrayList<>();

    //muchos a muchos
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();


    @OneToMany(targetEntity = ForumEntity.class,fetch = FetchType.LAZY,mappedBy = "user")
    private List<ForumEntity> forums;


    @OneToMany(targetEntity = CommentUserForosDetails.class,fetch = FetchType.LAZY,mappedBy = "user_id",
            cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<CommentUserForosDetails> comments = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "favorites_forums_users",joinColumns = @JoinColumn(name="id_user"),
            inverseJoinColumns = @JoinColumn(name="id_forum"))
    private List<ForumEntity> forumsFavorites;

    private LocalDate dateRegister;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeviceEntityFormUser> devices;

}
