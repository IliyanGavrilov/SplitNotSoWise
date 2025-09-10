package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.general;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnknownCommandTest {
    private UnknownCommand unknownCommand;

    @BeforeEach
    void setUp() {
        unknownCommand = new UnknownCommand(new String[] {});
    }

    @Test
    void testExecuteReturnsErrorJsonMessage() {
        String result = unknownCommand.execute();
        assertTrue(result.contains("\"status\":\"ERROR\"") && result.contains("Unknown command!"),
            "execute should return ERROR JSON message with correct text");
    }

    @Test
    void testArgumentsAreStored() {
        String[] args = unknownCommand.arguments();
        assertEquals(0, args.length, "Constructor should store provided arguments array");
    }
}
