package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.expense;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.Group;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.group.InMemoryGroupRepository;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.nio.channels.SocketChannel;
import java.util.Set;

public final class SplitGroupCommand extends SplitCommand {
    public SplitGroupCommand(String[] args, SocketChannel socket) {
        super(args, CommandType.SPLIT_GROUP, socket);
    }

    @Override
    protected Group getGroup(User runner) throws ExpenseSplitException {
        return InMemoryGroupRepository.getInstance().getGroup(args[NAME_IDX], runner.getUsername());
    }

    @Override
    protected String invalidGroupMessage() {
        return String.format(INVALID_GROUP_MESSAGE, "group");
    }

    @Override
    protected String getName(Group group) {
        return group.name();
    }

    @Override
    protected String getIdentifier(String runner, Group group) throws ExpenseSplitException {
        Set<String> g = group.usersExcept(runner);
        StringBuilder result = new StringBuilder();

        for (String username : g) {
            result.append(getDebtStatus(runner, username));
        }

        return result.toString();
    }
}
