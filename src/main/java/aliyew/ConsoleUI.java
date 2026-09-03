package aliyew;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Logger;

public class ConsoleUI {
    private static final Logger logger = LogManager.getLogger(ConsoleUI.class.getName());

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
				createRecord();
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
		ArrayList<Record> allRecords = JsonManager.getRecords();
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
				ArrayList<Expense> allExpenses = showExpenses(rec);

				System.out.printf("[ %-20s ]\n", "--------------------");
				System.out.printf("[ %-17s (1)]\n", "Insert Expense");
				System.out.printf("[ %-17s (2)]\n", "Delete Expense");
				System.out.printf("[ %-17s (3)]\n", "Update Expense");
				System.out.printf("[ %-17s (4)]\n", "Update Record");
				System.out.printf("[ %-17s (5)]\n", "Show Report");
				System.out.printf("[ %-17s (6)]\n", "Delete Record");
				System.out.printf("[ %-17s (7)]\n", "Synchronization");
				System.out.printf("[ %-17s (0)]\n", "Quit");
				System.out.printf("[ %-20s ]\n", "--------------------");
				System.out.print("[ Select ] : ");

				input = console.nextLine();

				switch (input) {
				case "1":
					createExpense(rec, allExpenses);
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
					showReport(rec);
					break;
				case "6":
					deleteRecord(rec);
					return;
				case "7":
					sync(rec);
					break;
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
		ArrayList<Expense> allExpenses = JsonManager.getExpenses(rec);
		Scanner console = new Scanner(System.in);
		double totalExpense = 0;
		String[] cats = {"Air", "Home", "Loan", "Self", "Utility", "Others"};

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
		System.out.printf("[ %-35s ]\n", "Total Expense : " + totalExpense);
		System.out.printf("[ %-35s ]\n", "Remaining Salary :" + (rec.getRecordIncome() + totalExpense));
		System.out.printf("[ %-35s ]\n[ %-35s ]\n", "-".repeat(35), "-".repeat(35));

		HashMap<String, Double> categorizedMap = new HashMap<>();

		System.out.printf("[ %-35s ]\n", "<< Category Based Expense Ratio >>");
		for (String str : cats) {
			double catExpense = 0;
			for (Expense exp : allExpenses) {
				if (exp.getExpenseCat().equalsIgnoreCase(str)) {
					catExpense += exp.getExpenseAmt();
				}
			}
			catExpense = Math.round(catExpense * 100.0) / 100.0;
			System.out.printf("[ %-35s ]\n", str + " : " + catExpense);
			categorizedMap.put(str, catExpense);
		}

		System.out.printf("[ %-35s ]\n\n", "-".repeat(35));

		System.out.printf("[ %-31s %-3s ]\n", "Create PDF", "(1)");
		System.out.printf("[ %-31s %-3s ]\n", "Quit", "(0)");

		System.out.print("[ Select ]  : ");
		String input = console.nextLine();

		if (input.equals("1")) {
			System.out.println("[ " + PDFManager.createPdf(new Report(rec, allExpenses, categorizedMap)) + " ]");
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
			}
		} else if (input.equals("0")) {
			return;
		} else {
			System.out.println("[ Wrong Input ]");
			try {
				Thread.sleep(1000);
			} catch (Exception e) {}
			showReport(rec);
		}



	}

	private static void createRecord() {
		Scanner console = new Scanner(System.in);
		String newRecordName = "";
		String newRecordTotaIncome = "";
		String newRecordSaving = "";

		System.out.print("\033[H\033[2J");
		System.out.flush();
		System.out.printf("[ %-20s ]\n", "-----NEW RECORD-----");
		System.out.print("[ Record Name : ");
		newRecordName = console.nextLine();
		System.out.print("[ Total Income : ");
		newRecordTotaIncome = console.nextLine();
		System.out.print("[ Total Saving : ");
		newRecordSaving = console.nextLine();
		System.out.printf("[ %-20s ]\n\n", "--------------------");

		if (newRecordTotaIncome.matches("-?\\d+(\\.\\d+)?") && newRecordSaving.matches("-?\\d+(\\.\\d+)?")) {
			Record rec = new Record(newRecordName, Double.parseDouble(newRecordTotaIncome), Double.parseDouble(newRecordSaving));
			System.out.printf("[ %-20s ]\n", DBManager.createRecord(rec) + " on DB");

			if (!rec.getRecordSync()) {
				rec.setRecordId(JsonManager.getMinIdRecord());
				new Synchronization("CREATE_RECORD", rec, null);
			}

            String response = JsonManager.createRecord(rec);
			System.out.printf("[ %-20s ]\n", response + " on Local");
			if (response.contains("Failed")) {
			         JsonManager.deleteLastOp();
			}
            
			


		} else {
			System.out.println("[ Wrong Input ]");
		}

		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}

	}

	public static ArrayList<Expense> showExpenses(Record rec) {
		ArrayList<Expense> allExpenses = JsonManager.getExpenses(rec);

		if (allExpenses.isEmpty()) {
			System.out.printf("[ %-20s ]\n", "--------------------");
			System.err.println("[ NO EXPENSE ]");
			System.out.printf("[ %-20s ]\n\n", "--------------------");

			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
			return allExpenses;
		}
		System.out.println("\n[  ----------------------------- Expenses -----------------------------  ]");
		System.out.println("[ ______________________________________________________________________ ]");
		System.out.printf("|[ %9s ] [ %15s ] [ %10s ] [ %9s ] [ %5s ]|\n", "ID", "DATE", "CATEGORY", "AMOUNT", "SYNC");
		for (Expense exp : allExpenses) {
			System.out.println(exp.toString());
		}
		System.out.println("[ ---------------------------------------------------------------------- ]\n");

		return allExpenses;
	}

	public static void createExpense(Record rec, ArrayList<Expense> allExpenses) {
		Scanner console = new Scanner(System.in);
		Expense lastExpense = null;

		System.out.printf("[ %-20s ]\n", "--------------------");
		if (allExpenses.isEmpty()) {
			System.out.print("[ Expense Date (dd.mm.yy): ");
		} else {
			lastExpense = Expense.getLastExpense(allExpenses);
			System.out.print("[ Expense Date (" + lastExpense.getExpenseDate() + ") : ");
		}
		String expenseDate = console.nextLine();

		if (expenseDate.equals("") && lastExpense != null) {
			expenseDate = lastExpense.getExpenseDate() + "";
		} else if (expenseDate.equals(">") && lastExpense != null) {
			expenseDate = lastExpense.getExpenseDate().plusDays(1) + "";
		} else if (expenseDate.matches(">-?\\d++") && lastExpense != null) {
			expenseDate = expenseDate.substring(1);
			expenseDate = lastExpense.getExpenseDate().plusDays(Integer.parseInt(expenseDate)) + "";
		}


		if (isValidDate(expenseDate)) {
			System.out.printf("[ %-20s ]\n", "Categories : Air (1), Home (2), Loan (3), Self (4), Utiliy (5), Others (6)");
			System.out.print("[ Expense Category : ");

			String cat = console.nextLine();

			cat = cat.equals("1") ? "air" : (cat.equals("2") ? "home" : (cat.equals("3") ? "loan" : (cat.equals("4") ? "self" : (cat.equals("5") ? "utility" : (cat.equals("6") ? "others" : "")))));

			if (cat.equals("air") || cat.equals("home") || cat.equals("loan") || cat.equals("self") || cat.equals("utility") || cat.equals("others")) {
				System.out.print("[ Input Expense Amount : ");
				String amt = console.nextLine();
				System.out.printf("[ %-20s ]\n\n", "--------------------");

				if (amt.matches("-?\\d+(\\.\\d+)?")) {
					Expense exp = new Expense(LocalDate.parse(expenseDate, DateTimeFormatter.ofPattern("[dd.MM.yy][yyyy-MM-dd][d.M.yy][dd.M.yy][d.MM.yy]")), cat, Double.parseDouble(amt));
					exp.setExpenseRecordId(rec.getRecordId());
					exp.setExpenseId(JsonManager.getMinIdExpense());

					if (rec.getRecordSync()) {
						System.out.println("[ " + DBManager.createExpense(exp) + " on DB]");
						if (!exp.getExpenseSync()) {
							new Synchronization("CREATE_EXPENSE", null, exp);
						}
					} else {
							new Synchronization("CREATE_EXPENSE", null, exp);
					}

					System.out.println("[ " + JsonManager.createExpense(exp) + " on Local]");
					



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

	public static boolean isValidDate(String input) {
		try {
			LocalDate.parse(input, DateTimeFormatter.ofPattern("[dd.MM.yy][yyyy-MM-dd][d.M.yy][dd.M.yy][d.MM.yy]"));
			return true;
		} catch (Exception a) {
			return false;
		}


	}

	public static void deleteExpense(Record rec) {
		ArrayList<Expense> allExpenses = JsonManager.getExpenses(rec);

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
			System.out.println("[ " + JsonManager.deleteExpense(expense) + " on Local]");

			if (expense.getExpenseSync()) {
				expense.setExpenseSync(false);
				System.out.println("[ " + DBManager.deleteExpense(expense) + " on DB]");
				if (!expense.getExpenseSync()) {
					new Synchronization("DELETE_EXPENSE", null, expense);
				}
			}

			try {
				Thread.sleep(2000);
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

		ArrayList<Record> allRecords = JsonManager.getRecords();

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

		if (!income.matches("-?\\d+(\\.\\d+)?")) {
			System.out.println("[ Wrong Input ]");

			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}

			return;
		}

		System.out.print("[ Record Saving  (" + rec.getRecordSaving() + ") : ");
		String saving = console.nextLine();

		if (saving.equals("")) {
			saving = rec.getRecordSaving() + "";
		}

		if (!saving.matches("-?\\d+(\\.\\d+)?")) {
			System.out.println("[ Wrong Input ]");

			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}

			return;
		}

		rec.setRecordName(name);
		rec.setRecordIncome(Double.parseDouble(income));
		rec.setRecordSaving(Double.parseDouble(saving));

		if (rec.getRecordSync()) {
			rec.setRecordSync(false);
			System.out.println("[ " + DBManager.updateRecord(rec) + " on DB]");
			if (!rec.getRecordSync()) {
				new Synchronization("UPDATE_RECORD", rec, null);
			}
		}

		System.out.println("[ " + JsonManager.updateRecord(rec) + " on Local]");


		try {
			Thread.sleep(2000);
		} catch (Exception e) {
		}
	}

	public static void updateExpense(Record rec) {
		ArrayList<Expense> allExpenses = JsonManager.getExpenses(rec);

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
			Expense lastExpense = null;

			System.out.printf("[ %-20s ]\n", "--------------------");
			if (allExpenses.isEmpty()) {
				System.out.print("[ Expense Date (dd.mm.yy): ");
			} else {
				lastExpense = Expense.getLastExpense(allExpenses);
				System.out.print("[ Expense Date (" + lastExpense.getExpenseDate() + ") : ");
			}
			String expenseDate = console.nextLine();

			if (expenseDate.equals("") && lastExpense != null) {
				expenseDate = lastExpense.getExpenseDate() + "";
			} else if (expenseDate.equals(">") && lastExpense != null) {
				expenseDate = lastExpense.getExpenseDate().plusDays(1) + "";
			} else if (expenseDate.matches(">-?\\d++") && lastExpense != null) {
				expenseDate = expenseDate.substring(1);
				expenseDate = lastExpense.getExpenseDate().plusDays(Integer.parseInt(expenseDate)) + "";
			}

			if (!isValidDate(expenseDate)) {
				return;
			}

			System.out.print("[ Category (" + expense.getExpenseCat() + ") : ");
			String cat = console.nextLine();
			if (cat.equals("")) {
				cat = expense.getExpenseCat();
			}
			cat = cat.equals("1") ? "air" : (cat.equals("2") ? "home" : (cat.equals("3") ? "loan" : (cat.equals("4") ? "self" : (cat.equals("5") ? "utility" : (cat.equals("6") ? "others" : "")))));


			if (cat.equals("air") || cat.equals("home") || cat.equals("loan") || cat.equals("self") || cat.equals("utility") || cat.equals("others") || cat.equals("")) {
				if (cat.equals("")) {
					cat = expense.getExpenseCat();
				}
				System.out.print("[ Amount (" + expense.getExpenseAmt() + ") : ");
				String amount = console.nextLine();
				if (amount.equals("") || amount.matches("-?\\d+(\\.\\d+)?")) {
					if (amount.equals("")) {
						amount = expense.getExpenseAmt() + "";
					}
					Expense exp = new Expense(expense.getExpenseId(), expense.getExpenseRecordId(), LocalDate.parse(expenseDate, DateTimeFormatter.ofPattern("[dd.MM.yy][yyyy-MM-dd][d.M.yy][dd.M.yy][d.MM.yy]")), cat,
											  Double.parseDouble(amount));
					if (expense.getExpenseSync() && rec.getRecordSync()) {
						System.out.println("[ " + DBManager.updateExpense(exp) + " on DB]");
						if (!exp.getExpenseSync()) {
							new Synchronization("UPDATE_EXPENSE", rec, exp);
						}
					}
					System.out.println("[ " + JsonManager.updateExpense(exp) + " on Local]");

					try {
						Thread.sleep(2000);
					} catch (Exception e) {
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
			if (rec.getRecordSync()) {
				rec.setRecordSync(false);
				System.out.println("[ " + DBManager.deleteRecord(rec) + " on DB]");
				if (!rec.getRecordSync()) {
					new Synchronization("DELETE_RECORD", rec, null);
				}
			}
			System.out.println("[ " + JsonManager.deleteRecord(rec) + " on Local]");

			try {
				Thread.sleep(2000);
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

	public static void sync(Record record) {
		
	}
	
	
}
