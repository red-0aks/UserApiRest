package com.example.UserApiRest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserReqDTO {
    private String nombre;
    private String correo;
    @JsonProperty("contraseña")
    private String contrasenia;
    private List<PhoneReqDTO> telefonos = new ArrayList<>();

    public UserReqDTO(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public List<PhoneReqDTO> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<PhoneReqDTO> telefonos) {
        this.telefonos = telefonos;
    }
}
