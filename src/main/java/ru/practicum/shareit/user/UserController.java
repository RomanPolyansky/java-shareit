package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.AddUserRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.model.UserResponse;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    final UserService service;

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable("id") long id) {
        User user = service.getUserById(id);
        log.info("Received GET id {}", id);
        return UserMapper.mapUserToResponse(user);
    }

    @GetMapping()
    public List<UserResponse> getAllUsers() {
        log.info("Received GET all");
        return service.getAll().stream()
                .map(UserMapper::mapUserToResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public UserResponse addUser(@RequestBody @Valid AddUserRequest userDto) {
        User user = UserMapper.mapToUser(userDto);
        log.info("Received POST AddUserRequest of AddUserRequest {}", userDto);
        return UserMapper.mapUserToResponse(service.addUser(user));
    }

    @PatchMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable("id") long id,
            @RequestBody @Valid UpdateUserRequest userDto) {
        User user = UserMapper.mapToUser(userDto);
        user.setId(id);
        log.info("Received PATCH UpdateUserRequest of UpdateUserRequest {} at {}", userDto, id);
        return UserMapper.mapUserToResponse(service.changeUser(user));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        log.info("Received DELETE of {}", id);
        service.deleteUser(id);
    }
}