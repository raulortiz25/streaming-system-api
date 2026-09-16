package com.Streaming.StreamingSystem.DTO;

import com.Streaming.StreamingSystem.Model.Enums.PlanEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChangePlanRequest(
                                @NotNull(message = "el plan no puede estar vacio")PlanEnum changePlan) {
}
