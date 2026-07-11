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
    void registerValidUser_Ok() {
        User user = createUser(VALID_LOGIN, VALID_PASSWORD, VALID_AGE);

        User actual = registrationService.register(user);

        assertSame(user, actual);
        assertEquals(1, Storage.people.size());
        assertSame(user, Storage.people.get(0));
    }

    @Test
    void registerNullUser_notOk() {
        assertThrows(RegistrationException.class,
                () -> registrationService.register(null));
    }

    @Test
    void registerNullLogin_notOk() {
        User user = createUser(null, VALID_PASSWORD, VALID_AGE);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
    }

    @Test
    void registerShortLogin_notOk() {
        String[] invalidLogins = {"", "abc", "abcde"};

        for (String login : invalidLogins) {
            User user = createUser(login, VALID_PASSWORD, VALID_AGE);
            assertThrows(RegistrationException.class,
                    () -> registrationService.register(user));
        }
    }

    @Test
    void registerMinLengthLogin_Ok() {
        User user = createUser("abcdef", VALID_PASSWORD, VALID_AGE);

        assertSame(user, registrationService.register(user));
    }

    @Test
    void registerNullPassword_notOk() {
        User user = createUser(VALID_LOGIN, null, VALID_AGE);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
    }

    @Test
    void registerShortPassword_notOk() {
        String[] invalidPasswords = {"", "abc", "abcde"};

        for (String password : invalidPasswords) {
            User user = createUser(VALID_LOGIN, password, VALID_AGE);
            assertThrows(RegistrationException.class,
                    () -> registrationService.register(user));
        }
    }

    @Test
    void registerMinLengthPassword_Ok() {
        User user = createUser(VALID_LOGIN, "abcdef", VALID_AGE);

        assertSame(user, registrationService.register(user));
    }

    @Test
    void registerNullAge_notOk() {
        User user = createUser(VALID_LOGIN, VALID_PASSWORD, null);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
    }

    @Test
    void registerUnderageUser_notOk() {
        Integer[] invalidAges = {-1, 0, 17};

        for (Integer age : invalidAges) {
            User user = createUser(VALID_LOGIN, VALID_PASSWORD, age);
            assertThrows(RegistrationException.class,
                    () -> registrationService.register(user));
        }
    }

    @Test
    void registerMinAge_Ok() {
        User user = createUser(VALID_LOGIN, VALID_PASSWORD, 18);

        assertSame(user, registrationService.register(user));
    }

    private User createUser(String login, String password, Integer age) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setAge(age);
        return user;
    }
}
