package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.general;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class HelpCommand extends Command {
    public HelpCommand(String[] args) {
        super(args, CommandType.HELP, null);
    }

    @Override
    protected String executeCommand() throws ExpenseSplitException {
        return Arrays.stream(CommandType.values())
            .filter(type -> type != CommandType.HELP)
            .map(CommandType::toString)
            .collect(Collectors.joining("\n"));
    }
}
