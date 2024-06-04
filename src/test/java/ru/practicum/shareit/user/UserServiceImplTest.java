package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.heandler.exception.NotFoundValueException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;
import ru.practicum.shareit.user.service.repository.UserStorage;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {

    @Autowired
    private final UserServiceImpl userService;

    @MockBean
    private final UserStorage userStorage;
    private static UserDto user1;
    private static UserDto user2;

    @BeforeAll
    public static void setUp() {
        user1 = UserDto.builder()
                .name("test name")
                .email("test@test.ru")
                .build();
        user2 = UserDto.builder()
                .name("test name 2")
                .email("test2@test.ru")
                .build();
    }

    @Test
    void create() {
        UserDto userDto = UserDto.builder()
                .name("name")
                .email("user@email.com")
                .build();

        User user = User.builder()
                .id(1L)
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();

        when(userStorage.save(any()))
                .thenReturn(user);

        userDto = userService.creatUser(userDto);
        assertThat(userDto, is(notNullValue()));
    }

    @Test
    void update() {
        UserDto userDto = UserDto.builder()
                .name("name updated")
                .email("userUpdated@email.com")
                .build();

        User user = User.builder()
                .id(1L)
                .name("name")
                .email("user@email.com")
                .build();

        when(userStorage.findById(1L))
                .thenReturn(Optional.of(user));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        when(userStorage.save(any()))
                .thenReturn(user);

        userDto = userService.updateUser(1L, userDto);

        assertThat(userDto, is(notNullValue()));
    }

    @Test
    void throwUserNotFoundException() {
        UserDto userDto = UserDto.builder()
                .name("name updated")
                .email("userUpdated@email.com")
                .build();

        when(userStorage.findById(1L))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                NotFoundValueException.class,
                () -> userService.updateUser(1L, userDto)
        );

        Assertions.assertThrows(
                NotFoundValueException.class,
                () -> userService.getUser(1L)
        );
    }

    @Test
    void getUser() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("user@email.com")
                .build();

        when(userStorage.findById(1L))
                .thenReturn(Optional.of(user));

        UserDto userDto = userService.getUser(1L);

        assertThat(userDto, is(notNullValue()));
    }

    @Test
    void delete() {

        userService.deleteUser(1L);

        verify(userStorage, times(1)).deleteById(1L);

    }

    @Test
    void getAll() {

        List<User> users = List.of(User.builder()
                .id(1L)
                .name("name")
                .email("user@email.com")
                .build());

        when(userStorage.findAll())
                .thenReturn(users);

        List<UserDto> userDtos = userService.getUsers();

        assertThat(userDtos, is(notNullValue()));
    }
}