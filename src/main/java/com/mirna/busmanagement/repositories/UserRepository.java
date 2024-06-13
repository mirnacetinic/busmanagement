package com.mirna.busmanagement.repositories;

import com.mirna.busmanagement.models.Role;
import com.mirna.busmanagement.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> getUserByRoleEquals(Role role);

    User getUserByEmail(String email);

    User getUserByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
