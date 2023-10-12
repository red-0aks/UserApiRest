package com.example.UserApiRest.enums;

import org.springframework.beans.factory.annotation.Value;

public enum MessagesEnum {
    INVALID_EMAIL("El correo electrónico no cumple con el formato válido. "),
    INVALID_PASSWORD("La contraseña debe contener al menos una letra y un número, y tener una longitud mínima de 8 caracteres. ");

    private final String message;

    @Value("${app.email.regex}")
    private String emailRegex;
    @Value("${app.password.regex}")
    private String passwordRegex;

    MessagesEnum(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
