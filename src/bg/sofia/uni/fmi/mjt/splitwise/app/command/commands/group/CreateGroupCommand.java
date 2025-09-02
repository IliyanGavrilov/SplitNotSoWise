package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.group;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;

import java.nio.channels.SocketChannel;

public final class CreateGroupCommand extends AddCommand {
    public CreateGroupCommand(String[] args, SocketChannel socket) {
        super(args, CommandType.CREATE_GROUP, socket);
    }
}
