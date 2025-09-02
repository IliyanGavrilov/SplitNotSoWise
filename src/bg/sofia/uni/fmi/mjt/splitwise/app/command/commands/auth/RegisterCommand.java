package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.auth;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.user.InMemoryUserRepository;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.user.UserSessionManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;
import bg.sofia.uni.fmi.mjt.splitwise.util.Encryption;

import java.util.Arrays;

public final class RegisterCommand extends Command {
    private static final int USERNAME_IDX = 0;
    private static final int PASSWORD_IDX = 1;
    private static final int NICKNAME_IDX = 2;
    private static final String NICKNAME_DELIMITER = " ";

    public RegisterCommand(String[] args) {
        super(args, CommandType.REGISTER, null);
    }

    @Override
    protected String executeCommand() throws ExpenseSplitException {
        // Only allow registration if user is not logged in
        if (UserSessionManager.getInstance().isLoggedIn(socket)) {
            throw new ExpenseSplitException("Cannot run this command when logged in!");
        }

        String nickname = String.join(NICKNAME_DELIMITER, Arrays.copyOfRange(args, NICKNAME_IDX, args.length));

        String encryptedPassword = Encryption.encrypt(args[PASSWORD_IDX], args[USERNAME_IDX]);

        InMemoryUserRepository.getInstance().register(args[USERNAME_IDX], encryptedPassword, nickname);

        return "User registered successfully";
    }
}
