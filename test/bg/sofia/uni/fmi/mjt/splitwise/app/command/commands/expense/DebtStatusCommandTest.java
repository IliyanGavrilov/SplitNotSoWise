package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.expense;

import bg.sofia.uni.fmi.mjt.splitwise.api.client.ExchangeRatesClient;
import bg.sofia.uni.fmi.mjt.splitwise.api.dto.conversion.ExchangeRates;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositoryManager;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.expense.ExpenseRepository;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.user.UserRepository;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExchangeAPIException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.math.BigDecimal;
import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class DebtStatusCommandTest {
    private UserRepository userRepository;
    private ExpenseRepository expenseRepository;
    private String[] args;
    private StatusCommand command;

    @BeforeEach
    void setUp() {
        RepositoryManager repositoryManager = mock();
        userRepository = mock();
        expenseRepository = mock();
        SocketChannel socket = mock();
        args = new String[] {"100", "debtor"};
        when(repositoryManager.userRepository()).thenReturn(userRepository);
        when(repositoryManager.expenseRepository()).thenReturn(expenseRepository);
        command = spy(new StatusCommand(args, socket, repositoryManager));
    }

    @Test
    void displayAmountShouldRoundToTwoDecimals() {
        BigDecimal result = command.displayAmount(new BigDecimal("10.456"));
        assertEquals(new BigDecimal("10.46"), result, "displayAmount() should round to two decimal places");
    }

    @Test
    void checkForSelfCommandShouldThrowException() {
        args[1] = "runner";
        ExpenseSplitException exception = assertThrows(
            ExpenseSplitException.class,
            () -> command.checkForSelfCommand("runner"),
            "checkForSelfCommand() should throw exception if runner equals target"
        );
        assertEquals("Cannot split or pay money to yourself!", exception.getMessage());
    }

    @Test
    void getDebtStatusShouldReturnOwesYouForPositive() throws ExchangeAPIException, ExpenseSplitException {
        User runner = mock();
        User debited = mock();
        when(userRepository.getUserByUsername("runner")).thenReturn(runner);
        when(userRepository.getUserByUsername("debtor")).thenReturn(debited);
        when(runner.getUsername()).thenReturn("runner");
        when(debited.getUsername()).thenReturn("debtor");
        when(runner.getCurrency()).thenReturn("USD");
        when(expenseRepository.getDebtStatus("runner", "debtor")).thenReturn(new BigDecimal("50"));
        ExchangeRates rates = mock();
        when(rates.convertFromBaseCurrency(any(), anyString())).thenReturn(new BigDecimal("50"));
        try (MockedStatic<ExchangeRatesClient> mockedClient = mockStatic(ExchangeRatesClient.class)) {
            ExchangeRatesClient clientMock = mock();
            when(clientMock.getExchangeRates()).thenReturn(rates);
            mockedClient.when(ExchangeRatesClient::getInstance).thenReturn(clientMock);
            String result = command.getDebtStatus("runner", "debtor");
            assertEquals("Owes you 50.00 USD", result, "getDebtStatus() should return 'Owes you' for positive debt");
        }
    }

    @Test
    void getDebtStatusShouldReturnYouOweForNegative() throws ExchangeAPIException, ExpenseSplitException {
        User runner = mock();
        User debited = mock();
        when(userRepository.getUserByUsername("runner")).thenReturn(runner);
        when(userRepository.getUserByUsername("debtor")).thenReturn(debited);
        when(runner.getUsername()).thenReturn("runner");
        when(debited.getUsername()).thenReturn("debtor");
        when(runner.getCurrency()).thenReturn("USD");
        when(expenseRepository.getDebtStatus("runner", "debtor")).thenReturn(new BigDecimal("-30"));
        ExchangeRates rates = mock();
        when(rates.convertFromBaseCurrency(any(), anyString())).thenReturn(new BigDecimal("30"));
        try (MockedStatic<ExchangeRatesClient> mockedClient = mockStatic(ExchangeRatesClient.class)) {
            ExchangeRatesClient clientMock = mock();
            when(clientMock.getExchangeRates()).thenReturn(rates);
            mockedClient.when(ExchangeRatesClient::getInstance).thenReturn(clientMock);
            String result = command.getDebtStatus("runner", "debtor");
            assertEquals("You owe 30.00 USD", result, "getDebtStatus() should return 'You owe' for negative debt");
        }
    }
}