package bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.user;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

public interface UserRepository {
    void register(String username, String password, String nickname) throws ExpenseSplitException;

    boolean validateUserLoginInfo(String username, String password);

    User getUserByUsername(String username) throws ExpenseSplitException;
}
