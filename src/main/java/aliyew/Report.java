package aliyew;

import java.util.ArrayList;
import java.util.HashMap;

public class Report {
    private Record rec = new Record();
    private ArrayList<Expense> allExpenses = new ArrayList<>();
    private Expense highestExpense = new Expense();
    private Expense smallestExpense = new Expense();
    private Expense firstExpense = new Expense();
    private Expense lastExpense = new Expense();
    private int totalExpense = 0;
    private HashMap<String, Integer> categorizedMap = new HashMap<>();

    public Report(Record record, ArrayList<Expense> allExpenses, HashMap<String, Integer> map) {

        this.rec = record;
        this.allExpenses = allExpenses;
        this.categorizedMap = map;
        if (!allExpenses.isEmpty()) {
        this.highestExpense = getHighestExpense(this.allExpenses);
        this.smallestExpense = getSmallestExpense(this.allExpenses);
        this.firstExpense = getFirstExpense(this.allExpenses);
        this.lastExpense = getLastExpense(this.allExpenses);
        this.totalExpense = getTotalExpense(this.allExpenses);
        }

    }

     
    public Expense getHighestExpense() {
        return this.highestExpense;
    }
    public Expense getSmallestExpense() {
        return this.smallestExpense;
    }
    public Expense getFirstExpense() {
        return this.firstExpense;
    }
    public Expense getLastExpense() {
        return this.lastExpense;
    }
    public int getTotalExpense() {
        return this.totalExpense;
    }
    public Record getRecord() {
        return this.rec;
    }
    public ArrayList<Expense> getAllExpenses() {
        return this.allExpenses;
    }
    public HashMap<String, Integer> getCategorizedMap() {
        return this.categorizedMap;
    }


    public Expense getHighestExpense(ArrayList<Expense> allExpenses) {
        Expense highest = allExpenses.get(0);

        for (Expense exp : allExpenses) {
            if (exp.getExpenseAmt() < highest.getExpenseAmt()) {
                highest = exp;
            }
        }

        return highest;
    }

    public Expense getSmallestExpense(ArrayList<Expense> allExpenses) {
        Expense smallest = allExpenses.get(0);

        for (Expense exp : allExpenses) {
            if (exp.getExpenseAmt() > smallest.getExpenseAmt()) {
                smallest = exp;
            }
        }
        return smallest;
    }

    public Expense getFirstExpense(ArrayList<Expense> allExpenses) {
        Expense first = allExpenses.get(0);

        for (Expense exp : allExpenses) {
            if (exp.getExpenseDate().isBefore(first.getExpenseDate())) {
                first = exp;
            }
        }
        
        return first;
    }

    public Expense getLastExpense(ArrayList<Expense> allExpenses) {
        Expense last = allExpenses.get(0);

        for (Expense exp : allExpenses) {
            if (exp.getExpenseDate().isAfter(last.getExpenseDate())) {
                last = exp;
            }
        }
        
        return last;
    }
    public int getTotalExpense(ArrayList<Expense> allExpenses) {
        int total = 0;

        for (Expense exp : allExpenses) {
            total += exp.getExpenseAmt();
        }
        
        return total;
    }
}
