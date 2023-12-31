package com.example.UserApiRest.service;

import com.example.UserApiRest.dto.*;
import com.example.UserApiRest.helper.JwtHelper;
import com.example.UserApiRest.helper.MapperHelper;
import com.example.UserApiRest.model.Phone;
import com.example.UserApiRest.model.User;
import com.example.UserApiRest.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
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
    @Autowired
    private ModelMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Value("${app.email.regex}")
    private String emailRegex;
    @Value("${app.password.regex}")
    private String passwordRegex;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private MapperHelper mapperHelper;

    @PostConstruct
    public void validateProperty() {
        logger.info("--> La expresion regular para validar el correo es " + emailRegex);
        logger.info("--> La expresion regular para validar la contraseña es " + passwordRegex);
    }

    public ResponseDTO createUser(UserReqDTO userReq) {
        SuccessDTO response;
        User nuevoUsuario = mapperHelper.mapUserReqDTOtoUser(userReq);

        nuevoUsuario.setCreado(LocalDateTime.now());
        nuevoUsuario.setUltimoLogin(LocalDateTime.now());
        nuevoUsuario.setToken(jwtHelper.generateToken(userReq.getNombre()));
        nuevoUsuario.setActivo(true);
        nuevoUsuario.vincularTelefonos();
        User save = userRepository.save(nuevoUsuario);

        response = new SuccessDTO("Se ha creado el usuario en la base de datos.");
        response.setId(save.getId());
        response.setCreado(save.getCreado());
        response.setUltimoLogin(save.getCreado());
        response.setToken(save.getToken());
        response.setActivo(save.isActivo());

        return response;
    }

    public UserResDTO findUserByUUID(UUID userId) {
        Optional<User> user = userRepository.findById(userId);

        return user.map(value -> mapperHelper.mapUsertoDTO(value)).orElse(null);
    }

    public List<UserResDTO> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        if (allUsers != null) {
            return mapperHelper.mapListUsertoDTO(allUsers);
        } else {
            return null;
        }
    }

    public ResponseDTO updateUser(UserUpdateReqDTO userReq) {
        ResponseDTO response;
        Optional<User> find = userRepository.findById(userReq.getId());
        if (find.isPresent()) {
            User user = find.get();
            User updatedUser = mapperHelper.mapUserUpdateReqDTOtoUser(userReq, user);
            List<Phone> existingPhones = user.getTelefonos();
            List<Phone> updatedPhones = mapperHelper.mapListPhoneUpdateReqDTOtoPhone(userReq.getTelefonos(), userReq.getId());

            updateUserPhones(existingPhones, updatedPhones);
            updatedUser.setModificado(LocalDateTime.now());

            userRepository.save(updatedUser);
            response = new SuccessDTO("Se han aplicado las actualizaciones enviadas en el usuario.");
        } else {
            return null;
        }

        return response;
    }

    private void updateUserPhones(List<Phone> existingPhones, List<Phone> updatedPhones) {
        for (Phone existingPhone : existingPhones) {
            for (Phone updatedPhone : updatedPhones) {
                if (existingPhone.getId().equals(updatedPhone.getId()) && existingPhone.getUser().getId().equals(updatedPhone.getUser().getId())) {
                    existingPhone.setNumero(updatedPhone.getNumero());
                    existingPhone.setCodigoCiudad(updatedPhone.getCodigoCiudad());
                    existingPhone.setCodigoPais(updatedPhone.getCodigoPais());
                }
            }
        }
    }

    public ResponseDTO updateUserParcially(UUID userId, Map<String, Object> updates) throws Throwable {
        ResponseDTO response;
        Optional<User> find = userRepository.findById(userId);
        if (find.isPresent()) {
            User user = find.get();
            Class<?> userClass = user.getClass();
            Field[] fields = userClass.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                if (updates.containsKey(field.getName())) {
                    try {
                        Object value = field.get(user);
                        Object updatedValue = updates.get(field.getName());
                        field.set(user, updatedValue);

                    } catch (IllegalAccessException e) {
                        throw new IllegalAccessException("Error al actualizar parcialmente el usuario").initCause(e);
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

    public ResponseDTO deleteUser(UUID userId) {
        ResponseDTO response;
        Optional<User> find = userRepository.findById(userId);
        if (find.isPresent()) {
            userRepository.deleteById(userId);
            response = new SuccessDTO("Se ha eliminado el usuario de la base de datos.");
        } else {
            return null;
        }

        return response;
    }

    public Boolean isValidCorreo(String correo) {
        return correo.matches(emailRegex);
    }

    public Boolean isValidPassword(String password) {
        return password.matches(passwordRegex);
    }


}
