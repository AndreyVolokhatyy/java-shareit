package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto userCreate(@Valid @RequestBody UserDto userDto) {
        return userService.creatUser(userDto);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto userUpdate(@PathVariable(value = "userId") int id, @Valid @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable(value = "userId") int id) {
        return userService.getUser(id);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable(value = "userId") int id) {
        userService.deleteUser(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUser() {
        return userService.getUsers();
    }
}
