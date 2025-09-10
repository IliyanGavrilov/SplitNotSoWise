package bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.group;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.Group;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.util.Set;

public interface GroupRepository {
    boolean groupExistsForUsers(String groupName, Set<String> users);

    void addGroup(Set<String> members);

    void addGroup(String groupName, Set<String> members) throws ExpenseSplitException;

    Set<Group> getUserTwoPersonGroups(String username);

    Set<Group> getUserMultiPersonGroups(String username);

    Set<Group> getUserGroups(String username);

    Group getGroup(String groupName, String username) throws ExpenseSplitException;

    Group getGroup(Set<String> members) throws ExpenseSplitException;
}
