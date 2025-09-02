package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.expense;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.Group;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.group.InMemoryGroupRepository;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.user.InMemoryUserRepository;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.nio.channels.SocketChannel;

public final class SplitFriendshipCommand extends SplitCommand {
    public SplitFriendshipCommand(String[] args, SocketChannel socket) {
        super(args, CommandType.SPLIT, socket);
    }

    @Override
    protected Group getGroup(User runner) throws ExpenseSplitException {
        return InMemoryGroupRepository.getInstance().getGroup(runner.getUsername(), args[NAME_IDX]);
    }

    @Override
    protected String invalidGroupMessage() {
        return String.format(INVALID_GROUP_MESSAGE, "friend");
    }

    @Override
    protected String getName(Group group) throws ExpenseSplitException {
        return InMemoryUserRepository.getInstance().getUserByUsername(args[NAME_IDX]).getNickname();
    }

    @Override
    protected String getIdentifier(String runner, Group group) throws ExpenseSplitException {
        return getDebtStatus(runner, args[NAME_IDX]);
    }
}
