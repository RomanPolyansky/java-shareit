package ru.practicum.shareit.user.model;

public class UserMapper {

    public static User mapToUser(UpdateUserRequest dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }

    public static User mapToUser(AddUserRequest dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }

    public static UserResponse mapUserToResponse(User user) {
        UserResponse userDto = new UserResponse();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        return userDto;
    }
}