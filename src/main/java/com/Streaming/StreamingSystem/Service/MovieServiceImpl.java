package com.Streaming.StreamingSystem.Service;

import com.Streaming.StreamingSystem.DTO.MovieCreateRequest;
import com.Streaming.StreamingSystem.DTO.MovieResponse;
import com.Streaming.StreamingSystem.DTO.MovieUpdateRequest;
import com.Streaming.StreamingSystem.Exception.Custom.ResourceAlreadyExistsException;
import com.Streaming.StreamingSystem.Exception.Custom.ResourceNotFoundException;
import com.Streaming.StreamingSystem.Exception.Custom.UnauthorizedAccessException;
import com.Streaming.StreamingSystem.Model.Enums.PlanEnum;
import com.Streaming.StreamingSystem.Model.MovieEntity;
import com.Streaming.StreamingSystem.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService{

    @Autowired
    private MovieRepository movieRepo;

    @Override
    @Transactional(readOnly = true)
    public List<MovieResponse> getAllMovies() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<PlanEnum> allowedLevels = determineAllowedAccesLevels(authentication);

        List<MovieEntity> movies = movieRepo.findByMoviePlanIn(allowedLevels);

        return movies.stream()
                .map(this::mapToResponse)
                .toList();

    }

    private List<PlanEnum> determineAllowedAccesLevels(Authentication authentication){

        boolean isPremiumOrdAdmin = authentication.getAuthorities().stream()
                .anyMatch(a-> a.getAuthority().equals("ROLE_PREMIUM") ||
                        a.getAuthority().equals("ROLE_ADMIN"));

        if(isPremiumOrdAdmin){
            return List.of(PlanEnum.PREMIUM, PlanEnum.CUSTOMER);
        }

        return List.of(PlanEnum.CUSTOMER);
    }

    private MovieResponse mapToResponse(MovieEntity movieEntity){
        return new MovieResponse(
                movieEntity.getId(),
                movieEntity.getTitle(),
                movieEntity.getDescription(),
                movieEntity.getMoviePlan()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public MovieResponse findMovie(Long id) {

        MovieEntity movie = movieRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("La película con id: " + id + " no existe"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();


        boolean isPremiumContent =
                movie.getMoviePlan() == PlanEnum.PREMIUM;

        boolean isUserPremiumOrAdmin = authentication.getAuthorities().stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN") ||
                                a.getAuthority().equals("ROLE_PREMIUM")
                );

        if (isPremiumContent && !isUserPremiumOrAdmin) {
            throw new UnauthorizedAccessException(
                    "Para ver esa película cambie la suscripción a premium"
            );
        }

        return mapToResponse(movie);
    }

    @Override
    public MovieResponse createMovie(MovieCreateRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        movieRepo.findByTitle(request.title()).ifPresent(movieName->{
            throw new ResourceAlreadyExistsException("la pelicula con nombre: " + request.title() + " ya existe");
        });

        MovieEntity movieCreated = MovieEntity.builder()
                .title(request.title())
                .description(request.description())
                .moviePlan(request.moviePlan())
                .build();

        MovieEntity movieSave = movieRepo.save(movieCreated);

        return new MovieResponse(movieSave.getId(), movieSave.getTitle(), movieSave.getDescription(), movieSave.getMoviePlan());
    }

    @Override
    public MovieResponse updateMovie(Long id, MovieUpdateRequest updateRequest) {
        MovieEntity movieUpdate = movieRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("La pelicula con id: " +  id + " no existe"));

        movieUpdate.setTitle(updateRequest.title());
        movieUpdate.setDescription(updateRequest.description());
        movieUpdate.setMoviePlan(updateRequest.moviePlan());

        MovieEntity saveMovieUpdate = movieRepo.save(movieUpdate);

        return new MovieResponse(saveMovieUpdate.getId(), saveMovieUpdate.getTitle(), saveMovieUpdate.getDescription(), saveMovieUpdate.getMoviePlan());
    }


    @Override
    public void deleteMovie(Long movieId) {

        MovieEntity movieDelete = movieRepo.findById(movieId)
                .orElseThrow(()-> new ResourceNotFoundException(" la pelicula con id: " + movieId + " no existe"));

        movieRepo.delete(movieDelete);

    }
}
