package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.group;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.group.InMemoryGroupRepository;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.Set;

public final class AddFriendCommand extends AddCommand {
    private static final int MEMBERS_NAME_START_IDX = 0;

    public AddFriendCommand(String[] args, SocketChannel socket) {
        super(args, CommandType.ADD_FRIEND, socket);
    }

    @Override
    protected Set<String> getMembers(String runner, String[] args) throws ExpenseSplitException {
        if (Objects.equals(args[MEMBERS_NAME_START_IDX], runner)) {
            throw new ExpenseSplitException("Cannot add yourself as a friend!");
        }
        
        return Set.of(args[MEMBERS_NAME_START_IDX]);
    }

    @Override
    protected void addGroup(Set<String> members) {
        InMemoryGroupRepository.getInstance().addGroup(members);
    }

    @Override
    protected String getSuccessMessage() {
        return "Friend successfully added!";
    }
}
