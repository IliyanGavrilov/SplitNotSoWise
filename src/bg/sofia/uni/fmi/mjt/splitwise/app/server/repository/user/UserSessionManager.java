package bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.user;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

public class UserSessionManager {
    private final Map<SocketChannel, String> activeSessions = new HashMap<>();
    private final Map<String, SocketChannel> loggedInUsers = new HashMap<>();
    private static UserSessionManager instance;

    private UserSessionManager() {
    }

    public static UserSessionManager getInstance() {
        if (instance == null) {
            instance = new UserSessionManager();
        }

        return instance;
    }

    public void login(SocketChannel socketChannel, String username, String password) throws ExpenseSplitException {
        if (isLoggedIn(username)) {
            throw new ExpenseSplitException("User is logged in from another device!");
        }

        if (isLoggedIn(socketChannel)) {
            throw new ExpenseSplitException("You're already logged in!");
        }

        if (!InMemoryUserRepository.getInstance().validateUserLoginInfo(username, password)) {
            throw new ExpenseSplitException("Invalid username or password. Please try again!");
        }

        loggedInUsers.put(username, socketChannel);
        activeSessions.put(socketChannel, username);
    }

    public void logout(SocketChannel socketChannel) throws ExpenseSplitException {
        if (!isLoggedIn(socketChannel)) {
            throw new ExpenseSplitException("You are not logged in!");
        }

        loggedInUsers.remove(activeSessions.get(socketChannel));
        activeSessions.remove(socketChannel);
    }

    public boolean isLoggedIn(SocketChannel socketChannel) {
        return activeSessions.containsKey(socketChannel);
    }

    public boolean isLoggedIn(String username) {
        return loggedInUsers.containsKey(username);
    }

    public String getLoggedUsername(SocketChannel socketChannel) throws ExpenseSplitException {
        if (!isLoggedIn(socketChannel)) {
            throw new ExpenseSplitException("Cannot run this command when not logged in!");
        }

        return activeSessions.get(socketChannel);
    }

    public User getLoggedUser(SocketChannel socketChannel) throws ExpenseSplitException {
        return InMemoryUserRepository.getInstance().getUserByUsername(getLoggedUsername(socketChannel));
    }

    public SocketChannel getSocketChannel(String username) throws ExpenseSplitException {
        if (!isLoggedIn(username)) {
            throw new ExpenseSplitException("Given user is not logged in!");
        }

        return loggedInUsers.get(username);
    }
}
