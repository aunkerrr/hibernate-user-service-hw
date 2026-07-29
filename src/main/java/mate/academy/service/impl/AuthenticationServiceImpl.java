package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.User;
import mate.academy.service.AuthenticationService;
import mate.academy.service.UserService;
import mate.academy.util.HashUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private UserService userService;

    @Override
    public User login(String email, String password) throws AuthenticationException {
        Optional<User> userFromDb = userService.findByEmail(email);

        if (userFromDb.isEmpty()) {
            throw new AuthenticationException("Incorrect email or password");
        }

        User user = userFromDb.get();

        String hashedPassword = HashUtil.hashPassword(password, user.getSalt());

        if (user.getPassword().equals(hashedPassword)) {
            return user;
        }

        throw new AuthenticationException("Incorrect email or password");
    }

    @Override
    public User register(String email, String password) throws RegistrationException {

        if (userService.findByEmail(email).isPresent()) {
            throw new RegistrationException("Can't registrate user with this "
                    + email + " is already present.");
        }

        byte[] salt = HashUtil.getSalt();
        String hashedPassword = HashUtil.hashPassword(password, salt);

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setSalt(salt);

        return userService.add(user);
    }
}
