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

    public Synchronization(){}
 
    public String getOp() {
        return op;
    }

    public Record getRecord() {
        return record;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setOp(String opStr) {
        this.op = opStr;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }


}