package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.expense;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.expense.EqualSplitExpense;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.expense.Expense;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositoryManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.math.BigDecimal;
import java.nio.channels.SocketChannel;
import java.util.Set;

public final class PaidCommand extends DebtStatusCommand {
    private static final String SUCCESS_MESSAGE = "%s (%s) paid you %s %s\nCurrent status: %s";
    private static final String REASON = "Pay back";
    private static final String NOTIFICATION_MESSAGE = "%s approved your payment of %s %s. [%s]";

    private void registerPayment(User runner, User debited, BigDecimal inputAmount) {
        Expense expense =
            new EqualSplitExpense(inputAmount, runner.getUsername(), null, "Pay back");
        repositoryManager.expenseRepository().pay(expense, debited.getUsername());
    }

    private void notifyUser(User runner, User debited, BigDecimal inputAmount) throws ExpenseSplitException {
        repositoryManager.notificationRepository().addNotification(runner.getUsername(),
            String.format(NOTIFICATION_MESSAGE, runner.getNickname(), displayAmount(inputAmount),
                debited.getCurrency(), REASON),
            repositoryManager.groupRepository().getGroup(Set.of(runner.getUsername(), debited.getUsername())));
    }

    public PaidCommand(String[] args, SocketChannel socket, RepositoryManager repositoryManager) {
        super(args, CommandType.PAID, socket, repositoryManager);
    }

    @Override
    protected String executeCommand() throws ExpenseSplitException {
        User runner = getCommandRunner();
        checkForSelfCommand(runner.getUsername());

        User debited = repositoryManager.userRepository().getUserByUsername(args[USERNAME_IDX]);

        BigDecimal inputAmount = getInputAmount(runner.getCurrency());

        registerPayment(runner, debited, inputAmount);

        notifyUser(runner, debited, inputAmount);

        String currStatus = getDebtStatus(runner, debited);
        return String.format(SUCCESS_MESSAGE, debited.getNickname(), debited.getUsername(),
            displayAmount(getOutputAmount(runner.getCurrency())), runner.getCurrency(), currStatus);
    }
}
