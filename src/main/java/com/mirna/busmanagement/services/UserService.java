package com.mirna.busmanagement.services;

import com.mirna.busmanagement.dtos.UserDto;
import com.mirna.busmanagement.mappers.UserMapper;
import com.mirna.busmanagement.models.Role;
import com.mirna.busmanagement.models.User;
import com.mirna.busmanagement.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> getAllUsersPaginated(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.getUserByRoleEquals(role);
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    public Optional<User> findUserByUsernameOpt(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findUserByEmailOpt(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean emailExists(String email){
        return findUserByEmailOpt(email).isPresent();
    }
    public boolean usernameExists(String email){
        return findUserByUsernameOpt(email).isPresent();
    }
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
    public void saveUser(UserDto userDto) {
        if(usernameExists(userDto.getUsername())){
            throw new RuntimeException("Username is taken!");
        } else if (emailExists(userDto.getEmail())) {
            throw new RuntimeException("An account with this email already exists!");

        }
        else{
            User user = UserMapper.userDtoToEntity(userDto);
            userRepository.save(user);
        }
    }

    public void updateUser(String userId, UserDto updatedUser) {
        try {
            User existingUser = getUserById(userId);
            User updatedUserEntity = UserMapper.userDtoToEntity(updatedUser);

            if (!Objects.equals(existingUser.getEmail(), updatedUserEntity.getEmail()) && emailExists(updatedUserEntity.getEmail())) {
                throw new RuntimeException("Email taken!!");
            } else if (!Objects.equals(existingUser.getUsername(), updatedUserEntity.getUsername()) && usernameExists(updatedUserEntity.getUsername())) {
                throw new RuntimeException("Username taken!!");}

            existingUser.setName(updatedUserEntity.getName());
            existingUser.setSurname(updatedUserEntity.getSurname());
            existingUser.setEmail(updatedUserEntity.getEmail());
            existingUser.setUsername(updatedUserEntity.getUsername());
            existingUser.setPassword(updatedUserEntity.getPassword());
            existingUser.setRole(updatedUserEntity.getRole());

            userRepository.save(existingUser);

        } catch (Exception e) {
            throw new RuntimeException("Error updating user.", e);
        }
    }
}
