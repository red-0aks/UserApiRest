package com.example.UserApiRest.controller;

import com.example.UserApiRest.RegexValidationEnum;
import com.example.UserApiRest.dto.ErrorResponseDTO;
import com.example.UserApiRest.dto.ResponseDTO;
import com.example.UserApiRest.model.User;
import com.example.UserApiRest.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ResponseDTO> createUser(@RequestBody User user){
        try {
            String mensaje = "";
            Boolean isValidCorreo = userService.isValidCorreo(user.getCorreo());
            Boolean isValidPassword = userService.isValidPassword(user.getContrasenia());
            if(isValidCorreo && isValidPassword){
                ResponseDTO response = userService.createUser(user);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                if(!isValidCorreo){
                    mensaje += RegexValidationEnum.EMAIL.getErrorMessage();
                }
                if(!isValidPassword){
                    mensaje += RegexValidationEnum.PASSWORD.getErrorMessage();
                }
                ResponseDTO errorResponse = new ErrorResponseDTO(mensaje);
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
        } catch(Exception e) {
            ResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(){
        try{
            List<User> userList = userService.getAllUser();
            if(!userList.isEmpty()){
                return new ResponseEntity<>(userList, HttpStatus.OK);
            } else {
                ResponseDTO errorResponse = new ErrorResponseDTO("No existen usuarios creados en la base de datos.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

        }catch(Exception e){
            ResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<ResponseDTO> findUserByNombre(@PathVariable String nombre){
        try {
            User user = userService.findUserByNombre(nombre);
            if(user != null){
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                ResponseDTO errorResponse = new ErrorResponseDTO("No se ha encontrado el usuario en la base de datos.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch(Exception e){
            ResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable UUID userId, @RequestBody User user){
        try {
            ResponseDTO response;
            response = userService.updateUser(userId, user);
            if(response != null){
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                ResponseDTO errorResponse = new ErrorResponseDTO("No se ha encontrado el id de usuario en la base de datos.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch(Exception e){
            ResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUserParcially(@PathVariable UUID userId, @RequestBody Map<String, Object> updates){
        try{
            ResponseDTO response;
            response = userService.updateUserParcially(userId, updates);
            if(response != null){
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                ResponseDTO errorResponse = new ErrorResponseDTO("No se ha encontrado el id de usuario en la base de datos.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Throwable e){
            ResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseDTO> deleteUserById(@PathVariable UUID userId){
        try {
            ResponseDTO response;
            response = userService.deleteUser(userId);
            if(response != null){
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response = new ErrorResponseDTO("No se ha encontrado el id de usuario en la base de datos.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch(Exception e){
            ResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
