package aliyew;

import java.time.LocalDate;

public class Expense {

    private int expense_id = 0;
    private int expense_record_id = 0;
    private LocalDate expense_date = null;
    private String expense_cat = "undefined";
    private int expense_amt = 0;

    public Expense(int id, int record_id, LocalDate date, String cat, int amount) {
        this.expense_id = id;
        this.expense_record_id = record_id;
        this.expense_date = date;
        this.expense_cat = cat;
        this.expense_amt = amount;
    }

    public Expense(LocalDate date, String cat, int amount) {
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

    public int getExpenseAmt() {
        return this.expense_amt;
    }

    @Override
    public String toString() {
        return "|[ %9d ] [ %15s ] [ %10s ] [ %9d ]|".formatted(expense_id, expense_date.toString(), expense_cat, expense_amt);

    }
}
