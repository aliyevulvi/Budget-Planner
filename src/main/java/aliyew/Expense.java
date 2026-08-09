package aliyew;

import java.time.LocalDate;

public class Expense {
	private int expense_id = 0;
	private LocalDate expense_date = null;
	private String expense_cat = "undefined";
	private int expense_amt = 0;
	
	public Expense(int id, LocalDate date, String cat, int amount) {
	    this.expense_id = id;
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
	
	public LocalDate getExpenseDate() {
	    return this.expense_date;
	}
	
	public String getExpenseCat() {
	    return this.expense_cat;
	}
	
	public int getExpenseAmt() {
	    return this.expense_amt;
	}
}