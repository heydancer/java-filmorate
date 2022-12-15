package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserStorageTest {

    private final UserDbStorage userDbStorage;
    private User testUser;
    private User testUser2;
    private User testUser3;

    @BeforeEach
    public void createUsers() {
        testUser = User.builder()
                .name("Test user")
                .login("tester")
                .email("testuser@yandex.ru")
                .birthday(LocalDate.of(1994, 4, 16))
                .build();

        testUser2 = User.builder()
                .name("Test user2")
                .login("tester2")
                .email("testuser2@yandex.ru")
                .birthday(LocalDate.of(1995, 3, 10))
                .build();

        testUser3 = User.builder()
                .name("Test user3")
                .login("tester3")
                .email("testuser3@yandex.ru")
                .birthday(LocalDate.of(1998, 1, 3))
                .build();
    }

    @Test
    public void shouldCreateUser() {
        User user = userDbStorage.add(testUser);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getName()).isEqualTo("Test user");
        assertThat(user.getLogin()).isEqualTo("tester");
        assertThat(user.getEmail()).isEqualTo("testuser@yandex.ru");
    }

    @Test
    public void shouldUpdateUser() {
        User user = userDbStorage.add(testUser);
        User updatedUser = userDbStorage.update(testUser2, user.getId());

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(1);
        assertThat(updatedUser.getName()).isEqualTo("Test user2");
        assertThat(updatedUser.getLogin()).isEqualTo("tester2");
        assertThat(updatedUser.getEmail()).isEqualTo("testuser2@yandex.ru");
    }

    @Test
    public void shouldRemoveUserByIdAndReturnNull() {
        User user = userDbStorage.add(testUser);

        userDbStorage.removeData(user.getId());

        assertThat(userDbStorage.getData(user.getId())).isNull();
        assertThat(userDbStorage.getAll()).hasSize(0);
    }

    @Test
    public void shouldReturnUserById() {
        User user = userDbStorage.add(testUser);

        assertThat(userDbStorage.getData(user.getId())).isNotNull();
        assertThat(userDbStorage.getData(user.getId())).isEqualTo(user);
    }

    @Test
    public void shouldReturnAllUsers() {
        userDbStorage.add(testUser);
        userDbStorage.add(testUser2);
        userDbStorage.add(testUser3);

        assertThat(userDbStorage.getAll()).isNotNull();
        assertThat(userDbStorage.getAll()).hasSize(3);
    }

    @Test
    public void shouldReturnEmptyListUsers() {
        assertThat(userDbStorage.getAll()).isEmpty();
    }

    @Test
    void shouldReturnNullWithANonExistentId() {
        assertThat(userDbStorage.getData(9999)).isNull();
    }

    @Test
    public void shouldReturnFriendList() {
        User user = userDbStorage.add(testUser);
        User user2 = userDbStorage.add(testUser2);
        User user3 = userDbStorage.add(testUser3);

        userDbStorage.addFriend(user.getId(), user2.getId());
        userDbStorage.addFriend(user.getId(), user3.getId());
        userDbStorage.addFriend(user2.getId(), user3.getId());

        assertThat(userDbStorage.getFriendList(user.getId())).hasSize(2);
        assertThat(userDbStorage.getFriendList(user2.getId())).hasSize(1);
        assertThat(userDbStorage.getFriendList(user3.getId())).hasSize(0);
    }

    @Test
    public void shouldReturnCommonFriends() {
        User user = userDbStorage.add(testUser);
        User user2 = userDbStorage.add(testUser2);
        User user3 = userDbStorage.add(testUser3);

        userDbStorage.addFriend(user.getId(), user3.getId());
        userDbStorage.addFriend(user2.getId(), user3.getId());

        Optional<User> result = userDbStorage.getCommonFriends(user.getId(), user2.getId()).stream().findFirst();

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(user3);
    }

    @Test
    public void shouldRemoveFriendAndReturnEmptyList() {
        User user = userDbStorage.add(testUser);
        User user2 = userDbStorage.add(testUser2);
        User user3 = userDbStorage.add(testUser3);

        userDbStorage.addFriend(user.getId(), user2.getId());
        userDbStorage.addFriend(user.getId(), user3.getId());
        userDbStorage.addFriend(user2.getId(), user3.getId());

        userDbStorage.removeFriend(user.getId(), user2.getId());
        userDbStorage.removeFriend(user.getId(), user3.getId());
        userDbStorage.removeFriend(user2.getId(), user3.getId());

        assertThat(userDbStorage.getFriendList(user.getId())).isEmpty();
        assertThat(userDbStorage.getFriendList(user2.getId())).isEmpty();
    }
}
