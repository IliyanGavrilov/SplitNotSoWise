package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.expense;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.expense.EqualSplitExpense;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.Group;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.expense.InMemoryExpenseRepository;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.notification.InMemoryNotificationRepository;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.user.UserSessionManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.math.BigDecimal;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public abstract sealed class SplitCommand extends DebtStatusCommand permits SplitFriendshipCommand, SplitGroupCommand {
    protected static final int REASON_FOR_PAYMENT_IDX = 2;
    protected static final String REASON_DELIMITER = " ";
    protected static final String INVALID_GROUP_MESSAGE = "Given %s does not exist!";

    private static final String SUCCESS_MESSAGE = "Split %s %s between you and %s\nCurrent status: %s";
    private static final String NOTIFICATION_MESSAGE = "%s ... %s [%s].";

    public SplitCommand(String[] args, CommandType type, SocketChannel socket) {
        super(args, type, socket);
    }

    @Override
    protected String executeCommand() throws ExpenseSplitException {
        User runner = UserSessionManager.getInstance().getLoggedUser(socket);
        checkForSelfCommand(runner.getUsername());

        Group group = getGroup(runner);
        validateGroup(group);

        String reason = String.join(REASON_DELIMITER, Arrays.copyOfRange(args, REASON_FOR_PAYMENT_IDX, args.length));

        BigDecimal inputAmount = getInputAmount(runner.getCurrency());
        InMemoryExpenseRepository.getInstance()
            .addExpense(new EqualSplitExpense(inputAmount, runner.getUsername(), group, reason));

        InMemoryNotificationRepository.getInstance()
            .addNotification(runner.getUsername(),
                String.format(NOTIFICATION_MESSAGE, runner.getNickname(), inputAmount, runner.getCurrency()),
                group);

        return String.format(SUCCESS_MESSAGE, getOutputAmount(runner.getCurrency()), runner.getCurrency(),
            getName(group), getIdentifier(runner.getUsername(), group));
    }

    protected abstract Group getGroup(User runner) throws ExpenseSplitException;

    protected abstract void validateGroup(Group group) throws ExpenseSplitException;

    protected abstract String getName(Group group) throws ExpenseSplitException;

    protected abstract String getIdentifier(String runner, Group group) throws ExpenseSplitException;
}
