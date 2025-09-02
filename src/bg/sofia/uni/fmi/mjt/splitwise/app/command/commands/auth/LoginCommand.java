package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.auth;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.notification.InMemoryNotificationRepository;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.user.UserSessionManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;
import bg.sofia.uni.fmi.mjt.splitwise.util.Encryption;

import java.nio.channels.SocketChannel;

public final class LoginCommand extends Command {
    private static final int USERNAME_IDX = 0;
    private static final int PASSWORD_IDX = 1;

    private static final String SUCCESS_MESSAGE = "User successfully logged in!\n%s";

    public LoginCommand(String[] args, SocketChannel socket) {
        super(args, CommandType.LOGIN, socket);
    }

    @Override
    protected String executeCommand() throws ExpenseSplitException {
        String encryptedPassword = Encryption.encrypt(args[PASSWORD_IDX], args[USERNAME_IDX]);
        UserSessionManager.getInstance().login(socket, args[USERNAME_IDX], encryptedPassword);

        return String.format(SUCCESS_MESSAGE,
            InMemoryNotificationRepository.getInstance().getUnreadNotifications(args[USERNAME_IDX]));
    }
}
