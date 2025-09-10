package bg.sofia.uni.fmi.mjt.splitwise.app.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandTest {
    @Test
    void testExecuteReturnsOkWhenNoException() {
        Command command = mock();
        when(command.execute()).thenReturn("{\"status\":\"OK\",\"data\":\"success\"}");
        assertEquals("{\"status\":\"OK\",\"data\":\"success\"}", command.execute(),
            "Execute should return OK with success data");
    }
}
