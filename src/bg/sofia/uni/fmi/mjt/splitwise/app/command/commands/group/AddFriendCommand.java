package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.group;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;

import java.nio.channels.SocketChannel;

public final class AddFriendCommand extends AddCommand {
    public AddFriendCommand(String[] args, SocketChannel socket) {
        super(args, CommandType.ADD_FRIEND, socket);
    }
}
