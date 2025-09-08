package bg.sofia.uni.fmi.mjt.splitwise.app.server.model.expense;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.Group;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class EqualSplitExpense extends Expense {
    public EqualSplitExpense(BigDecimal totalAmount, String paidBy, Group debited, String reasonForPayment) {
        super(totalAmount, paidBy, debited, reasonForPayment);
    }

    @Override
    public BigDecimal getAmountPerPerson() {
        return totalAmount.divide(BigDecimal.valueOf(debited.size()), BIG_DECIMAL_SCALE, RoundingMode.HALF_UP);
    }
}
