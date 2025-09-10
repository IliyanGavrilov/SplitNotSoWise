package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.expense;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.Group;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositoryManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.nio.channels.SocketChannel;
import java.util.Set;

public final class StatusCommand extends DebtStatusCommand {
    private static final String SUCCESS_MESSAGE = "Friends:\n%s\nGroups:\n%s";

    private String getStatus(User runner, Set<Group> groups) throws ExpenseSplitException {
        StringBuilder status = new StringBuilder();

        for (Group group : groups) {
            Set<String> users = group.usersExcept(runner.getUsername());

            if (!users.isEmpty()) {
                // If multiple users, show group name; otherwise, just the single user
                if (users.size() > Group.FRIENDSHIP_GROUP_SIZE - 1) {
                    status.append("* ").append(group.name()).append("\n");
                }

                for (String username : users) {
                    User debited = repositoryManager.userRepository().getUserByUsername(username);

                    status.append(users.size() > Group.FRIENDSHIP_GROUP_SIZE - 1 ? "- " : "* ")
                        .append(debited.getNickname()).append(" (").append(username).append("): ")
                        .append(getDebtStatus(runner, debited)).append('\n');
                }
            }
        }

        return status.toString();
    }

    public StatusCommand(String[] args, SocketChannel socket, RepositoryManager repositoryManager) {
        super(args, CommandType.GET_STATUS, socket, repositoryManager);
    }

    @Override
    protected String executeCommand() throws ExpenseSplitException {
        User runner = repositoryManager.sessionManager().getLoggedUser(socket);

        Set<Group> friendships = repositoryManager.groupRepository().getUserTwoPersonGroups(runner.getUsername());
        Set<Group> groups = repositoryManager.groupRepository().getUserMultiPersonGroups(runner.getUsername());

        return String.format(SUCCESS_MESSAGE, getStatus(runner, friendships), getStatus(runner, groups));
    }
}
