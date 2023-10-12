package com.example.UserApiRest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public abstract class ResponseDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mensaje;

    public ResponseDTO(){}

    public ResponseDTO(String mensaje){
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
