package com.Streaming.StreamingSystem.Repository;

import com.Streaming.StreamingSystem.Model.Enums.PlanEnum;
import com.Streaming.StreamingSystem.Model.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long > {

    List<MovieEntity> findByMoviePlanIn(List<PlanEnum> moviePlan);

    Optional<MovieEntity>findByTitle(String title);
}
