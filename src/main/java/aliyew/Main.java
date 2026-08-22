package aliyew;

public class Main {

    public static void main(String[] args) {
        
        // DBManager.main(args);
        // ConsoleUI.startProgram();

        JsonManager.writeValueRecordList(DBManager.getRecords());
        JsonManager.writeValueExpenseList(DBManager.getExpenses());

    }
}
