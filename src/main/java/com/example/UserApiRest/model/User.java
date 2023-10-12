package com.example.UserApiRest.model;

import com.example.UserApiRest.dto.ResponseDTO;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuario")
public class User extends ResponseDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nombre;
    @Column(unique = true)
    private String correo;
    private String contrasenia;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Phone> telefonos = new ArrayList<>();

    private LocalDateTime creado;
    private LocalDateTime modificado;
    private LocalDateTime ultimoLogin;
    private boolean activo;

    public void addTelefono(List<Phone> nuevosTelefonos){
        for(Phone nuevoTelefono : nuevosTelefonos){
            boolean existeTelefono = telefonos.stream()
                    .anyMatch(telefono -> telefono.equals(nuevoTelefono));

            if(!existeTelefono){
                telefonos.add(nuevoTelefono);
                nuevoTelefono.setUser(this);
            }
        }
    }

    public void vincularTelefonos(){
        if(!telefonos.isEmpty()){
            for(Phone telefono : telefonos){
                telefono.setUser(this);
            }
        }
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

    public List<Phone> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<Phone> telefonos) {
        this.telefonos = telefonos;
    }

    public LocalDateTime getCreado() {
        return creado;
    }

    public void setCreado(LocalDateTime creado) {
        this.creado = creado;
    }

    public LocalDateTime getModificado() {
        return modificado;
    }

    public void setModificado(LocalDateTime modificado) {
        this.modificado = modificado;
    }

    public LocalDateTime getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(LocalDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
