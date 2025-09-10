package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.general;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositoryManager;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.group.GroupRepository;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SocketChannel;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WhoAmICommandTest {
    private WhoAmICommand whoAmICommand;
    private User user;

    @BeforeEach
    void setUp() throws ExpenseSplitException {
        RepositoryManager repositoryManagerMock = mock();
        GroupRepository groupRepositoryMock = mock();
        SocketChannel socketMock = mock();
        user = new User("john", "pass", "Johnny", "EUR");
        when(repositoryManagerMock.sessionManager()).thenReturn(mock());
        when(repositoryManagerMock.sessionManager().getLoggedUser(socketMock)).thenReturn(user);
        when(repositoryManagerMock.groupRepository()).thenReturn(groupRepositoryMock);
        when(groupRepositoryMock.getUserTwoPersonGroups(user.getUsername())).thenReturn(Collections.emptySet());
        when(groupRepositoryMock.getUserMultiPersonGroups(user.getUsername())).thenReturn(Collections.emptySet());
        whoAmICommand = new WhoAmICommand(new String[] {}, socketMock, repositoryManagerMock);
    }

    @Test
    void testExecuteReturnsOkJsonMessageWithNoFriendsOrGroups() {
        String result = whoAmICommand.execute();
        assertTrue(result.contains("\"status\":\"OK\"") && result.contains("Logged in as:"),
            "execute should return OK JSON message with correct header and no friends/groups");
    }

    @Test
    void testExecuteContainsCorrectUserDetails() {
        String result = whoAmICommand.execute();
        assertTrue(result.contains(user.getUsername()), "execute output should contain username");
        assertTrue(result.contains(user.getNickname()), "execute output should contain nickname");
        assertTrue(result.contains(user.getCurrency()), "execute output should contain currency");
    }

    @Test
    void testFriendsAndGroupsAreNoResultsWhenEmpty() {
        String result = whoAmICommand.execute();
        assertTrue(result.contains("-"), "Friends and groups should show NO_RESULTS when empty");
    }
}