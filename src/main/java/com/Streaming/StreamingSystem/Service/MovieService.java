package com.Streaming.StreamingSystem.Service;

import com.Streaming.StreamingSystem.DTO.MovieCreateRequest;
import com.Streaming.StreamingSystem.DTO.MovieResponse;
import com.Streaming.StreamingSystem.DTO.MovieUpdateRequest;

import java.util.List;

public interface MovieService{
    List<MovieResponse> getAllMovies ();

    MovieResponse findMovie(Long id);

    MovieResponse createMovie (MovieCreateRequest request);

    MovieResponse updateMovie(Long id, MovieUpdateRequest updateRequest);

    void deleteMovie (Long movieId);


}
