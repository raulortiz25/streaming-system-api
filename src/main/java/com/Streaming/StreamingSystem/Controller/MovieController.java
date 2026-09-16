package com.Streaming.StreamingSystem.Controller;

import com.Streaming.StreamingSystem.DTO.*;
import com.Streaming.StreamingSystem.Model.MovieEntity;
import com.Streaming.StreamingSystem.Service.MovieServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieServiceImpl movie;

    @GetMapping
    public ResponseEntity<List<MovieResponse>> findAll(){
        return new ResponseEntity<>(movie.getAllMovies(), HttpStatus.OK);
    }

    @GetMapping("/{idMovie}")
    public  ResponseEntity<MovieResponse>findMovie(@PathVariable Long idMovie){
        return new ResponseEntity<>(movie.findMovie(idMovie), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MovieResponse>createMovie(@Valid @RequestBody MovieCreateRequest createRequest){
        return new ResponseEntity<>(movie.createMovie(createRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{idMovie}")
    public ResponseEntity<MovieResponse>upgradeMovie(@PathVariable Long idMovie, @Valid @RequestBody MovieUpdateRequest updateRequest){
        return new ResponseEntity<>(movie.updateMovie(idMovie, updateRequest), HttpStatus.OK);

    }

    @DeleteMapping("{idMovie}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long idMovie){
        movie.deleteMovie(idMovie);

        return ResponseEntity.ok("La pelicula a sido eliminada correctamente");
    }

}
