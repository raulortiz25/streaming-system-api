package com.Streaming.StreamingSystem.Model;

import com.Streaming.StreamingSystem.Model.Enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleEnum roleEnum;

    @OneToMany(mappedBy = "roleEntity")
    private Set<UserEntity> users = new HashSet<>();
}
