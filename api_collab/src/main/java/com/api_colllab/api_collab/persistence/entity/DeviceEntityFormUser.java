package com.api_colllab.api_collab.persistence.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="devices_users")
public class DeviceEntityFormUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_device;

    private String token_device;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
