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

    public void retryOp() {

        switch (this.failedOp) {
            case "createRecord" : createRecord();
                break;
            case "updateRecord" : updateRecord();
                break;
            case "deleteRecord" : deleteRecord();
                break;
            case "createExpense" : createExpense();
                break;
            case "updateExpense" : updateExpense();
                break;
            case "deleteExpense" : deleteExpense();
                break;
        }
    }
    
    public void createRecord() {
        Record record = new Record(recordName, recordIncome, recordSaving);
        DBManager.createRecord(record);
        
        if (record.getRecordSync()) {
            ArrayList<Expense> allExpenses = JsonManager.getExpenses(new Record(recordId, null, null));
            
            for (Expense exp : allExpenses) {
                exp.setExpenseRecordId(record.getRecordId());
                JsonManager.updateExpense(exp);
            }
            
            ArrayList<Record> allRecords = JsonManager.getRecords();
            
            for (Record rec : allRecords) {
                if (rec.getRecordId() == recordId) {
                    JsonManager.deleteRecord(rec);
                    break;
                }
            }
            
            JsonManager.createRecord(record);
                     
            this.failedOp = "solved";
        }
    }
    
    public void updateRecord() {
        Record record = new Record(recordName, recordIncome, recordSaving);
        DBManager.updateRecord(record, recordName, recordIncome, recordSaving);
        
        if (record.getRecordSync()) {
            JsonManager.updateRecord(record);
            this.failedOp = "solved";
        }
    }
    
    public void deleteRecord() {
        Record record = new Record(recordName, recordIncome, recordSaving);
        DBManager.deleteRecord(record);
        
        if (record.getRecordSync()) {
            this.failedOp = "solved";
        }
    }
    
    public void createExpense() {
        Expense expense = new Expense(expenseDate, expenseCat, expenseAmt);
        Record record = new Record(recordName, recordIncome, recordSaving);
        DBManager.createExpense(record, expense);
        
        if (expense.getExpenseSync()) {
            ArrayList<Expense> allExpenses = JsonManager.getExpenses(record);
            
            for (Expense exp : allExpenses) {
                if (exp.getExpenseId() == expenseId) {
                    JsonManager.deleteExpense(exp);
                    break;
                }
            }
            
            
            
            this.failedOp = "solved";
        }
    }
    
    

}