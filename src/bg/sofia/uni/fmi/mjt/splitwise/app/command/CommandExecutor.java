package bg.sofia.uni.fmi.mjt.splitwise.app.command;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositoryManager;

import java.nio.channels.SocketChannel;

public final class CommandExecutor {
    public String execute(String input, SocketChannel channel, RepositoryManager repositoryManager) {
        return CommandFactory.newCommand(channel, input, repositoryManager).execute();
    }
}
