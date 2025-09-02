package bg.sofia.uni.fmi.mjt.splitwise.app.command;

import java.nio.channels.SocketChannel;

public class CommandExecutor {
    public String execute(String input, SocketChannel channel) {
        return CommandFactory.newCommand(channel, input).execute();
    }
}
