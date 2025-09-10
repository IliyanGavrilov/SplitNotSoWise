package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.api;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositoryManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.nio.channels.SocketChannel;

public final class SwitchCurrencyCommand extends Command {
    private static final int CURRENCY_CODE_IDX = 0;
    private static final String SUCCESS_MESSAGE = "Successfully changed currency to %s!";

    public SwitchCurrencyCommand(String[] args, SocketChannel socket, RepositoryManager repositoryManager) {
        super(args, CommandType.SWITCH_CURRENCY, socket, repositoryManager);
    }

    @Override
    protected String executeCommand() throws ExpenseSplitException {
        User runner = getCommandRunner();
        String newCurrency = args[CURRENCY_CODE_IDX].toUpperCase();

        repositoryManager.userRepository().changeCurrency(runner, newCurrency);

        return String.format(SUCCESS_MESSAGE, newCurrency);
    }
}
