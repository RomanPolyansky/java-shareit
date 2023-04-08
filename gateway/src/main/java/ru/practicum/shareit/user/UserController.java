package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constraint.Create;
import ru.practicum.shareit.user.model.UserDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") long id) {
        log.info("GET user with id: {}", id);
        return userClient.getUser(id);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllUsers() {
        log.info("GET all users");
        return userClient.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Validated(Create.class) UserDto userDto) {
        log.info("POST add user of body: {}", userDto);
        return userClient.addUser(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(
            @PathVariable("id") long id,
            @RequestBody @Valid UserDto userDto) {
        log.info("PATCH update user with id: {} ; body of: {}", id, userDto);
        return userClient.changeUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") long id) {
        log.info("DELETE of id: {}", id);
        return userClient.deleteUser(id);
    }
}
