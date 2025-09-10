package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.expense;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.expense.EqualSplitExpense;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.Group;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositoryManager;
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

    private String extractReason() {
        return String.join(REASON_DELIMITER, Arrays.copyOfRange(args, REASON_FOR_PAYMENT_IDX, args.length));
    }

    private void notifyMembers(User runner, Group group, BigDecimal inputAmount) throws ExpenseSplitException {
        User debited = repositoryManager.userRepository().getUserByUsername(args[USERNAME_IDX]);

        repositoryManager.notificationRepository()
            .addNotification(runner.getUsername(),
                String.format(NOTIFICATION_MESSAGE, runner.getNickname(), displayAmount(inputAmount),
                    debited.getCurrency()),
                group);
    }

    public SplitCommand(String[] args, CommandType type, SocketChannel socket, RepositoryManager repositoryManager) {
        super(args, type, socket, repositoryManager);
    }

    @Override
    protected String executeCommand() throws ExpenseSplitException {
        User runner = getCommandRunner();
        checkForSelfCommand(runner.getUsername());

        Group group = getGroup(runner);
        validateGroup(group);

        String reason = extractReason();
        BigDecimal inputAmount = getInputAmount(runner.getCurrency());

        repositoryManager.expenseRepository()
            .addExpense(new EqualSplitExpense(inputAmount, runner.getUsername(), group, reason));

        notifyMembers(runner, group, inputAmount);

        return String.format(SUCCESS_MESSAGE, displayAmount(getOutputAmount(runner.getCurrency())),
            runner.getCurrency(), getName(group), getIdentifier(runner.getUsername(), group));
    }

    protected abstract Group getGroup(User runner) throws ExpenseSplitException;

    protected abstract void validateGroup(Group group) throws ExpenseSplitException;

    protected abstract String getName(Group group) throws ExpenseSplitException;

    protected abstract String getIdentifier(String runner, Group group) throws ExpenseSplitException;
}
