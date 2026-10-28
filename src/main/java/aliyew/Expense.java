package aliyew;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class Expense {

	private String expenseId = "undefined";
	private String expenseRecordId = "undefined";
	private LocalDate expenseDate = null;
	private String expenseCat = "undefined";
	private double expenseAmt = 0;
	private boolean expenseSync = false;

	public Expense(String id, String record_id, LocalDate date, String cat, double amount) {
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
		
		String uuidStr = UUID.randomUUID().toString();
		
		ArrayList<Expense> allExpenses = JsonManager.getExpenses();
        
        for (int i = 0; i < allExpenses.size(); i++) {
            if (allExpenses.get(i).getExpenseId().equals(uuidStr)) {
                uuidStr = UUID.randomUUID().toString();
                i = -1;
            }
        }
        
        this.expenseId = uuidStr;
	}

	public Expense() {

	}

	public Expense(String id) {
		this.expenseId = id;
	}

	public String getExpenseId() {
		return this.expenseId;
	}

	public String getExpenseRecordId() {
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

	public void setExpenseId(String id) {
		this.expenseId = id;
	}

	public void setExpenseSync(boolean bool) {
		this.expenseSync = bool;
	}

	public void setExpenseRecordId(String id) {
		this.expenseRecordId = id;
	}

	public void setExpenseDate(LocalDate ld) {
		this.expenseDate = ld;
	}

	public void setExpenseCat(String cat) {
		this.expenseCat = cat;
	}

	public void setExpenseAmt(Double amt) {
		this.expenseAmt = amt;
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


}
