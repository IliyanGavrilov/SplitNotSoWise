package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.expense;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.Group;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.group.InMemoryGroupRepository;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.user.InMemoryUserRepository;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.nio.channels.SocketChannel;
import java.util.Set;

public final class SplitFriendshipCommand extends SplitCommand {
    public SplitFriendshipCommand(String[] args, SocketChannel socket) {
        super(args, CommandType.SPLIT, socket);
    }

    @Override
    protected Group getGroup(User runner) throws ExpenseSplitException {
        return InMemoryGroupRepository.getInstance().getGroup(Set.of(runner.getUsername(), args[USERNAME_IDX]));
    }

    @Override
    protected void validateGroup(Group group) throws ExpenseSplitException {
        if (group == null) {
            throw new ExpenseSplitException(String.format(INVALID_GROUP_MESSAGE, "friend"));
        }
        if (!group.isFriendshipGroup()) {
            throw new ExpenseSplitException("The specified user is not your friend; it refers to a group instead!");
        }
    }

    @Override
    protected String getName(Group group) throws ExpenseSplitException {
        return InMemoryUserRepository.getInstance().getUserByUsername(args[USERNAME_IDX]).getNickname();
    }

    @Override
    protected String getIdentifier(String runner, Group group) throws ExpenseSplitException {
        return getDebtStatus(runner, args[USERNAME_IDX]);
    }
}
