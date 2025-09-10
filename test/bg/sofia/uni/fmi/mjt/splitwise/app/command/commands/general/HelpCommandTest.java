package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.general;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HelpCommandTest {
    private HelpCommand helpCommand;

    @BeforeEach
    void setUp() {
        helpCommand = new HelpCommand(new String[] {});
    }

    @Test
    void testExecuteReturnsOkJsonMessage() {
        String result = helpCommand.execute();
        assertTrue(result.contains("\"status\":\"OK\""), "execute should return OK JSON status");
    }

    @Test
    void testArgumentsAreStored() {
        String[] args = helpCommand.arguments();
        assertEquals(0, args.length, "Constructor should store provided arguments array");
    }
}
