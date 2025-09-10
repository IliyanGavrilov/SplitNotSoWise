package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.general;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DisconnectCommandTest {
    private DisconnectCommand disconnectCommand;

    @BeforeEach
    void setUp() {
        disconnectCommand = new DisconnectCommand(new String[] {});
    }

    @Test
    void testExecuteReturnsOkJsonMessage() {
        String result = disconnectCommand.execute();
        assertTrue(result.contains("\"status\":\"OK\"") && result.contains("User disconnected successfully!"),
            "execute should return OK JSON message with correct disconnect text");
    }

    @Test
    void testArgumentsAreStored() {
        String[] args = disconnectCommand.arguments();
        assertEquals(0, args.length, "Constructor should store provided arguments array");
    }
}
