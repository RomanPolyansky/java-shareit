package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.user.UserRepositoryCustom;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByEmail(@Param("email") String emailSearch);

//    Optional<User> getUserById(long id);
//    List<User> getAll();
//    User addUser(User user);
//    User changeUser(User user);
//    void deleteUser(long id);
    
}