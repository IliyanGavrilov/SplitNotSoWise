package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.expense.history;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.expense.InMemoryExpenseRepository;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.user.UserSessionManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.nio.channels.SocketChannel;

public final class PaymentHistoryCommand extends Command {
    public PaymentHistoryCommand(String[] args, SocketChannel socket) {
        super(args, CommandType.PAYMENT_HISTORY, socket);
    }

    @Override
    protected String executeCommand() throws ExpenseSplitException {
        String runner = UserSessionManager.getInstance().getLoggedUsername(socket);

        return InMemoryExpenseRepository.getInstance().getHistory(runner).toString();
    }
}
