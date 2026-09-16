package com.Streaming.StreamingSystem.DTO;

import com.Streaming.StreamingSystem.Model.Enums.PlanEnum;
import com.Streaming.StreamingSystem.Model.Enums.RoleEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MovieCreateRequest(@NotBlank(message = "El titulo de la pelicula no puede estar vacio") String title,
                                 @NotBlank(message = "la descripcion no puede estar vacia") String description,
                                 @NotNull(message = " Debes seleccionar el plan de la pelicula") PlanEnum moviePlan
                                 ) {
}
