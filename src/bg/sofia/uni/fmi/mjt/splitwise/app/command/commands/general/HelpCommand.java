package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.general;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

public final class HelpCommand extends Command {
    public HelpCommand(String[] args) {
        super(args, CommandType.HELP, null);
    }

    @Override
    protected String executeCommand() throws ExpenseSplitException {
        StringBuilder result2 = new StringBuilder();

        for (CommandType type : CommandType.values()) {
            if (type != CommandType.HELP) {
                result2.append(type).append('\n');
            }
        }

        return result2.toString();
    }
}
