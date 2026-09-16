package com.Streaming.StreamingSystem.Model;

import com.Streaming.StreamingSystem.Model.Enums.PlanEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "movies")
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column (name = "movie_plan", nullable = false)
    @Enumerated(EnumType.STRING)
   private PlanEnum moviePlan;


}
