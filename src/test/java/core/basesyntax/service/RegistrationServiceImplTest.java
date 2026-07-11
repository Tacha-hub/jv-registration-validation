package core.basesyntax.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.db.Storage;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationServiceImplTest {
    private static final String VALID_LOGIN = "login1";
    private static final String VALID_PASSWORD = "secret";
    private static final int VALID_AGE = 18;
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        registrationService = new RegistrationServiceImpl();
        Storage.people.clear();
    }

    @Test
    void register_validUser_ok() {
        User user = createUser(VALID_LOGIN, VALID_PASSWORD, VALID_AGE);

        User actual = registrationService.register(user);

        assertSame(user, actual);
        assertEquals(1, Storage.people.size());
        assertSame(user, Storage.people.get(0));
    }

    @Test
    void register_nullUser_notOk() {
        assertThrows(RegistrationException.class,
                () -> registrationService.register(null));
    }

    @Test
    void register_nullLogin_notOk() {
        User user = createUser(null, VALID_PASSWORD, VALID_AGE);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
    }

    @Test
    void register_shortLogin_notOk() {
        String[] invalidLogins = {"", "abc", "abcde"};

        for (String login : invalidLogins) {
            User user = createUser(login, VALID_PASSWORD, VALID_AGE);
            assertThrows(RegistrationException.class,
                    () -> registrationService.register(user));
        }
    }

    @Test
    void register_minLengthLogin_ok() {
        User user = createUser("abcdef", VALID_PASSWORD, VALID_AGE);

        assertSame(user, registrationService.register(user));
        assertEquals(1, Storage.people.size());
        assertSame(user, Storage.people.get(0));
    }

    @Test
    void register_nullPassword_notOk() {
        User user = createUser(VALID_LOGIN, null, VALID_AGE);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
    }

    @Test
    void register_shortPassword_notOk() {
        String[] invalidPasswords = {"", "abc", "abcde"};

        for (String password : invalidPasswords) {
            User user = createUser(VALID_LOGIN, password, VALID_AGE);
            assertThrows(RegistrationException.class,
                    () -> registrationService.register(user));
        }
    }

    @Test
    void register_minLengthPassword_ok() {
        User user = createUser(VALID_LOGIN, "abcdef", VALID_AGE);

        assertSame(user, registrationService.register(user));
        assertEquals(1, Storage.people.size());
        assertSame(user, Storage.people.get(0));
    }

    @Test
    void register_nullAge_notOk() {
        User user = createUser(VALID_LOGIN, VALID_PASSWORD, null);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
    }

    @Test
    void register_underageUser_notOk() {
        Integer[] invalidAges = {-1, 0, 17};

        for (Integer age : invalidAges) {
            User user = createUser(VALID_LOGIN, VALID_PASSWORD, age);
            assertThrows(RegistrationException.class,
                    () -> registrationService.register(user));
        }
    }

    @Test
    void register_minAge_ok() {
        User user = createUser(VALID_LOGIN, VALID_PASSWORD, 18);

        assertSame(user, registrationService.register(user));
        assertEquals(1, Storage.people.size());
        assertSame(user, Storage.people.get(0));
    }

    @Test
    void register_ageOver18_ok() {
        User user = createUser(VALID_LOGIN, VALID_PASSWORD, 20);

        assertSame(user, registrationService.register(user));
        assertEquals(1, Storage.people.size());
        assertSame(user, Storage.people.get(0));
    }

    @Test
    void register_existingLogin_notOk() {
        User existingUser = createUser(VALID_LOGIN, VALID_PASSWORD, VALID_AGE);
        User userWithSameLogin = createUser(VALID_LOGIN, "password2", 25);
        Storage.people.add(existingUser);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(userWithSameLogin));
        assertEquals(1, Storage.people.size());
        assertSame(existingUser, Storage.people.get(0));
    }

    private User createUser(String login, String password, Integer age) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setAge(age);
        return user;
    }
}
