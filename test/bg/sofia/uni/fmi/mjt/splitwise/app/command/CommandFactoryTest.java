package bg.sofia.uni.fmi.mjt.splitwise.app.command;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class CommandFactoryTest {
    private SocketChannel channel;
    private RepositoryManager repositoryManager;

    @BeforeEach
    void setUp() {
        channel = mock(SocketChannel.class);
        repositoryManager = mock(RepositoryManager.class);
    }

    @Test
    void testNewCommandReturnsCommandInstance() {
        Command command = CommandFactory.newCommand(channel, "test arg1 arg2", repositoryManager);
        assertNotNull(command, "CommandFactory should return a non-null Command instance");
    }

    @Test
    void testNewCommandTrimsInput() {
        Command command = CommandFactory.newCommand(channel, "   test arg   ", repositoryManager);
        assertNotNull(command, "CommandFactory should handle input with extra spaces");
    }

    @Test
    void testNewCommandWithQuotedArgument() {
        Command command = CommandFactory.newCommand(channel, "test \"arg with spaces\"", repositoryManager);
        assertNotNull(command, "CommandFactory should handle quoted arguments correctly");
    }
}