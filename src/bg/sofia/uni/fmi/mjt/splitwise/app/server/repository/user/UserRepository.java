package bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.user;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExchangeAPIException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

public interface UserRepository {
    boolean isRegistered(String username);

    void register(String username, String password, String nickname) throws ExpenseSplitException;

    void changeCurrency(User user, String newCurrency) throws ExchangeAPIException;

    boolean validateUserLoginInfo(String username, String password);

    User getUserByUsername(String username) throws ExpenseSplitException;
}
