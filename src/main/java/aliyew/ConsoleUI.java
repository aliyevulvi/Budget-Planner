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
			System.out.printf("[ %-16s (3)]\n", "Get Report");
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
			case "3":
				System.out.println("3");
				break outerWhile;
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
				System.out.printf("[ %-20s ]\n", "--------------------");
				System.out.printf("[ %-17s (1)]\n", "Insert Expense");
				System.out.printf("[ %-17s (2)]\n", "Delete Expense");
				System.out.printf("[ %-17s (3)]\n", "Update Expense");
				System.out.printf("[ %-17s (4)]\n", "Upd. Rec. Name");
				System.out.printf("[ %-17s (5)]\n", "Update Income");
				System.out.printf("[ %-17s (5)]\n", "Delete Record");
				System.out.printf("[ %-17s (0)]\n", "Quit");
				System.out.printf("[ %-20s ]\n", "--------------------");
				System.out.print("[ Select ] : ");

				input = console.nextLine();

				switch (input) {
				case "1":
					System.out.println("Insert");
					createNewExpense(rec);
					break;
				case "2":
					System.out.println("Delete");
					break;
				case "3":
					System.out.println("Update");
					break;
				case "4":
					System.out.println("Update");
					break;
				case "5":
					System.out.println("Update");
					break;
				case "6":
					System.out.println("Delete Record");
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

		if (newRecordTotaIncome.matches("\\d+")) {
			System.out.printf("[ %-20s ]", DBManager.createNewRecord(new Record(newRecordName, Integer.parseInt(newRecordTotaIncome))));

		} else {
			System.out.println("[ Wrong Input ]");
		}

		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}

	}

	public static void createNewExpense(Record rec) {
		Scanner console = new Scanner(System.in);

		System.out.printf("[ %-20s ]\n", "--------------------");
		System.out.printf("[ %-20s ", "Expense Year : ");
		String year = console.nextLine();
		System.out.printf("[ %-20s ", "Expense Month : ");
		String month = console.nextLine();
		System.out.printf("[ %-20s ", "Expense Day : ");
		String day = console.nextLine();

		if (year.matches("\\d+") && month.matches("\\d+") && day.matches("\\d+")) {
			System.out.printf("[ %-20s ]\n", "Categories : Air, Home, Loan, Self, Utiliy, Others, Income");
			System.out.printf("[ %-20s ", " Select Cat. : ");
			String cat = console.nextLine();

			if (cat.equals("air") || cat.equals("home") || cat.equals("loan") || cat.equals("self") || cat.equals("utility") || cat.equals("others") || cat.equals("income")) {
				System.out.print("[ Input Expense Amount : ");
				String amt = console.nextLine();

				if (amt.matches("\\d+")) {
					System.out.println("[ "+DBManager.createExpense(rec, new Expense(java.time.LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day)), cat, Integer.parseInt(amt)))
				+" ]");
				
				    try {
				        
				        Thread.sleep(2000);
				    } catch (Exception e) {
				        
				    }
				} else {
					System.out.printf("[ %-20s ]\n", "Wrong Input");
					try {
						Thread.sleep(1000);
					} catch (Exception e) {}
					return;
				}
			} else {
				System.out.printf("[ %-20s ]\n", "Wrong Input");
				try {
					Thread.sleep(1000);
				} catch (Exception e) {}
				return;
			}
		} else {
			System.out.printf("[ %-20s ]\n", "Wrong Input");
			try {
				Thread.sleep(1000);
			} catch (Exception e) {}
			return;
		}
	}
}
