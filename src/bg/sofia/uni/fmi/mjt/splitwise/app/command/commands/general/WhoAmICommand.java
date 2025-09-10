package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.general;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.Group;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositoryManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.nio.channels.SocketChannel;
import java.util.stream.Collectors;

public final class WhoAmICommand extends Command {
    private static final String SUCCESS_MESSAGE =
        "Logged in as:\n* Username: %s\n* Nickname: %s\n* Currency: %s\n* Friends: %s\n* Groups: %s";
    private static final String NO_RESULTS = "-";
    private static final String LIST_DELIMITER = ", ";

    public WhoAmICommand(String[] args, SocketChannel socket, RepositoryManager repositoryManager) {
        super(args, CommandType.UNKNOWN, socket, repositoryManager);
    }

    @Override
    protected String executeCommand() throws ExpenseSplitException {
        User runner = repositoryManager.sessionManager().getLoggedUser(socket);

        String friends = repositoryManager.groupRepository()
            .getUserTwoPersonGroups(runner.getUsername()).stream()
            .map(g -> g.usersExcept(runner.getUsername()).iterator().next()) // get the other user
            .collect(Collectors.joining(LIST_DELIMITER));
        friends = friends.isBlank() ? NO_RESULTS : friends;

        String groups = repositoryManager.groupRepository()
            .getUserMultiPersonGroups(runner.getUsername()).stream()
            .map(Group::name) // group names
            .collect(Collectors.joining(LIST_DELIMITER));
        groups = groups.isBlank() ? NO_RESULTS : groups;

        return String.format(SUCCESS_MESSAGE, runner.getUsername(), runner.getNickname(), runner.getCurrency(),
            friends, groups);
    }
}
