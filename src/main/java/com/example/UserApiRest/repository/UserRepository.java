package com.example.UserApiRest.repository;

import com.example.UserApiRest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByNombre(String nombre);
    Optional<User> findByCorreo(String correo);

    void deleteByNombre(String nombre);
}
