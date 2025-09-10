package bg.sofia.uni.fmi.mjt.splitwise.app.command;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class CommandExecutorTest {
    private SocketChannel channel;
    private RepositoryManager repositoryManager;
    private CommandExecutor executor;
    private Command commandMockSuccess;
    private Command commandMockError;

    @BeforeEach
    void setUp() {
        channel = mock(SocketChannel.class);
        repositoryManager = mock(RepositoryManager.class);
        executor = new CommandExecutor();
        commandMockSuccess = mock(Command.class);
        commandMockError = mock(Command.class);
    }

    @Test
    void testExecuteBothSuccessAndError() {
        when(commandMockSuccess.execute()).thenReturn("SUCCESS");
        when(commandMockError.execute()).thenReturn("ERROR");
        try (MockedStatic<CommandFactory> mockedFactory = mockStatic(CommandFactory.class)) {
            mockedFactory.when(() -> CommandFactory.newCommand(channel, "success input", repositoryManager))
                .thenReturn(commandMockSuccess);
            mockedFactory.when(() -> CommandFactory.newCommand(channel, "fail input", repositoryManager))
                .thenReturn(commandMockError);
            String resultSuccess = executor.execute("success input", channel, repositoryManager);
            assertEquals("SUCCESS", resultSuccess, "Execute should return SUCCESS message");
            String resultError = executor.execute("fail input", channel, repositoryManager);
            assertEquals("ERROR", resultError, "Execute should return ERROR message");
        }
    }
}