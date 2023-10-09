package com.example.UserApiRest.dto;

public abstract class ResponseDTO {
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
