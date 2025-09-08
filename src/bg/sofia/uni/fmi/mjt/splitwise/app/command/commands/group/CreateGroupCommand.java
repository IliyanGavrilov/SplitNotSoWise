package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.group;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.group.InMemoryGroupRepository;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public final class CreateGroupCommand extends AddCommand {
    private static final int GROUP_NAME_IDX = 0;
    private static final int MEMBERS_NAME_START_IDX = 1;

    public CreateGroupCommand(String[] args, SocketChannel socket) {
        super(args, CommandType.CREATE_GROUP, socket);
    }

    @Override
    protected Set<String> getMembers(String runner, String[] args) throws ExpenseSplitException {
        Set<String> members = Arrays.stream(args, MEMBERS_NAME_START_IDX, args.length).collect(Collectors.toSet());

        if (members.contains(runner)) {
            throw new ExpenseSplitException("You are already part of the group and cannot be added again!");
        }

        return members;
    }

    @Override
    protected void addGroup(Set<String> members) throws ExpenseSplitException {
        InMemoryGroupRepository.getInstance().addGroup(args[GROUP_NAME_IDX], members);
    }

    @Override
    protected String getSuccessMessage() {
        return "Group successfully created!";
    }
}