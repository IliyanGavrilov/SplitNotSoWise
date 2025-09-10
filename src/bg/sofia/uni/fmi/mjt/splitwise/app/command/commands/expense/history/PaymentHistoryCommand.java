package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.expense.history;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositoryManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.nio.channels.SocketChannel;

public final class PaymentHistoryCommand extends Command {
    public PaymentHistoryCommand(String[] args, SocketChannel socket, RepositoryManager repositoryManager) {
        super(args, CommandType.PAYMENT_HISTORY, socket, repositoryManager);
    }

    @Override
    protected String executeCommand() throws ExpenseSplitException {
        String runner = getCommandRunnerUsername();

        return repositoryManager.expenseRepository().getHistory(runner).toString();
    }
}
