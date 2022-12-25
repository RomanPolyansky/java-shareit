package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserController {

    UserService service;

    @Autowired
    public UserController(UserService userService) {
        this.service = userService;
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable("id") long id) {
        User user = service.getEntityById(id);
        return UserMapper.mapUserToResponse(user);
    }

    @GetMapping()
    public List<UserResponse> getAllUsers() {
        return service.getAll().stream()
                .map(UserMapper::mapUserToResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public UserResponse addUser(@RequestBody @Valid AddUserRequest userDto) {
        User user = UserMapper.mapToUser(userDto);
        return UserMapper.mapUserToResponse(service.addEntity(user));
    }

    @PatchMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable("id") long id,
            @RequestBody @Valid UpdateUserRequest userDto) {
        User user = UserMapper.mapToUser(userDto);
        user.setId(id);
        return UserMapper.mapUserToResponse(service.changeEntity(user));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        service.deleteEntity(id);
    }
}