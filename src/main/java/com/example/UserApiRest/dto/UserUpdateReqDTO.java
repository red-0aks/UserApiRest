package com.example.UserApiRest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserUpdateReqDTO {
    private UUID id;
    private String nombre;
    private String correo;
    @JsonProperty("contraseña")
    private String contrasenia;
    private List<PhoneUpdateReqDTO> telefonos = new ArrayList<>();

    private LocalDateTime ultimoLogin;

    public UserUpdateReqDTO(){

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public List<PhoneUpdateReqDTO> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<PhoneUpdateReqDTO> telefonos) {
        this.telefonos = telefonos;
    }

    public LocalDateTime getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(LocalDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }
}
