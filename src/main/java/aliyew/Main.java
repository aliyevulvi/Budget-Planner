package aliyew;

public class Main {

    public static void main(String[] args) {
        DBManager.adminDeleteRecords();
        DBManager.main(args);
        ConsoleUI.startProgram();
    }
}
