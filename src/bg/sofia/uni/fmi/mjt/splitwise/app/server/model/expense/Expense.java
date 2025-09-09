package bg.sofia.uni.fmi.mjt.splitwise.app.server.model.expense;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.Group;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

public class Expense {
    private final String credited; // The person who paid the expense
    private final String reasonForPayment;
    protected final BigDecimal totalAmount;
    protected final Group debited; // The people who owe the person who paid
    public static final int BIG_DECIMAL_SCALE = 96;

    public Expense(BigDecimal totalAmount, String paidBy, Group debited, String reasonForPayment) {
        this.totalAmount = totalAmount;
        this.credited = paidBy;
        this.debited = debited;
        this.reasonForPayment = reasonForPayment;
    }

    public String getCredited() {
        return credited;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getAmountPerPerson() {
        return totalAmount.divide(BigDecimal.valueOf(debited.size()), BIG_DECIMAL_SCALE, RoundingMode.HALF_EVEN);
    }

    public Set<String> getDebited(String username) {
        return debited.usersExcept(username);
    }

    public Set<String> getGroup() {
        return debited.users();
    }

    @Override
    public String toString() {
        return "Expense{" +
            ", totalAmount = " + totalAmount +
            ", debited = " + debited +
            ", reasonForPayment = '" + reasonForPayment + '\'' +
            '}';
    }
}
