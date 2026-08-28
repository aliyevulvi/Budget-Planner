package aliyew;

import java.time.LocalDate;
import java.util.ArrayList;

public class Expense {

    private int expenseId = 0;
    private int expenseRecordId = 0;
    private LocalDate expenseDate = null;
    private String expenseCat = "undefined";
    private double expenseAmt = 0;
    private boolean expenseSync = false;

    public Expense(int id, int record_id, LocalDate date, String cat, double amount) {
        this.expenseId = id;
        this.expenseRecordId = record_id;
        this.expenseDate = date;
        this.expenseCat = cat;
        this.expenseAmt = amount;
    }

    public Expense(LocalDate date, String cat, double amount) {
        this.expenseDate = date;
        this.expenseCat = cat;
        this.expenseAmt = amount;
    }

    public Expense() {

    }

    public int getExpenseId() {
        return this.expenseId;
    }

    public int getExpenseRecordId() {
        return this.expenseRecordId;
    }

    public LocalDate getExpenseDate() {
        return this.expenseDate;
    }

    public String getExpenseCat() {
        return this.expenseCat;
    }

    public double getExpenseAmt() {
        return this.expenseAmt;
    }

    public boolean getExpenseSync() {
        return this.expenseSync;
    }

    public void setExpenseId(int id) {
        this.expenseId = id;
    }

    public void setExpenseSync(boolean bool) {
        this.expenseSync = bool;
    }

    public void setExpenseRecordId(int id) {
        this.expenseRecordId = id;
    }

    public static Expense getLastExpense(ArrayList<Expense> allExpenses) {
        Expense lastExpense = allExpenses.get(0);

        for (Expense expense : allExpenses) {
            if (expense.getExpenseDate().isAfter(lastExpense.getExpenseDate())) {
                lastExpense = expense;
            }
        }

        return lastExpense;
    }

    @Override
    public String toString() {
        return "|[ %9d ] [ %15s ] [ %10s ] [ %9.2f ] [ %5s ]|".formatted(expenseId, expenseDate.toString(), expenseCat, expenseAmt, expenseSync);

    }

   
}
