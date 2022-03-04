package com.ndirituedwin.Repository;

import com.ndirituedwin.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

    Optional<User> findByUsernameOrEmail(String usernameoremail, String usernameoremail1);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);


    List<User> findByIdIn(List<Long> creatorIds);


    User findByIdAndAvatar(Long id, String filename);
}
