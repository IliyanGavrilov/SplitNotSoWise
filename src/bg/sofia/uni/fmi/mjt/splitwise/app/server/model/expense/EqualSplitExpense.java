package bg.sofia.uni.fmi.mjt.splitwise.app.server.model.expense;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.Group;

import java.math.BigDecimal;

public class EqualSplitExpense extends Expense {
    public EqualSplitExpense(BigDecimal totalAmount, String paidBy, Group debited, String reasonForPayment) {
        super(totalAmount, paidBy, debited, reasonForPayment);
    }
}
