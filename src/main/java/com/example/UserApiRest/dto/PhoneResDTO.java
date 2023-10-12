package com.example.UserApiRest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PhoneResDTO {
    private Long id;
    private String numero;
    private String codigoCiudad;
    private String codigoPais;
    @JsonIgnore
    private UserResDTO user;

    public PhoneResDTO(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCodigoCiudad() {
        return codigoCiudad;
    }

    public void setCodigoCiudad(String codigoCiudad) {
        this.codigoCiudad = codigoCiudad;
    }

    public String getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
    }

    public UserResDTO getUser() {
        return user;
    }

    public void setUser(UserResDTO user) {
        this.user = user;
    }
}
