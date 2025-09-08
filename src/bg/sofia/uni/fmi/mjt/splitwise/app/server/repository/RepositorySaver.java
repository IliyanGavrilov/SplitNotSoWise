package bg.sofia.uni.fmi.mjt.splitwise.app.server.repository;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.expense.Expense;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.Group;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.util.json.GsonSingleton;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RepositorySaver {
    private static final int FILES_CNT = RepositoryPath.getFilesCount();

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(FILES_CNT);

    private static <K, V> void saveData(RepositoryPath repositoryPath, Map<K, V> data) {
        String filePath = repositoryPath.getPath();

        synchronized (filePath.intern()) {
            try (FileWriter writer = new FileWriter(filePath)) {
                GsonSingleton.getInstance().toJson(data, writer);
            } catch (IOException e) {
                throw new IllegalStateException(String.format("Could not save data to %s!", filePath), e);
            }
        }
    }

    private static <K, V> Map<K, V> loadData(RepositoryPath repositoryPath, TypeToken<Map<K, V>> typeToken) {
        try (FileReader reader = new FileReader(repositoryPath.getPath())) {
            Map<K, V> data = GsonSingleton.getInstance().fromJson(reader, typeToken.getType());

            return (data != null) ? data : new HashMap<>();
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

    public static <K, V> void saveUsers(Map<K, V> users) {
        EXECUTOR_SERVICE.submit(() -> saveData(RepositoryPath.USERS, users));
    }

    public static <K, V> void saveGroupsByName(Map<K, V> groups) {
        EXECUTOR_SERVICE.submit(() -> saveData(RepositoryPath.GROUPS_BY_NAME, groups));
    }

    public static <K, V> void saveGroupsByUser(Map<K, V> groups) {
        EXECUTOR_SERVICE.submit(() -> saveData(RepositoryPath.GROUPS_BY_USER, groups));
    }

    public static <K, V> void saveExpenses(Map<K, V> expenses) {
        EXECUTOR_SERVICE.submit(() -> saveData(RepositoryPath.EXPENSES, expenses));
    }

    public static <K, V> void saveBalances(Map<K, V> balances) {
        EXECUTOR_SERVICE.submit(() -> saveData(RepositoryPath.BALANCES, balances));
    }

    public static <K, V> void saveNotifications(Map<K, V> notifications) {
        EXECUTOR_SERVICE.submit(() -> saveData(RepositoryPath.NOTIFICATIONS, notifications));
    }

    public static Map<String, User> loadUsers() {
        return loadData(RepositoryPath.USERS, new TypeToken<>() {
        });
    }

    public static Map<String, Set<Group>> loadGroupsByName() {
        return loadData(RepositoryPath.GROUPS_BY_NAME, new TypeToken<>() {
        });
    }

    public static Map<String, Set<Group>> loadGroupsByUser() {
        return loadData(RepositoryPath.GROUPS_BY_USER, new TypeToken<>() {
        });
    }

    public static Map<String, List<Expense>> loadExpenses() {
        return loadData(RepositoryPath.EXPENSES, new TypeToken<>() {
        });
    }

    public static Map<String, Map<String, BigDecimal>> loadBalances() {
        return loadData(RepositoryPath.BALANCES, new TypeToken<>() {
        });
    }

    public static Map<String, Map<String, Queue<String>>> loadNotifications() {
        return loadData(RepositoryPath.NOTIFICATIONS, new TypeToken<>() {
        });
    }
}
