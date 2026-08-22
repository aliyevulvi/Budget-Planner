package aliyew;

import java.time.LocalDate;
import java.util.ArrayList;

public class Expense {

    private int expense_id = 0;
    private int expense_record_id = 0;
    private LocalDate expense_date = null;
    private String expense_cat = "undefined";
    private double expense_amt = 0;
    private boolean isSync = false;

    public Expense(int id, int record_id, LocalDate date, String cat, double amount) {
        this.expense_id = id;
        this.expense_record_id = record_id;
        this.expense_date = date;
        this.expense_cat = cat;
        this.expense_amt = amount;
    }

    public Expense(LocalDate date, String cat, double amount) {
        this.expense_date = date;
        this.expense_cat = cat;
        this.expense_amt = amount;
    }

    public Expense() {

    }

    public int getExpenseId() {
        return this.expense_id;
    }

    public int getExpenseRecordId() {
        return this.expense_record_id;
    }

    public LocalDate getExpenseDate() {
        return this.expense_date;
    }

    public String getExpenseCat() {
        return this.expense_cat;
    }

    public double getExpenseAmt() {
        return this.expense_amt;
    }

    public void setSynced() {
        this.isSync = true;
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
        return "|[ %9d ] [ %15s ] [ %10s ] [ %9.2f ]|".formatted(expense_id, expense_date.toString(), expense_cat, expense_amt);

    }

   
}
