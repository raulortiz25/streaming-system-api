package com.Streaming.StreamingSystem.DTO;

import com.Streaming.StreamingSystem.Model.Enums.PlanEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MovieUpdateRequest(@NotBlank(message = "El titulo no puede estar vacio") String title,
                                 @NotBlank(message = "La descripcion no puede estar vacia") String description,
                                 @NotNull(message = " El plan de la pelicula no puede estar vacio")PlanEnum moviePlan) {
}
