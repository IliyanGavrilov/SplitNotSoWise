package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.group;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositoryManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public final class CreateGroupCommand extends AddCommand {
    private static final int GROUP_NAME_IDX = 0;
    private static final int MEMBERS_NAME_START_IDX = 1;

    public CreateGroupCommand(String[] args, SocketChannel socket, RepositoryManager repositoryManager) {
        super(args, CommandType.CREATE_GROUP, socket, repositoryManager);
    }

    @Override
    protected Set<String> getMembers(String runner, String[] args) throws ExpenseSplitException {
        Set<String> members = Arrays.stream(args, MEMBERS_NAME_START_IDX, args.length).collect(Collectors.toSet());

        if (members.contains(runner)) {
            throw new ExpenseSplitException("You cannot be added more than once to the group!");
        }

        return members;
    }

    @Override
    protected void addGroup(Set<String> members) throws ExpenseSplitException {
        repositoryManager.groupRepository().addGroup(args[GROUP_NAME_IDX], members);
    }

    @Override
    protected String getSuccessMessage() {
        return "Group successfully created!";
    }
}