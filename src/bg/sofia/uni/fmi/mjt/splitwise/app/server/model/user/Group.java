package bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user;

import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.expense.UserNotInGroupException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record Group(String name, Set<String> users, GroupType type) {
    public static final int FRIENDSHIP_GROUP_SIZE = 2;
    private static final String FRIENDSHIP_GROUP_NAME_DELIMITER = " ";

    public Group(String name, Set<String> users, GroupType type) {
        if (isFriendShipGroup(users)) {
            this.type = GroupType.FRIEND;
            try {
                this.name = getFriendshipGroupName(users);
            } catch (ExpenseSplitException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.type = GroupType.GROUP;
            this.name = name;
        }
        this.users = users;
    }

    public Group(String name, Set<String> users) {
        this(name, users, null);
    }

    public Group(Set<String> users) {
        this(null, users);
    }

    public static String getFriendshipGroupName(Set<String> members) throws ExpenseSplitException {
        if (members.size() != FRIENDSHIP_GROUP_SIZE) {
            throw new ExpenseSplitException(
                "Invalid friendship group size! Friendship group size must be " + FRIENDSHIP_GROUP_SIZE);
        }

        return members.stream()
            .sorted() // Ensures uniqueness regardless of input order
            .collect(Collectors.joining(FRIENDSHIP_GROUP_NAME_DELIMITER));
    }

    public Set<String> usersExcept(String username) {
        Set<String> usersWithoutCredited = new HashSet<>(users);

        if (usersWithoutCredited.remove(username)) {
            return usersWithoutCredited;
        }

        throw new UserNotInGroupException("Given user is not in group!");
    }

    public int size() {
        return users.size();
    }

    public String name(String username) {
        if (type == GroupType.FRIEND) {
            return name.replace(username, "").replace(FRIENDSHIP_GROUP_NAME_DELIMITER, "");
        }

        return name;
    }

    public boolean isFriendShipGroup(Set<String> users) {
        return users.size() == FRIENDSHIP_GROUP_SIZE;
    }

    public boolean isFriendshipGroup() {
        return type == GroupType.FRIEND && users.size() == FRIENDSHIP_GROUP_SIZE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Group that = (Group) o;

        return Objects.equals(name, that.name) && users.equals(that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, users);
    }
}
