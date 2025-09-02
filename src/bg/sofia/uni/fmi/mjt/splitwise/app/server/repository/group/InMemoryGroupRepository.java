package bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.group;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.Group;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.GroupType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositorySaver;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InMemoryGroupRepository implements GroupRepository {
    private final Map<String, Set<Group>> groupsByName;
    private final Map<String, Set<Group>> groupsByUser;
    private static InMemoryGroupRepository instance;
    private static final int UNIQUE_GROUPS_CNT = 1;

    private Set<Group> filterGroupsUserIsIn(String username, Predicate<Group> predicate) {
        return groupsByUser.getOrDefault(username, Collections.emptySet())
            .stream()
            .filter(predicate)
            .collect(Collectors.toSet());
    }

    private Set<Group> filterGroupsByName(String groupName, Predicate<Group> predicate) {
        return groupsByName.getOrDefault(groupName, Collections.emptySet())
            .stream()
            .filter(predicate)
            .collect(Collectors.toSet());
    }

    private InMemoryGroupRepository() {
        groupsByName = RepositorySaver.loadGroupsByName();
        groupsByUser = RepositorySaver.loadGroupsByUser();
    }

    public static InMemoryGroupRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryGroupRepository();
        }

        return instance;
    }

    @Override
    public boolean groupExistsForUsers(String groupName, Set<String> users) {
        // Check if any of the users are already in a group with the same name
        return filterGroupsByName(groupName, group -> !Collections.disjoint(group.users(), users))
            .stream()
            .findAny()  // Return true if any group matches the condition
            .isPresent();
    }

    @Override
    public void addGroup(String... usernames) throws ExpenseSplitException {
        addGroup(Group.getFriendshipGroupName(new HashSet<>(Arrays.asList(usernames))),
            new HashSet<>(Arrays.asList(usernames)));
    }

    @Override
    public void addGroup(String groupName, Set<String> members) throws ExpenseSplitException {
        if (groupExistsForUsers(groupName, members)) {
            throw new ExpenseSplitException(
                "One or more of the given users are already in a group with the same name!");
        }

        Group group = new Group(groupName, members);

        groupsByName
            .computeIfAbsent(groupName, _ -> new HashSet<>())
            .add(group);

        for (String member : members) {
            groupsByUser.computeIfAbsent(member, _ -> new HashSet<>())
                .add(group);
        }

        RepositorySaver.saveGroupsByName(groupsByName);
        RepositorySaver.saveGroupsByUser(groupsByUser);
    }

    @Override
    public Set<Group> getUserTwoPersonGroups(String username) {
        return filterGroupsUserIsIn(username, group -> group.type() == GroupType.FRIEND);
    }

    @Override
    public Set<Group> getUserMultiPersonGroups(String username) {
        return filterGroupsUserIsIn(username, group -> group.type() == GroupType.GROUP);
    }

    @Override
    public Set<Group> getUserGroups(String username) {
        return filterGroupsUserIsIn(username, _ -> true);
    }

    @Override
    public Group getGroup(String groupName, String username) throws ExpenseSplitException {
        Set<Group> set = filterGroupsUserIsIn(username, _ -> true);
        set.retainAll(filterGroupsByName(groupName, _ -> true));

        if (set.size() != UNIQUE_GROUPS_CNT) {
            throw new ExpenseSplitException("Given username or group name is invalid! No such group exists!");
        }

        return set.iterator().next();
    }

    @Override
    public Group getGroup(String... usernames) throws ExpenseSplitException {
        Set<String> group = new HashSet<>(Arrays.asList(usernames));

        return getGroup(Group.getFriendshipGroupName(group), group.iterator().next());
    }
}
