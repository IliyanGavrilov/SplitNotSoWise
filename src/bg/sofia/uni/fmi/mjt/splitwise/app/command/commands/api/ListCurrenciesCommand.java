package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.api;

import bg.sofia.uni.fmi.mjt.splitwise.api.client.ExchangeRatesClient;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

public final class ListCurrenciesCommand extends Command {
    private static final int CURRENCY_CODE_IDX = 0;
    private static final String SUCCESS_MESSAGE = "Successfully changed currency to %s!";

    public ListCurrenciesCommand(String[] args) {
        super(args, CommandType.LIST_CURRENCIES, null);
    }

    @Override
    protected String executeCommand() throws ExpenseSplitException {
        return ExchangeRatesClient.getInstance().getExchangeRates().getAvailableCurrencies().toString();
    }
}
