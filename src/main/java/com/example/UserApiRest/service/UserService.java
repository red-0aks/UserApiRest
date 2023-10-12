package com.example.UserApiRest.service;

import com.example.UserApiRest.RegexValidationEnum;
import com.example.UserApiRest.dto.PhoneDTO;
import com.example.UserApiRest.dto.ResponseDTO;
import com.example.UserApiRest.dto.SuccessDTO;
import com.example.UserApiRest.dto.UserDTO;
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

    @PostConstruct
    public void validateProperty(){
        logger.info("--> La expresion regular para validar el correo es "+emailRegex);
        logger.info("--> La expresion regular para validar la contraseña es "+passwordRegex);
    }

    public ResponseDTO createUser(User nuevoUsuario){
        SuccessDTO response;

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

        return response;
    }

    public UserDTO findUserByUUID(UUID userId){
        Optional<User> user = userRepository.findById(userId);

        return user.map(this::mapUsertoDTO).orElse(null);
    }

    public List<UserDTO> getAllUsers(){
        List<User> allUsers = userRepository.findAll();
        if(allUsers != null){
            return mapListUsertoDTO(allUsers);
        } else {
            return null;
        }
    }

    public ResponseDTO updateUser(UUID userId, User updateUser){
        ResponseDTO response;
        Optional<User> find = userRepository.findById(userId);
        if(find.isPresent()){
            User user = find.get();

            List<Phone> existingPhones = user.getTelefonos();

            user.setNombre(updateUser.getNombre());
            user.setCorreo(updateUser.getCorreo());
            user.setContrasenia(updateUser.getContrasenia());

            updateUserPhones(existingPhones, updateUser.getTelefonos());

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

    private void updateUserPhones(List<Phone> existingPhones, List<Phone> updatedPhones){
        for (Phone existingPhone : existingPhones) {
            for (Phone updatedPhone : updatedPhones) {
                if (existingPhone.getId().equals(updatedPhone.getId()) && existingPhone.getUser().equals(updatedPhone.getUser())) {
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
        return correo.matches(emailRegex);
    }

    public Boolean isValidPassword(String password){
        return password.matches(passwordRegex);
    }

    public UserDTO mapUsertoDTO(User user){
        UserDTO userDTO = new UserDTO();
        mapper.map(user, userDTO);

        return userDTO;
    }

    public List<UserDTO> mapListUsertoDTO(List<User> userList){
        List<UserDTO> userDTOList = new ArrayList<>();
        for(User user : userList){
            UserDTO dto = new UserDTO();
            mapper.map(user, dto);
            userDTOList.add(dto);
        }
        return  userDTOList;
    }
}
