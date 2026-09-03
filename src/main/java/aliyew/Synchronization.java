package aliyew;

public class Synchronization {
    private String op = "undefined";
    private Record record = null;
    private Expense expense = null;

    public Synchronization(String operation, Record rec, Expense exp) {
        this.op = operation;
        this.record = rec;
        this.expense = exp;

        JsonManager.addOp(this);
    }


}