package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.group;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.group.InMemoryGroupRepository;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.user.InMemoryUserRepository;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.user.UserSessionManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public abstract sealed class AddCommand extends Command permits AddFriendCommand, CreateGroupCommand {
    private static final int NAME_IDX = 1;
    private static final String NOT_REGISTERED = "User \"%s\" is not registered!";

    public AddCommand(String[] args, CommandType type, SocketChannel socket) {
        super(args, type, socket);
    }

    @Override
    protected String executeCommand() throws ExpenseSplitException {
        String runner = UserSessionManager.getInstance().getLoggedUsername(socket);

        Set<String> friends = Arrays.stream(args, NAME_IDX, args.length) // Get tokens without the group name
            .collect(Collectors.toSet());

        for (String friend : friends) {
            if (!InMemoryUserRepository.getInstance().isRegistered(friend)) {
                throw new ExpenseSplitException(String.format(NOT_REGISTERED, friend));
            }
        }

        InMemoryGroupRepository.getInstance().addGroup(runner, friends);

        return "Friend successfully added!";
    }
}
