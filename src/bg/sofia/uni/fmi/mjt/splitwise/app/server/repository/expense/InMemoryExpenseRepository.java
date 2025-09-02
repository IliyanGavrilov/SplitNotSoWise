package bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.expense;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.expense.Expense;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositorySaver;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InMemoryExpenseRepository implements ExpenseRepository {
    private final Map<String, List<Expense>> expenses; // For history of payments
    // Outer key is paidBy, inner is payers
    private final Map<String, Map<String, BigDecimal>> amountsOwed;
    private static ExpenseRepository instance;

    private Map<String, BigDecimal> filterStatus(String username, Predicate<BigDecimal> predicate) {
        return amountsOwed.getOrDefault(username, new HashMap<>()).entrySet().stream()
            .filter(entry -> predicate.test(entry.getValue()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private InMemoryExpenseRepository() {
        expenses = RepositorySaver.loadExpenses();
        amountsOwed = RepositorySaver.loadBalances();
    }

    public static ExpenseRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryExpenseRepository();
        }

        return instance;
    }

    @Override
    public void addUser(String username) {
        if (!expenses.containsKey(username) && !amountsOwed.containsKey(username)) {
            expenses.put(username, new LinkedList<>());
            amountsOwed.put(username, new HashMap<>());

            RepositorySaver.saveExpenses(expenses);
            RepositorySaver.saveBalances(amountsOwed);
        }
    }

    @Override
    public void addExpense(Expense expense) {
        String credited = expense.getCredited();
        BigDecimal amount = expense.getAmountPerPerson();

        addUser(credited);
        expenses.get(credited).add(expense);

        for (String debited : expense.getDebited(credited)) {
            addUser(debited);
            // The person who paid sees how much the others owe him
            amountsOwed.get(credited)
                .put(debited, amountsOwed.get(credited).getOrDefault(debited, BigDecimal.ZERO).add(amount));

            // The person who owes money can see how much they owe to the person that paid
            amountsOwed.get(debited)
                .put(credited, amountsOwed.get(debited).getOrDefault(credited, BigDecimal.ZERO).subtract(amount));
        }

        RepositorySaver.saveExpenses(expenses);
        RepositorySaver.saveBalances(amountsOwed);
    }

    @Override
    public void pay(Expense expense, String debited) {
        String credited = expense.getCredited();
        BigDecimal amount = expense.getTotalAmount();

        addUser(credited);
        addUser(debited);

        amountsOwed.get(credited)
            .put(debited, amountsOwed.get(credited).getOrDefault(debited, BigDecimal.ZERO).subtract(amount));
        amountsOwed.get(debited)
            .put(credited, amountsOwed.get(debited).getOrDefault(credited, BigDecimal.ZERO).add(amount));

        // If we want to save pay backs to history
        expenses.get(credited).add(expense);

        RepositorySaver.saveExpenses(expenses);
        RepositorySaver.saveBalances(amountsOwed);
    }

    @Override
    public BigDecimal getDebtStatus(String credited, String debited) {
        addUser(credited);
        addUser(debited);

        return amountsOwed.getOrDefault(credited, new HashMap<>()).getOrDefault(debited, BigDecimal.ZERO);
    }

    @Override
    public Map<String, BigDecimal> getStatus(String username) {
        return filterStatus(username, _ -> true);
    }

    @Override
    public Map<String, BigDecimal> getStatusCreditsOnly(String username) {
        return filterStatus(username, val -> val.compareTo(BigDecimal.ZERO) > 0);
    }

    @Override
    public Map<String, BigDecimal> getStatusEvenOnly(String username) {
        return filterStatus(username, val -> val.abs().compareTo(BigDecimal.ZERO) == 0);
    }

    @Override
    public Map<String, BigDecimal> getStatusDebtsOnly(String username) {
        return filterStatus(username, val -> val.compareTo(BigDecimal.ZERO) < 0);
    }

    @Override
    public List<Expense> getHistory(String username) {
        return expenses.getOrDefault(username, new LinkedList<>());
    }
}
