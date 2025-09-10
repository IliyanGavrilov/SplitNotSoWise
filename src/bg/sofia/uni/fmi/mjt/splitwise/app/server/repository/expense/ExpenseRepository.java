package bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.expense;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.expense.Expense;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ExpenseRepository {
    void addUser(String username);

    void addExpense(Expense expense);

    void pay(Expense expense, String debited);

    BigDecimal getDebtStatus(String credited, String debited);

    Map<String, BigDecimal> getStatus(String username);

    Map<String, BigDecimal> getStatusCreditsOnly(String username);

    Map<String, BigDecimal> getStatusEvenOnly(String username);

    Map<String, BigDecimal> getStatusDebtsOnly(String username);

    List<Expense> getHistory(String username);
}
