package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.auth;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositoryManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.nio.channels.SocketChannel;

public final class LogoutCommand extends Command {
    public LogoutCommand(String[] args, SocketChannel socket, RepositoryManager repositoryManager) {
        super(args, CommandType.LOGOUT, socket, repositoryManager);
    }

    @Override
    protected String executeCommand() throws ExpenseSplitException {
        repositoryManager.sessionManager().logout(socket);

        return "User successfully logged out!";
    }
}
