package com.Streaming.StreamingSystem.Exception.DtoException;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime tiempo,
                            int estado,
                            String error,
                            String mensaje,
                            Object detalles,
                            String direccion) {

    public ErrorResponse(LocalDateTime tiempo, int estado, String error, String mensaje, String direccion){
        this(tiempo, estado, error, mensaje, null, direccion);
    }
}
