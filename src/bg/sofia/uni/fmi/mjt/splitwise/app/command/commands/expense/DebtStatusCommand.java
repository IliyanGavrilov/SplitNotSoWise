package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.expense;

import bg.sofia.uni.fmi.mjt.splitwise.api.client.ExchangeRatesClient;
import bg.sofia.uni.fmi.mjt.splitwise.api.dto.conversion.ExchangeRates;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.expense.InMemoryExpenseRepository;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.user.InMemoryUserRepository;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExchangeAPIException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.math.BigDecimal;
import java.nio.channels.SocketChannel;

public abstract sealed class DebtStatusCommand extends Command permits PaidCommand, StatusCommand, SplitCommand {
    private static final String DEBT_STATUS_EQUAL = "Equal";
    private static final String DEBT_STATUS_POSITIVE = "Owes you %s %s";
    private static final String DEBT_STATUS_NEGATIVE = "You owe %s %s";

    private static final int AMOUNT_IDX = 0;
    private static final int EQUAL = 0;
    private static final int GREATER = 1;

    private ExchangeRates getExchangeRates() throws ExchangeAPIException {
        return ExchangeRatesClient.getInstance().getExchangeRates();
    }

    public DebtStatusCommand(String[] args, CommandType type, SocketChannel socket) {
        super(args, type, socket);
    }

    // Convert input to program's base currency for calculations
    protected BigDecimal getInputAmount(String runnerCurrency) throws ExchangeAPIException {
        return getExchangeRates().convertToBaseCurrency(new BigDecimal(args[AMOUNT_IDX]), runnerCurrency);
    }

    // Convert calculated amount to user's currency for program
    protected BigDecimal getOutputAmount(String runnerCurrency) throws ExchangeAPIException {
        return getExchangeRates().convertFromBaseCurrency(new BigDecimal(args[AMOUNT_IDX]), runnerCurrency);
    }

    protected String getDebtStatus(String runner, String debited) throws ExpenseSplitException {
        User r = InMemoryUserRepository.getInstance().getUserByUsername(runner);
        User d = InMemoryUserRepository.getInstance().getUserByUsername(debited);

        return getDebtStatus(r, d);
    }

    protected String getDebtStatus(User runner, User debited) throws ExchangeAPIException {
        ExchangeRates exchangeRates = ExchangeRatesClient.getInstance().getExchangeRates();

        BigDecimal amount =
            InMemoryExpenseRepository.getInstance().getDebtStatus(runner.getUsername(), debited.getUsername());

        return switch (amount.compareTo(BigDecimal.ZERO)) {
            case EQUAL -> DEBT_STATUS_EQUAL;
            case GREATER ->
                String.format(DEBT_STATUS_POSITIVE, exchangeRates.convertFromBaseCurrency(amount, runner.getCurrency()),
                    runner.getCurrency());
            // case LESS_THAN a.k.a -1
            default -> String.format(DEBT_STATUS_NEGATIVE,
                exchangeRates.convertFromBaseCurrency(amount, runner.getCurrency()).abs(), runner.getCurrency());
        };
    }
}
