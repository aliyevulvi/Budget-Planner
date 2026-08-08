package aliyew;

import java.util.Scanner;
import java.util.ArrayList;

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

			case "1" :
				BudgetService.createNewRecord(console);
				break;
			case "2" :
				System.out.println("2");
				break outerWhile;
			case "3" :
				System.out.println("3");
				break outerWhile;
			case "0" :
				System.out.println("[ BudgetPlanner Closed ]");
				System.exit(0);
			default :
				try {
					System.out.print("\033[H\033[2J");
					System.out.flush();
					System.out.println("Wrong Selection");
					Thread.sleep(1000);
				} catch (Exception e) {}
				break;
			}
		}
	}

	public static void showRecords() {
		ArrayList<Record> allRecords = DBManager.getRecords();
		Scanner console = new Scanner(System.in);
		String input = "";

		if (allRecords.size() == 0) {
			System.out.printf("[ %-20s ]\n", "--------------------");
			System.out.println("[ No Records ]");
			System.out.printf("[ %-20s ]\n", "--------------------");
		} else {
			System.out.printf("[ %-20s ]\n", "--------------------");
			for (Record rc : allRecords) {
				rc.getRecordInfo();
			}
			System.out.printf("[ %-20s ]\n", "--------------------");
			System.out.printf("[ %-18s %1s ]\n", "Select Record (0 for quit", ":");
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
			} catch (Exception e) {}

			System.out.print("\033[H\033[2J");
			System.out.flush();

			rec.getRecordInfo();
			System.out.println();

			while (true) {
				System.out.printf("[ %-20s ]\n", "------------------------");
				System.out.printf("[ %-17s (1)]\n", "Insert Expense");
				System.out.printf("[ %-17s (2)]\n", "Delete Expense");
				System.out.printf("[ %-17s (3)]\n", "Update Expense");
				System.out.printf("[ %-17s (0)]\n", "Quit");
				System.out.printf("[ %-20s ]\n", "------------------------");
				System.out.printf("[ %-18s : ] ", "Select Option");

				input = console.nextLine();

				switch (input) {
				case "1" :
					System.out.println("Insert");
					break;
				case "2" :
					System.out.println("Delete");
					break;
				case "3" :
					System.out.println("Update");
					break;
				case "0" :
					System.out.println("Quit");
					return;
				default : System.out.println("[ Wrong Selection ]");
				}


			}


		}


	}
}