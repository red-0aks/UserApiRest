package com.example.UserApiRest.controller;

import com.example.UserApiRest.dto.*;
import com.example.UserApiRest.enums.MessagesEnum;
import com.example.UserApiRest.model.User;
import com.example.UserApiRest.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "createUser", description = "Permite crear un nuevo usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario creado en base de datos.")
    })
    public ResponseEntity<ResponseDTO> createUser(@RequestBody UserReqDTO userReq) throws DataFormatException {
        String mensaje = "";
        Boolean isValidCorreo = userService.isValidCorreo(userReq.getCorreo());
        Boolean isValidPassword = userService.isValidPassword(userReq.getContrasenia());
        if(isValidCorreo && isValidPassword){
            ResponseDTO response = userService.createUser(userReq);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            if(!isValidCorreo){
                mensaje += MessagesEnum.INVALID_EMAIL.getMessage();
            }
            if(!isValidPassword){
                mensaje += MessagesEnum.INVALID_PASSWORD.getMessage();
            }
            throw new DataFormatException(mensaje);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(){
        List<UserResDTO> userList = userService.getAllUsers();
        if(!userList.isEmpty()){
            return new ResponseEntity<>(userList, HttpStatus.OK);
        } else {
            ResponseDTO errorResponse = new ErrorResponseDTO("No existen usuarios creados en la base de datos.");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDTO> findUserByUUID(@PathVariable UUID userId){
        ResponseDTO response = userService.findUserByUUID(userId);
        if(response != null){
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ResponseDTO errorResponse = new ErrorResponseDTO("No se ha encontrado el usuario en la base de datos.");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateReqDTO userReq) throws DataFormatException {
        String mensaje = "";
        Boolean isValidCorreo = userService.isValidCorreo(userReq.getCorreo());
        Boolean isValidPassword = userService.isValidPassword(userReq.getContrasenia());
        if(isValidCorreo && isValidPassword){
            ResponseDTO response;
            response = userService.updateUser(userReq);
            if(response != null){
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                ResponseDTO errorResponse = new ErrorResponseDTO("No se ha encontrado el id de usuario en la base de datos.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } else {
            if(!isValidCorreo){
                mensaje += MessagesEnum.INVALID_EMAIL.getMessage();
            }
            if(!isValidPassword){
                mensaje += MessagesEnum.INVALID_PASSWORD.getMessage();
            }
            throw new DataFormatException(mensaje);
        }
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUserParcially(@PathVariable UUID userId, @RequestBody Map<String, Object> updates) throws Throwable {
        String mensaje = "";
        boolean isValidCorreo = true;
        boolean isValidPassword = true;
        if(updates.containsKey("correo") || updates.containsKey("contrasenia")){
            if(updates.containsKey("correo")){
                String correo = (String) updates.get("correo");
                isValidCorreo = userService.isValidCorreo(correo);
            }
            if(updates.containsKey("contrasenia")){
                String password = (String) updates.get("contrasenia");
                isValidPassword = userService.isValidPassword(password);
            }
        }
        if(isValidCorreo && isValidPassword){
            ResponseDTO response;
            response = userService.updateUserParcially(userId, updates);
            if(response != null){
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                ResponseDTO errorResponse = new ErrorResponseDTO("No se ha encontrado el id de usuario en la base de datos.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } else {
            if(!isValidCorreo){
                mensaje += MessagesEnum.INVALID_EMAIL.getMessage();
            }
            if(!isValidPassword){
                mensaje += MessagesEnum.INVALID_PASSWORD.getMessage();
            }
            throw new DataFormatException(mensaje);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseDTO> deleteUserById(@PathVariable UUID userId){
        ResponseDTO response;
        response = userService.deleteUser(userId);
        if(response != null){
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response = new ErrorResponseDTO("No se ha encontrado el id de usuario en la base de datos.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
