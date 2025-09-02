package bg.sofia.uni.fmi.mjt.splitwise.app.server.model.expense;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.Group;

import java.math.BigDecimal;
import java.util.Set;

public abstract class Expense {
    private final String credited; // The person who paid the expense
    private final String reasonForPayment;
    protected final BigDecimal totalAmount;
    protected final Group debited; // The people who owe the person who paid

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

    public abstract BigDecimal getAmountPerPerson();

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
