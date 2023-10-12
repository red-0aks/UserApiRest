package com.example.UserApiRest.controller;

import com.example.UserApiRest.dto.ResponseDTO;
import com.example.UserApiRest.dto.SuccessDTO;
import com.example.UserApiRest.dto.UserDTO;
import com.example.UserApiRest.model.User;
import com.example.UserApiRest.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    void test_createUser() {
        User user = new User();
        user.setCorreo("correo@example.com");
        user.setContrasenia("password1234");

        when(userService.isValidCorreo(user.getCorreo())).thenReturn(true);
        when(userService.isValidPassword(user.getContrasenia())).thenReturn(true);

        ResponseDTO responseDTO = new SuccessDTO("Se ha creado el usuario.");
        when(userService.createUser(user)).thenReturn(responseDTO);

        ResponseEntity<ResponseDTO> responseEntity = userController.createUser(user);
        verify(userService).isValidCorreo(user.getCorreo());
        verify(userService).isValidPassword(user.getContrasenia());
        verify(userService).createUser(user);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(responseDTO, responseEntity.getBody());
    }

    @Test
    void test_getAllUsers() {
        UserDTO user1 = new UserDTO();
        List<UserDTO> userList = new ArrayList<>();
        userList.add(user1);

        when(userService.getAllUsers()).thenReturn(userList);

        ResponseEntity<?> responseEntity = userController.getAllUsers();
        verify(userService).getAllUsers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userList, responseEntity.getBody());
    }

    @Test
    void test_findUserByNombre() {
        UUID userId = UUID.randomUUID();
        UserDTO user = new UserDTO();
        user.setNombre("nombre");

        when(userService.findUserByUUID(userId)).thenReturn(user);

        ResponseEntity<?> responseEntity = userController.findUserByUUID(userId);
        verify(userService).findUserByUUID(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user,responseEntity.getBody());
    }

    @Test
    void test_updateUser() {
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setCorreo("correo@example.com");
        user.setContrasenia("password1234");

        when(userService.isValidCorreo(user.getCorreo())).thenReturn(true);
        when(userService.isValidPassword(user.getContrasenia())).thenReturn(true);

        ResponseDTO responseDTO = new SuccessDTO("Se ha actualizado el usuario");
        when(userService.updateUser(userId, user)).thenReturn(responseDTO);

        ResponseEntity<?> responseEntity = userController.updateUser(userId, user);
        verify(userService).updateUser(userId, user);
        verify(userService).isValidCorreo(user.getCorreo());
        verify(userService).isValidPassword(user.getContrasenia());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseDTO, responseEntity.getBody());
    }

    @Test
    void test_updateUserParcially() throws Throwable {
        UUID userId = UUID.randomUUID();
        Map<String, Object> updates = Map.of("nombre", "NuevoNombre");

        ResponseDTO responseDTO = new SuccessDTO("Se ha actualizado el usuario");
        when(userService.updateUserParcially(userId, updates)).thenReturn(responseDTO);

        ResponseEntity<?> responseEntity = userController.updateUserParcially(userId, updates);
        verify(userService).updateUserParcially(userId, updates);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseDTO, responseEntity.getBody());
    }

    @Test
    void test_deleteUserByNombre() {
        UUID userId = UUID.randomUUID();

        ResponseDTO responseDTO = new SuccessDTO("Se ha eliminado el usuario");
        when(userService.deleteUser(userId)).thenReturn(responseDTO);

        ResponseEntity<?> responseEntity = userController.deleteUserById(userId);
        verify(userService).deleteUser(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseDTO, responseEntity.getBody());
    }
}