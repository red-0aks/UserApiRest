package com.example.UserApiRest;

public enum RegexValidationEnum {
    EMAIL("^[A-Za-z0-9+_.-]+@(.+)$","El correo electrónico no cumple con el formato válido. "),
    PASSWORD("^(?=.*[A-Za-z])(?=.*\\d).{8,}$","La contraseña debe contener al menos una letra y un número, y tener una longitud mínima de 8 caracteres. ");

    private final String regex;
    private final String errorMessage;

    RegexValidationEnum(String regex, String errorMessage){
        this.regex = regex;
        this.errorMessage = errorMessage;
    }

    public String getRegex() {
        return regex;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
