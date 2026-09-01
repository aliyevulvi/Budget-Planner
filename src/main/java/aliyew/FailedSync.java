package aliyew;

import java.time.LocalDate;

public class FailedSync {
    private String failedOp = "undefined";
    private int recordId = 0;
    private String recordName = "undefined";
    private String creationDate = "undefined";
    private double recordIncome = 0;
    private double recordSaving = 0;
    private boolean recordSync = false;
    private int expenseId = 0;
    private int expenseRecordId = 0;
    private LocalDate expenseDate = null;
    private String expenseCat = "undefined";
    private double expenseAmt = 0;
    private boolean expenseSync = false;

    public FailedSync(String op, Record record, Expense expense) {
        this.failedOp = op;

        if (record != null) {
            this.recordId = record.getRecordId();
            this.recordName = record.getRecordName();
            this.creationDate = record.getCreationDate();
            this.recordIncome = record.getRecordIncome();
            this.recordSaving = record.getRecordSaving();
            this.recordSync = record.getRecordSync();
        }

        if (expense != null) {
            this.expenseId = expense.getExpenseId();
            this.expenseRecordId = expense.getExpenseRecordId();
            this.expenseDate = expense.getExpenseDate();
            this.expenseCat = expense.getExpenseCat();
            this.expenseAmt = expense.getExpenseAmt();
            this.expenseSync = expense.getExpenseSync();
        }
    }

    public String retryOp() {

        switch (this.failedOp) {
            
        }

        return "Success";
    }

}