package com.Streaming.StreamingSystem.DTO;

import com.Streaming.StreamingSystem.Model.Enums.PlanEnum;

public record MovieResponse(Long id,
                            String title,
                            String description,
                            PlanEnum moviePlan) {
}
