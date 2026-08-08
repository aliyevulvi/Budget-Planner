package aliyew;

import java.util.Scanner;

public class BudgetService {
	
	public static void createNewRecord(Scanner console) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Record Name : ");
		String recordName = console.next();
		
		Record newRecord = new Record(recordName);
		
		newRecord.getRecordInfo();
		
		
		
	}
}