package aliyew;

import java.util.ArrayList;

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
    
    public boolean retryOp() {
        
        switch (op) {
            case "CREATE_RECORD" : 
                return createRecord();
            case "UPDATE_RECORD" : 
                return updateRecord();
            case "DELETE_RECORD" : 
                return deleteRecord();
            case "CREATE_EXPENSE" : 
                return createExpense();
            case "UPDATE_EXPENSE" : 
                return updateExpense();
            case "DELETE_EXPENSE" : 
                return deleteExpense();
            default : return false;
        }
    }
    
    public boolean createRecord() {
        Record oldRecord = new Record();
        oldRecord.setRecordId(record.getRecordId());
        oldRecord.setRecordName(record.getRecordName());
        
        DBManager.createRecord(record);
        
        if (record.getRecordSync()) {
            JsonManager.deleteRecord(oldRecord);
            JsonManager.createRecord(record);
            
            ArrayList<Expense> allExpenses = JsonManager.getExpenses(oldRecord);
            
            for (Expense exp : allExpenses) {
                exp.setExpenseRecordId(record.getRecordId());
                JsonManager.updateExpense(exp);
            }
            
            ArrayList<Synchronization> allOps = JsonManager.getOps();
            
            for (Synchronization sync : allOps) {
                if (sync.getRecord() != null && sync.getRecord().getRecordId() == oldRecord.getRecordId()) {
                    sync.getRecord().setRecordId(record.getRecordId());
                }
            }
            
            JsonManager.setAllOps(allOps);
            
            return true;
        } else {
            return false;
        }
    }
    
    public boolean updateRecord() {
        DBManager.updateRecord(record);
        
        if (record.getRecordSync()) {
            JsonManager.updateRecord(record);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean deleteRecord() {
        DBManager.deleteRecord(record);
        
        if (record.getRecordSync()) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean createExpense() {
        Expense oldExpense = new Expense();
        oldExpense.setExpenseId(expense.getExpenseId());
        
        DBManager.createExpense(expense);
        
        if (expense.getExpenseSync()) {
            JsonManager.deleteExpense(oldExpense);
            JsonManager.createExpense(expense);
            
            ArrayList<Synchronization> allOps = JsonManager.getOps();
            
            for (Synchronization sync : allOps) {
                if (sync.getExpense() != null) {
                    sync.getExpense().setExpenseId(expense.getExpenseId());
                }
            }
            
            JsonManager.setAllOps(allOps);
            
            return true;
        } else {
            return false;
        }
            
    }
    
    public boolean updateExpense() {
        DBManager.updateExpense(expense);
        
        if (expense.getExpenseSync()) {
            JsonManager.updateExpense(expense);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean deleteExpense() {
        DBManager.deleteExpense(expense);
        
        if (expense.getExpenseSync()) {
            return true;
        } else {
            return false;
        }
    }


}