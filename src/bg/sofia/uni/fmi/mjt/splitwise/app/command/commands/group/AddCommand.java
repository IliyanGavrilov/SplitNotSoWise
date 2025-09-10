package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.group;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositoryManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Set;

public abstract sealed class AddCommand extends Command permits AddFriendCommand, CreateGroupCommand {
    private static final String NOT_REGISTERED = "User \"%s\" is not registered!";

    public AddCommand(String[] args, CommandType type, SocketChannel socket, RepositoryManager repositoryManager) {
        super(args, type, socket, repositoryManager);
    }

    @Override
    protected final String executeCommand() throws ExpenseSplitException {
        String runner = repositoryManager.sessionManager().getLoggedUsername(socket);
        Set<String> members = new HashSet<>(getMembers(runner, args));

        members.add(runner);

        for (String member : members) {
            if (!repositoryManager.userRepository().isRegistered(member)) {
                throw new ExpenseSplitException(String.format(NOT_REGISTERED, member));
            }
        }

        addGroup(members);

        return getSuccessMessage();
    }

    protected abstract Set<String> getMembers(String runner, String[] args) throws ExpenseSplitException;

    protected abstract void addGroup(Set<String> members) throws ExpenseSplitException;

    protected abstract String getSuccessMessage();
}
