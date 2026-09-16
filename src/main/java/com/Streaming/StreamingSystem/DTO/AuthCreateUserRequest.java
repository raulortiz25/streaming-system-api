package com.Streaming.StreamingSystem.DTO;

import com.Streaming.StreamingSystem.Model.Enums.PlanEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuthCreateUserRequest(@NotBlank (message = "El email no puede estar vacio") String email,
                                    @NotBlank(message = "El usuario no puede estar vacio")
                                    @Size(min = 4, message = "el usuario debe tener al menos 4 letras") String username,
                                    @NotBlank(message = "La contraseña es obligatoria")
                                    @Size(min = 4, message = "La contraseña debe tener al menos 4 letras") String password,
                                    @NotNull(message = "Debe elegir un plan") PlanEnum plan) {
}
