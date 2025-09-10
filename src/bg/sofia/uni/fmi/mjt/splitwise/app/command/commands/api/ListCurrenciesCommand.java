package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.api;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.Command;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositoryManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

public final class ListCurrenciesCommand extends Command {
    public ListCurrenciesCommand(String[] args, RepositoryManager repositoryManager) {
        super(args, CommandType.LIST_CURRENCIES, repositoryManager);
    }

    @Override
    protected String executeCommand() throws ExpenseSplitException {
        return repositoryManager.exchangeRatesClient().getExchangeRates().getAvailableCurrencies().toString();
    }
}
