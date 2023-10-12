package com.example.UserApiRest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

public class PhoneUpdateReqDTO {
    private long id;
    private String numero;
    private String codigoCiudad;
    private String codigoPais;

    public PhoneUpdateReqDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
