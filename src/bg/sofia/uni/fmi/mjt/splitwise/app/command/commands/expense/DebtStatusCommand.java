package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.expense;

import bg.sofia.uni.fmi.mjt.splitwise.api.client.ExchangeRatesClient;
import bg.sofia.uni.fmi.mjt.splitwise.api.dto.conversion.ExchangeRates;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositoryManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExchangeAPIException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.channels.SocketChannel;

public abstract sealed class DebtStatusCommand extends Command permits PaidCommand, StatusCommand, SplitCommand {
    private static final String DEBT_STATUS_EQUAL = "Equal";
    private static final String DEBT_STATUS_POSITIVE = "Owes you %s %s";
    private static final String DEBT_STATUS_NEGATIVE = "You owe %s %s";

    private static final int MONEY_SCALE = 2;
    private static final int AMOUNT_IDX = 0;
    private static final int EQUAL = 0;
    private static final int GREATER = 1;
    protected static final int USERNAME_IDX = 1;

    private ExchangeRates getExchangeRates() throws ExchangeAPIException {
        return ExchangeRatesClient.getInstance().getExchangeRates();
    }

    protected BigDecimal displayAmount(BigDecimal amount) {
        return amount.setScale(MONEY_SCALE, RoundingMode.HALF_EVEN);
    }

    // Convert input to program's base currency for calculations
    protected BigDecimal getInputAmount(String runnerCurrency) throws ExchangeAPIException {
        return getExchangeRates().convertToBaseCurrency(new BigDecimal(args[AMOUNT_IDX]), runnerCurrency);
    }

    // Convert calculated amount to user's currency for program
    protected BigDecimal getOutputAmount(String runnerCurrency) throws ExchangeAPIException {
        return getExchangeRates().convertFromBaseCurrency(getInputAmount(runnerCurrency), runnerCurrency);
    }

    protected void checkForSelfCommand(String runner) throws ExpenseSplitException {
        if (args[USERNAME_IDX].equals(runner)) {
            throw new ExpenseSplitException("Cannot split or pay money to yourself!");
        }
    }

    protected String getDebtStatus(String runner, String debited) throws ExpenseSplitException {
        User r = repositoryManager.userRepository().getUserByUsername(runner);
        User d = repositoryManager.userRepository().getUserByUsername(debited);

        return getDebtStatus(r, d);
    }

    protected String getDebtStatus(User runner, User debited) throws ExchangeAPIException {
        ExchangeRates exchangeRates = ExchangeRatesClient.getInstance().getExchangeRates();

        BigDecimal amount =
            displayAmount(
                repositoryManager.expenseRepository().getDebtStatus(runner.getUsername(), debited.getUsername()));
        String convertedAmount =
            String.valueOf(displayAmount(exchangeRates.convertFromBaseCurrency(amount, runner.getCurrency()).abs()));

        return switch (amount.compareTo(BigDecimal.ZERO)) {
            case EQUAL -> DEBT_STATUS_EQUAL;
            case GREATER -> String.format(DEBT_STATUS_POSITIVE, convertedAmount, runner.getCurrency());
            // case LESS_THAN a.k.a -1
            default -> String.format(DEBT_STATUS_NEGATIVE, convertedAmount, runner.getCurrency());
        };
    }

    public DebtStatusCommand(String[] args, CommandType type, SocketChannel socket,
                             RepositoryManager repositoryManager) {
        super(args, type, socket, repositoryManager);
    }
}
