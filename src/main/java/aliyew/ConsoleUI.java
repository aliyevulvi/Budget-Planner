package aliyew;

import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleUI {

    public static void main(String args[]) {

    }

    public static void startProgram() {

        Scanner console = new Scanner(System.in);

        outerWhile:
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            System.out.printf("[ Budget Planner Menu ]\n");
            System.out.printf("[ %-16s (1)]\n", "Create Record");
            System.out.printf("[ %-16s (2)]\n", "Show Records");
            System.out.printf("[ %-16s (0)]\n", "Quit");
            System.out.printf("[ %-15s ]\n", "-------------------");
            System.out.print("\n[ Select ] : ");

            String input = console.next();

            switch (input) {

                case "1":
                    createNewRecord();
                    break;
                case "2":
                    showRecords();
                    break;
                case "0":
                    System.out.println("[ BudgetPlanner Closed ]");
                    System.exit(0);
                default:
                    try {
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        System.out.println("[ Wrong Selection ]");
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                    break;
            }
        }
    }

    public static void showRecords() {
        ArrayList<Record> allRecords = DBManager.getRecords();
        Scanner console = new Scanner(System.in);
        String input = "";

        if (allRecords.isEmpty()) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.printf("[ %-20s ]\n", "--------------------");
            System.out.println("[ No Records ]");
            System.out.printf("[ %-20s ]\n", "--------------------");

            try {
                Thread.sleep(2000);
            } catch (Exception e) {
            }
        } else {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.printf("[ %-35s ]\n", " ---------- ALL RECORDS ---------- ");
            for (Record rc : allRecords) {
                rc.getRecordInfo();
            }
            System.out.println("[ ----------------------------------- ]");

            System.out.print("[ Select Record (0 for quit) ] : ");
            input = console.nextLine();

            if (input.equals("0")) {
                return;
            }

            boolean recordFound = false;
            Record rec = null;

            for (Record rc : allRecords) {
                if (input.equals((rc.getRecordId() + ""))) {
                    recordFound = true;
                    rec = rc;
                }
            }

            if (!recordFound) {
                System.out.printf("[ %-16s ]\n", "Such Record couldn't found");
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
                return;
            }

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }

            while (true) {
                System.out.print("\033[H\033[2J");
                System.out.flush();

                rec.getRecordInfo();
                System.out.println();
                showExpenses(rec);

                System.out.printf("[ %-20s ]\n", "--------------------");
                System.out.printf("[ %-17s (1)]\n", "Insert Expense");
                System.out.printf("[ %-17s (2)]\n", "Delete Expense");
                System.out.printf("[ %-17s (3)]\n", "Update Expense");
                System.out.printf("[ %-17s (4)]\n", "Update Record");
                System.out.printf("[ %-17s (5)]\n", "Add Extra Income");
                System.out.printf("[ %-17s (6)]\n", "Show Report");
                System.out.printf("[ %-17s (7)]\n", "Delete Record");
                System.out.printf("[ %-17s (0)]\n", "Quit");
                System.out.printf("[ %-20s ]\n", "--------------------");
                System.out.print("[ Select ] : ");

                input = console.nextLine();

                switch (input) {
                    case "1":
                        createNewExpense(rec);
                        break;
                    case "2":
                        deleteExpense(rec);
                        break;
                    case "3":
                        updateExpense(rec);
                        break;
                    case "4":
                        updateRecord(rec);
                        return;
                    case "5":
                        addIncome(rec);
                        return;
                    case "6":
                        showReport(rec);
                        break;
                    case "7":
                        deleteRecord(rec);
                        return;
                    case "0":
                        System.out.println("Quit");
                        return;
                    default:
                        System.out.println("[ Wrong Selection ]");
                }

            }

        }

    }
    
    public static void showReport(Record rec) {
        ArrayList<Expense> allExpenses = DBManager.getExpenses(rec);
        Scanner console = new Scanner(System.in);
        int totalExpense = 0;
        String[] cats = {"Air", "Home", "Loan", "Self", "Utility", "Others", "Income"}; 
        
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.printf("[ %-35s ]\n", "-".repeat(35));
        rec.getRecordInfo();
        System.out.printf("[ %-35s ]\n", "-".repeat(35));
        if (allExpenses.isEmpty()) {
            System.out.printf("[ %-35s ]\n", "NO RECORDS");
        } else {
            for (Expense exp : allExpenses) {
                exp.toString();
                totalExpense += exp.getExpenseAmt();
            }
        }
        
        System.out.printf("[ %-35s ]\n", "<< Result >>");
        System.out.printf("[ %-35s ]\n", "Total Expense : "+totalExpense);
        System.out.printf("[ %-35s ]\n", "Remaining Salary :"+ (rec.getRecordIncome() + totalExpense));
        System.out.printf("[ %-35s ]\n[ %-35s ]\n", "-".repeat(35), "-".repeat(35));
        
        System.out.printf("[ %-35s ]\n", "<< Category Based Expense Ratio >>");
        for (String str : cats) {
            int catExpense = 0;
            for (Expense exp : allExpenses) {
                if (exp.getExpenseCat().equalsIgnoreCase(str)) {
                    catExpense += exp.getExpenseAmt();
                }
            }
            System.out.printf("[ %-35s ]\n", str + " : " + catExpense);
        }
        
        System.out.printf("[ %-35s ]\n\n", "-".repeat(35));
        
        System.out.printf("[ %-31s %-3s ]\n", "Create PDF", "(1)");
        System.out.printf("[ %-31s %-3s ]\n", "Quit", "(0)");
        
        System.out.print("[ Select ]  : ");
        String input = console.nextLine();
        
        if (input.equals("1")) {
            System.out.println("[ "+PDFManager.createPdf(rec, allExpenses)+" ]");
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
            }
        } else if (input.equals("0")) {
            return;
        } else {
            System.out.println("[ Wrong Input ]");
            try {Thread.sleep(1000);} catch (Exception e) {}
            showReport(rec);
        }
        

        
    }

    public static void addIncome(Record rec) {
        Scanner console = new Scanner(System.in);
        System.out.print("[ Add Extra Income (" + rec.getRecordIncome() + ") : ");
        String income = console.nextLine();

        if (income.matches("-?\\d+")) {
            System.out.println("[ " + DBManager.addIncome(rec, Integer.parseInt(income)) + " ]");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        } else {
            System.out.println("[ Wrong Input ]");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
    }

    private static void createNewRecord() {
        Scanner console = new Scanner(System.in);
        String newRecordName = "";
        String newRecordTotaIncome = "";

        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.printf("[ %-20s ]\n", "-----NEW RECORD-----");
        System.out.print("[ Record Name : ");
        newRecordName = console.nextLine();
        System.out.print("[ Total Income : ");
        newRecordTotaIncome = console.nextLine();
        System.out.printf("[ %-20s ]\n\n", "--------------------");

        if (newRecordTotaIncome.matches("-?\\d+")) {
            System.out.printf("[ %-20s ]", DBManager.createNewRecord(new Record(newRecordName, Integer.parseInt(newRecordTotaIncome))));

        } else {
            System.out.println("[ Wrong Input ]");
        }

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }

    }

    public static void showExpenses(Record rec) {
        ArrayList<Expense> allExpenses = DBManager.getExpenses(rec);

        if (allExpenses.isEmpty()) {
            System.out.printf("[ %-20s ]\n", "--------------------");
            System.err.println("[ NO EXPENSE ]");
            System.out.printf("[ %-20s ]\n\n", "--------------------");

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            return;
        }
        System.out.println("\n[  ------------------------ Expenses ------------------------  ]");
        System.out.println("[ ____________________________________________________________ ]");
        System.out.printf("|[ %9s ] [ %15s ] [ %10s ] [ %9s ]|\n", "ID", "DATE", "CATEGORY", "AMOUNT");
        for (Expense exp : allExpenses) {
            System.out.println(exp.toString());
        }
        System.out.println("[ ------------------------------------------------------------ ]\n");
    }

    public static void createNewExpense(Record rec) {
        Scanner console = new Scanner(System.in);

        System.out.printf("[ %-20s ]\n", "--------------------");
        System.out.print("[ Expense Year : ");
        String year = console.nextLine();
        System.out.print("[ Expense Month : ");
        String month = console.nextLine();
        System.out.print("[ Expense Day : ");
        String day = console.nextLine();

        if (year.matches("-?\\d+") && month.matches("-?\\d+") && day.matches("-?\\d+")) {
            System.out.printf("[ %-20s ]\n", "Categories : Air, Home, Loan, Self, Utiliy, Others, Income");
            System.out.print("[ Expense Cat. : ");

            String cat = console.nextLine();

            if (cat.equals("air") || cat.equals("home") || cat.equals("loan") || cat.equals("self") || cat.equals("utility") || cat.equals("others") || cat.equals("income")) {
                System.out.print("[ Input Expense Amount : ");
                String amt = console.nextLine();
                System.out.printf("[ %-20s ]\n\n", "--------------------");

                if (amt.matches("-?\\d+")) {
                    System.out.println("[ " + DBManager.createExpense(rec, new Expense(java.time.LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day)), cat, Integer.parseInt(amt)))
                            + " ]");

                    try {

                        Thread.sleep(2000);
                    } catch (Exception e) {

                    }
                } else {
                    System.out.printf("[ %-20s ]\n", "Wrong Input");
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                    return;
                }
            } else {
                System.out.printf("[ %-20s ]\n", "Wrong Input");
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
                return;
            }
        } else {
            System.out.printf("[ %-20s ]\n", "Wrong Input");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            return;
        }
    }

    public static void deleteExpense(Record rec) {
        ArrayList<Expense> allExpenses = DBManager.getExpenses(rec);

        if (allExpenses.isEmpty()) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.printf("[ %-20s ]\n\n", "--------------------");
            System.err.println("[ NO EXPENSE ]");
            System.out.printf("[ %-20s ]\n\n", "--------------------");

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            return;
        }

        Scanner console = new Scanner(System.in);
        System.out.print("[ Select Expense (0 for quit)] : ");
        String input = console.nextLine();
        Expense expense = null;

        if (input.equals("0")) {
            return;
        }

        for (Expense exp : allExpenses) {
            if (input.equals((exp.getExpenseId() + ""))) {
                expense = exp;
            }
        }

        if (expense != null) {
            System.out.println("[ " + DBManager.deleteExpense(expense.getExpenseId()) + " ]");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        } else {
            System.out.println("[ Wrong Selection ]");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }

    }

    public static void updateRecord(Record rec) {
        Scanner console = new Scanner(System.in);

        System.out.print("[ Record Name (" + rec.getRecordName() + ") : ");
        String name = console.nextLine();

        ArrayList<Record> allRecords = DBManager.getRecords();

        for (Record record : allRecords) {
            if (record.getRecordName().equals(name)) {
                System.out.println("[ Please Input Unique Name ]");

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }

                return;
            }
        }

        if (name.equals("")) {
            name = rec.getRecordName();
        }

        System.out.print("[ Record Income  (" + rec.getRecordIncome() + ") : ");
        String income = console.nextLine();

        if (income.equals("")) {
            income = rec.getRecordIncome() + "";
        }

        if (!income.matches("-?\\d+")) {
            System.out.println("[ Wrong Input ]");

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }

            return;
        }

        System.out.println("[ " + DBManager.updateRecord(rec, name, Integer.parseInt(income)) + " ]");

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
    }

    public static void updateExpense(Record rec) {
        ArrayList<Expense> allExpenses = DBManager.getExpenses(rec);

        if (allExpenses.isEmpty()) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.printf("[ %-20s ]\n\n", "--------------------");
            System.err.println("[ NO EXPENSE ]");
            System.out.printf("[ %-20s ]\n\n", "--------------------");

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            return;
        }

        Scanner console = new Scanner(System.in);
        System.out.print("[ Select Expense (0 for quit)] : ");
        String input = console.nextLine();
        Expense expense = null;

        if (input.equals("0")) {
            return;
        }

        for (Expense exp : allExpenses) {
            if (input.equals((exp.getExpenseId() + ""))) {
                expense = exp;
            }
        }

        if (expense != null) {
            System.out.print("[ Year (" + expense.getExpenseDate().getYear() + ") : ");
            String year = console.nextLine();
            if (year.equals("")) {
                year = expense.getExpenseDate().getYear() + "";
            } else if (!year.matches("-?\\d+")) {
                System.out.println("[ Wrong Input ]");
                try {
                    Thread.sleep(1000);
                    return;
                } catch (Exception e) {
                }
            }
            System.out.print("[ Month (" + expense.getExpenseDate().getMonth() + ") : ");
            String month = console.nextLine();
            if (month.equals("")) {
                month = expense.getExpenseDate().getMonth() + "";
            } else if (!month.matches("-?\\d+")) {
                System.out.println("[ Wrong Input ]");
                try {
                    Thread.sleep(1000);
                    return;
                } catch (Exception e) {
                }
            }
            System.out.print("[ Day (" + expense.getExpenseDate().getDayOfMonth() + ") : ");
            String day = console.nextLine();
            if (day.equals("")) {
                day = expense.getExpenseDate().getDayOfMonth() + "";
            } else if (!day.matches("-?\\d+")) {
                System.out.println("[ Wrong Input ]");
                try {
                    Thread.sleep(1000);
                    return;
                } catch (Exception e) {
                }
            }

            System.out.print("[ Category (" + expense.getExpenseCat() + ") : ");
            String cat = console.nextLine();

            if (cat.equals("air") || cat.equals("home") || cat.equals("loan") || cat.equals("self") || cat.equals("utility") || cat.equals("others") || cat.equals("income") || cat.equals("")) {
                if (cat.equals("")) {
                    cat = expense.getExpenseCat();
                }
                System.out.print("[ Amount (" + expense.getExpenseAmt() + ") : ");
                String amount = console.nextLine();
                if (amount.equals("") || amount.matches("-?\\d+")) {
                    if (amount.equals("")) {
                        amount = expense.getExpenseAmt() + "";
                    }
                    DBManager.updateExpense(new Expense(expense.getExpenseId(),expense.getExpenseRecordId(), java.time.LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day)), cat, Integer.parseInt(amount)));
                } else {
                    System.out.println("[ Wrong Input ]");
                    try {
                        Thread.sleep(1000);
                        return;
                    } catch (Exception e) {
                    }
                }
            } else {
                System.out.println("[ Wrong Input ]");
                try {
                    Thread.sleep(1000);
                    return;
                } catch (Exception e) {
                }
            }

        } else {
            System.out.println("[ Wrong Selection ]");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
    }

    public static void deleteRecord(Record rec) {
        Scanner console = new Scanner(System.in);
        System.out.print("[ Are you sure to delete this record? (y/n) : ");
        String input = console.nextLine();

        if (input.equals("y")) {
            System.out.println("[ " + DBManager.deleteRecord(rec) + " ]");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        } else if (input.equals("n")) {
            return;
		} else {
                System.out.println("[ Wrong Input ]");
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
		}
	}
}
