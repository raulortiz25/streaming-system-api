package com.Streaming.StreamingSystem.Model;


import com.Streaming.StreamingSystem.Model.Enums.PlanEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;


}
