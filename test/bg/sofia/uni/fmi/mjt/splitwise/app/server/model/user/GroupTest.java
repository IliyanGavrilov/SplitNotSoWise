package bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user;

import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.expense.UserNotInGroupException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GroupTest {
    private Set<String> friendshipUsers;
    private Group friendshipGroup;
    private Group normalGroup;

    @BeforeEach
    void setUp() {
        friendshipUsers = new HashSet<>();
        friendshipUsers.add("Alice");
        friendshipUsers.add("Bob");
        Set<String> groupUsers = new HashSet<>();
        groupUsers.add("Alice");
        groupUsers.add("Bob");
        groupUsers.add("Charlie");
        friendshipGroup = new Group(friendshipUsers);
        normalGroup = new Group("Team", groupUsers, GroupType.GROUP);
    }

    @Test
    void testFriendshipGroupName() throws ExpenseSplitException {
        String expectedName = "Alice Bob";
        assertEquals(expectedName, Group.getFriendshipGroupName(friendshipUsers),
            "Friendship group name should match expected");
    }

    @Test
    void testFriendshipGroupThrowsExceptionForInvalidSize() {
        Set<String> invalidUsers = new HashSet<>();
        invalidUsers.add("Alice");
        invalidUsers.add("Bob");
        invalidUsers.add("Charlie");
        assertThrows(ExpenseSplitException.class, () -> Group.getFriendshipGroupName(invalidUsers),
            "Should throw ExpenseSplitException for invalid friendship group size");
    }

    @Test
    void testUsersExceptRemovesUser() {
        Set<String> expected = Set.of("Bob");
        assertEquals(expected, friendshipGroup.usersExcept("Alice"), "usersExcept should remove the given user");
    }

    @Test
    void testUsersExceptThrowsForUnknownUser() {
        assertThrows(UserNotInGroupException.class, () -> friendshipGroup.usersExcept("Charlie"),
            "Should throw UserNotInGroupException if user not in group");
    }

    @Test
    void testSizeReturnsCorrectNumber() {
        assertEquals(2, friendshipGroup.size(), "Size should match the number of users in friendship group");
    }

    @Test
    void testNameForFriendshipGroupRemovesUsername() {
        String result = friendshipGroup.name("Alice");
        assertEquals("Bob", result, "name(username) should remove the given username for friendship groups");
    }

    @Test
    void testNameForNormalGroupReturnsOriginal() {
        assertEquals("Team", normalGroup.name("Alice"), "name(username) should return original name for normal groups");
    }

    @Test
    void testIsFriendshipGroupTrue() {
        assertTrue(friendshipGroup.isFriendshipGroup(), "isFriendshipGroup should return true for friendship group");
    }

    @Test
    void testIsFriendshipGroupFalse() {
        assertFalse(normalGroup.isFriendshipGroup(), "isFriendshipGroup should return false for normal group");
    }

    @Test
    void testEqualsAndHashCode() {
        Group sameGroup = new Group(friendshipUsers);
        assertEquals(friendshipGroup, sameGroup, "Groups with same name and users should be equal");
    }

    @Test
    void testHashCodeDifferentForDifferentGroups() {
        Set<String> users1 = Set.of("Alice", "Bob");
        Set<String> users2 = Set.of("Alice", "Charlie");
        Group group1 = new Group(users1);
        Group group2 = new Group(users2);
        assertNotEquals(group1.hashCode(), group2.hashCode(),
            "Groups with different users should have different hash codes");
    }
}
