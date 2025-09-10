package bg.sofia.uni.fmi.mjt.splitwise.app.server.model.expense;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExpenseTest {
    private BigDecimal amount;
    private String paidBy;
    private Group group;
    private String reason;
    private Expense expense;

    @BeforeEach
    void setUp() {
        amount = BigDecimal.valueOf(100);
        paidBy = "user1";
        group = mock();
        reason = "Dinner";
        expense = new Expense(amount, paidBy, group, reason);
    }

    @Test
    void testGetAmountPerPerson() {
        when(group.size()).thenReturn(4);
        BigDecimal expected =
            amount.divide(BigDecimal.valueOf(4), Expense.BIG_DECIMAL_SCALE, java.math.RoundingMode.HALF_EVEN);
        Expense e = new Expense(amount, paidBy, group, reason);
        assertEquals(expected, e.getAmountPerPerson(),
            "Amount per person should be total amount divided by group size");
    }

    @Test
    void testGetDebited() {
        Set<String> expected = Set.of("user2", "user3");
        when(group.usersExcept("user1")).thenReturn(expected);
        assertEquals(expected, expense.getDebited("user1"), "Debited users should exclude the credited one");
    }

    @Test
    void testGetGroup() {
        Set<String> expected = Set.of("user1", "user2", "user3");
        when(group.users()).thenReturn(expected);
        assertEquals(expected, expense.getGroup(), "Group should match the one returned from the debited group");
    }

    @Test
    void testToStringContainsAmount() {
        String result = expense.toString();
        assertTrue(result.contains(amount.toString()), "toString should contain the total amount");
    }

    @Test
    void testToStringContainsReason() {
        String result = expense.toString();
        assertTrue(result.contains(reason), "toString should contain the reason for payment");
    }
}