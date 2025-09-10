package bg.sofia.uni.fmi.mjt.splitwise.app.command;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositoryManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class CommandTypeTest {
    private SocketChannel channel;
    private RepositoryManager repositoryManager;

    @BeforeEach
    void setUp() {
        channel = mock(SocketChannel.class);
        repositoryManager = mock(RepositoryManager.class);
    }

    @Test
    void testFromStringWithAliases() {
        assertEquals(CommandType.LOGIN, CommandType.fromString("login"), "Should recognize 'login'");
        assertEquals(CommandType.LOGOUT, CommandType.fromString("logout"), "Should recognize 'logout'");
        assertEquals(CommandType.REGISTER, CommandType.fromString("register"), "Should recognize 'register'");
        assertEquals(CommandType.ADD_FRIEND, CommandType.fromString("add-user"), "Should recognize alias 'add-user'");
        assertNull(CommandType.fromString("nonexistent"), "Should return null for unknown commands");
    }

    @Test
    void testGetCommandAllTypes() {
        CommandDefinition defLogin = new CommandDefinition("login", new String[] {"user", "pass"});
        Command cLogin = CommandType.getCommand(defLogin, channel, repositoryManager);
        assertEquals("LoginCommand", cLogin.getClass().getSimpleName(), "LOGIN should return LoginCommand");
        CommandDefinition defUnknown = new CommandDefinition("xyz", new String[] {});
        Command cUnknown = CommandType.getCommand(defUnknown, channel, repositoryManager);
        assertEquals("UnknownCommand", cUnknown.getClass().getSimpleName(), "Unknown should return UnknownCommand");
        CommandDefinition defHelp = new CommandDefinition("help", new String[] {});
        Command cHelp = CommandType.getCommand(defHelp, channel, repositoryManager);
        assertEquals("HelpCommand", cHelp.getClass().getSimpleName(), "HELP should return HelpCommand");
    }

    @Test
    void testValidateArgumentsExactCorrect() {
        assertDoesNotThrow(() -> CommandType.LOGIN.validateArguments(new String[] {"u", "p"}),
            "Exact args count correct should not throw");
    }

    @Test
    void testValidateArgumentsExactTooFew() {
        ExpenseSplitException ex = assertThrows(ExpenseSplitException.class,
            () -> CommandType.LOGIN.validateArguments(new String[] {"onlyOne"}),
            "Exact args count too few should throw");
        assertTrue(ex.getMessage().contains("Invalid count of arguments"),
            "Exception message should mention invalid count");
    }

    @Test
    void testValidateArgumentsExactTooMany() {
        ExpenseSplitException ex = assertThrows(ExpenseSplitException.class,
            () -> CommandType.LOGIN.validateArguments(new String[] {"a", "b", "c"}),
            "Exact args count too many should throw");
        assertTrue(ex.getMessage().contains("Invalid count of arguments"),
            "Exception message should mention invalid count");
    }

    @Test
    void testValidateArgumentsAtLeastCorrect() {
        assertDoesNotThrow(() -> CommandType.REGISTER.validateArguments(new String[] {"u", "p", "n", "extra"}),
            "At least required args with extra should not throw");
    }

    @Test
    void testValidateArgumentsAtLeastTooFew() {
        ExpenseSplitException ex = assertThrows(ExpenseSplitException.class,
            () -> CommandType.REGISTER.validateArguments(new String[] {"u", "p"}),
            "At least required args too few should throw");
        assertTrue(ex.getMessage().contains("Invalid count of arguments"),
            "Exception message should mention insufficient args");
    }

    @Test
    void testValidateArgumentsNumberPositive() {
        assertDoesNotThrow(() -> CommandType.PAID.validateArguments(new String[] {"10", "user"}),
            "Positive number argument should not throw");
    }

    @Test
    void testValidateArgumentsNumberZero() {
        assertDoesNotThrow(() -> CommandType.PAID.validateArguments(new String[] {"0", "user"}),
            "Zero number argument should not throw");
    }

    @Test
    void testValidateArgumentsNumberNegative() {
        ExpenseSplitException ex = assertThrows(ExpenseSplitException.class,
            () -> CommandType.PAID.validateArguments(new String[] {"-5", "user"}),
            "Negative number argument should throw");
        assertTrue(ex.getMessage().contains("< 0"), "Exception message should mention negative number");
    }

    @Test
    void testValidateArgumentsNumberInvalidFormat() {
        ExpenseSplitException ex = assertThrows(ExpenseSplitException.class,
            () -> CommandType.PAID.validateArguments(new String[] {"abc", "user"}),
            "Invalid number format should throw");
        assertTrue(ex.getMessage().contains("not a valid number"), "Exception message should mention invalid number");
    }

    @Test
    void testToStringWithArgs() {
        String s = CommandType.LOGIN.toString();
        assertEquals("login <username> <password>", s, "toString should format LOGIN command correctly");
    }

    @Test
    void testToStringNoArgs() {
        String s = CommandType.LOGOUT.toString();
        assertEquals("logout ", s, "toString should format LOGOUT command correctly even with no args");
    }
}