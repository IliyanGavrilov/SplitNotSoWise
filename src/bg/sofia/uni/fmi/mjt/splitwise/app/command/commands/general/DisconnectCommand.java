package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.general;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

public final class DisconnectCommand extends Command {
    public DisconnectCommand(String[] args) {
        super(args, CommandType.DISCONNECT, null);
    }

    @Override
    protected String executeCommand() throws ExpenseSplitException {
        return "User disconnected successfully!";
    }
}
