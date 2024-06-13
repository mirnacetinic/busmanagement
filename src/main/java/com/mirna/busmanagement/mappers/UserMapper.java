package com.mirna.busmanagement.mappers;

import com.mirna.busmanagement.dtos.UserDto;
import com.mirna.busmanagement.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.mapstruct.Mapper;
@Mapper(componentModel="spring")
@Component
public class UserMapper {
   public static User userDtoToEntity(UserDto userDto){
        User user=new User();
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(userDto.getRole());

        return user;

    }

     public static UserDto EntitytoUserDto(User user){
          UserDto userDto=new UserDto();
          userDto.setName(user.getName());
          userDto.setSurname(user.getSurname());
          userDto.setEmail(user.getEmail());
          userDto.setUsername(user.getUsername());
          userDto.setPassword(user.getPassword());
          userDto.setRole(user.getRole());
          return userDto;

     }
}
