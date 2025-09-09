package bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.user;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositorySaver;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExchangeAPIException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.util.Map;

public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> usersByUsername;
    private static InMemoryUserRepository instance;

    private static final String NOT_REGISTERED = "User \"%s\" is not registered!";

    private InMemoryUserRepository() {
        usersByUsername = RepositorySaver.loadUsers();
    }

    public static InMemoryUserRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryUserRepository();
        }

        return instance;
    }

    public boolean isRegistered(String username) {
        return usersByUsername.containsKey(username);
    }

    @Override
    public void register(String username, String password, String nickname) throws ExpenseSplitException {
        if (isRegistered(username)) {
            throw new ExpenseSplitException("Username is already in use!");
        }

        usersByUsername.put(username, new User(username, password, nickname));
        RepositorySaver.saveUsers(usersByUsername);
    }

    @Override
    public void changeCurrency(User user, String newCurrency) throws ExchangeAPIException {
        user.setCurrency(newCurrency);
        RepositorySaver.saveUsers(usersByUsername);
    }

    @Override
    public boolean validateUserLoginInfo(String username, String password) {
        User user = usersByUsername.get(username);

        return user != null && user.authenticate(password);
    }

    @Override
    public User getUserByUsername(String username) throws ExpenseSplitException {
        if (!isRegistered(username)) {
            throw new ExpenseSplitException(String.format(NOT_REGISTERED, username));
        }

        return usersByUsername.get(username);
    }
}
