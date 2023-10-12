package com.example.UserApiRest.helper;

import com.example.UserApiRest.dto.*;
import com.example.UserApiRest.model.Phone;
import com.example.UserApiRest.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class MapperHelper {
    @Autowired
    private ModelMapper mapper;

    public UserResDTO mapUsertoDTO(User user) {
        UserResDTO userResDTO = new UserResDTO();
        mapper.map(user, userResDTO);

        return userResDTO;
    }

    public List<UserResDTO> mapListUsertoDTO(List<User> userList) {
        List<UserResDTO> userResDTOList = new ArrayList<>();
        for (User user : userList) {
            UserResDTO dto = mapUsertoDTO(user);
            userResDTOList.add(dto);
        }
        return userResDTOList;
    }

    public User mapUserReqDTOtoUser(UserReqDTO userReqDTO) {
        User user = new User();
        mapper.map(userReqDTO, user);
        user.setTelefonos(mapListPhoneReqDTOtoPhone(userReqDTO.getTelefonos()));

        return user;
    }

    public Phone mapPhoneReqDTOtoPhone(PhoneReqDTO phoneReqDTO){
        Phone phone = new Phone();
        mapper.map(phoneReqDTO,phone);

        return phone;
    }

    public Phone mapPhoneUpdateReqDTOtoPhone(PhoneUpdateReqDTO phoneUpdateReqDTO, UUID userId){
        Phone phone = new Phone();
        User user = new User();
        user.setId(userId);
        mapper.map(phoneUpdateReqDTO,phone);
        phone.setUser(user);

        return phone;
    }

    public List<Phone> mapListPhoneUpdateReqDTOtoPhone(List<PhoneUpdateReqDTO> phoneUpdateReqDTOList, UUID userId){
        List<Phone> phoneList = new ArrayList<>();
        for(PhoneUpdateReqDTO phoneUpdateReqDTO : phoneUpdateReqDTOList){
            Phone phone = mapPhoneUpdateReqDTOtoPhone(phoneUpdateReqDTO, userId);
            phoneList.add(phone);
        }
        return phoneList;
    }

    public List<Phone> mapListPhoneReqDTOtoPhone(List<PhoneReqDTO> phoneReqDTOList){
        List<Phone> phoneList = new ArrayList<>();
        for(PhoneReqDTO phoneReqDTO : phoneReqDTOList){
            Phone phone = mapPhoneReqDTOtoPhone(phoneReqDTO);
            phoneList.add(phone);
        }
        return phoneList;
    }

    public User mapUserUpdateReqDTOtoUser(UserUpdateReqDTO userUpdateReqDTO, User user) {
        user.setNombre(userUpdateReqDTO.getNombre());
        user.setCorreo(userUpdateReqDTO.getCorreo());
        user.setContrasenia(userUpdateReqDTO.getContrasenia());
        if(userUpdateReqDTO.getUltimoLogin() != null){
            user.setUltimoLogin(userUpdateReqDTO.getUltimoLogin());
        }
        user.addTelefonos(mapListPhoneUpdateReqDTOtoPhone(userUpdateReqDTO.getTelefonos(), userUpdateReqDTO.getId()));

        return user;
    }
}
