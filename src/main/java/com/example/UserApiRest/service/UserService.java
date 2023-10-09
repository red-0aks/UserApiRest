package com.example.UserApiRest.service;

import com.example.UserApiRest.RegexValidationEnum;
import com.example.UserApiRest.dto.ResponseDTO;
import com.example.UserApiRest.dto.SuccessDTO;
import com.example.UserApiRest.model.User;
import com.example.UserApiRest.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Value("${correo.regex}")
    private String correoRegEx;
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void validateProperty(){
        logger.info("--> La expresion regular para validar el correo es "+correoRegEx);
    }

    public ResponseDTO createUser(User nuevoUsuario){
        SuccessDTO response;
        Optional<User> existCorreo = userRepository.findByCorreo(nuevoUsuario.getCorreo());
        if(existCorreo.isPresent()){
            throw new RuntimeException("El correo ya existe en la base de datos. No se puede crear el usuario");
        } else {
            nuevoUsuario.setCreado(LocalDateTime.now());
            nuevoUsuario.setUltimoLogin(LocalDateTime.now());
            nuevoUsuario.setActivo(true);
            nuevoUsuario.vincularTelefonos();
            User save = userRepository.save(nuevoUsuario);

            response = new SuccessDTO("Se ha creado el usuario en la base de datos.");
            response.setId(save.getId());
            response.setCreado(save.getCreado());
            response.setUltimoLogin(save.getCreado());
            response.setActivo(save.isActivo());
        }

        return response;
    }

    public User findUserByNombre(String nombre){
        Optional<User> user = userRepository.findByNombre(nombre);
        return user.orElse(null);
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public ResponseDTO updateUser(UUID userId, User updateUser){
        ResponseDTO response;
        Optional<User> find = userRepository.findById(userId);
        if(find.isPresent()){
            User user = find.get();
            user.setNombre(updateUser.getNombre());
            user.setCorreo(updateUser.getCorreo());
            user.setContrasenia(updateUser.getContrasenia());
            user.setTelefonos(updateUser.getTelefonos());
            user.vincularTelefonos();
            user.setModificado(LocalDateTime.now());
            if(updateUser.getUltimoLogin() != null){
                user.setUltimoLogin(updateUser.getUltimoLogin());
            }
            userRepository.save(user);
            response = new SuccessDTO("Se han aplicado las actualizaciones enviadas en el usuario.");
        } else {
            return null;
        }

        return response;
    }

    public ResponseDTO updateUserParcially(UUID userId, Map<String, Object> updates) throws Throwable {
        ResponseDTO response;
        Optional<User> find = userRepository.findById(userId);
        if(find.isPresent()){
            User user = find.get();
            Class<?> userClass = user.getClass();
            Field[] fields = userClass.getDeclaredFields();

            for(Field field : fields){
                field.setAccessible(true);
                if(updates.containsKey(field.getName())){
                    try{
                        Object value = field.get(user);
                        Object updatedValue = updates.get(field.getName());
                        field.set(user, updatedValue);

                    } catch (IllegalAccessException e){
                        throw new RuntimeException("Error al actualizar parcialmente el usuario").initCause(e);
                    }
                }
            }
            user.setModificado(LocalDateTime.now());
            userRepository.save(user);
            response = new SuccessDTO("Se han aplicado las actualizaciones enviadas en el usuario.");
        } else {
            return null;
        }

        return response;
    }

    public ResponseDTO deleteUser(UUID userId){
        ResponseDTO response;
        Optional<User> find = userRepository.findById(userId);
        if(find.isPresent()){
            userRepository.deleteById(userId);
            response = new SuccessDTO("Se ha eliminado el usuario de la base de datos.");
        } else {
            return null;
        }

        return response;
    }

    public Boolean isValidCorreo(String correo){
        return correo.matches(RegexValidationEnum.EMAIL.getRegex());
    }

    public Boolean isValidPassword(String password){
        return password.matches(RegexValidationEnum.PASSWORD.getRegex());
    }
}
