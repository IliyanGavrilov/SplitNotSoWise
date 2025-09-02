package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.general;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

public final class UnknownCommand extends Command {
    public UnknownCommand(String[] args) {
        super(args, CommandType.UNKNOWN, null);
    }

    @Override
    protected String executeCommand() throws ExpenseSplitException {
        throw new ExpenseSplitException("Unknown command!");
    }
}
