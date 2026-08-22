package aliyew;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonManager {
    private static ObjectMapper mapper = new ObjectMapper();
    

    public static String writeValue(Record record) {
        ArrayList<Record> allRecords = readValueRecords();
        allRecords.add(record);

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/records.json"), allRecords);
            return "New Record Saved";
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return e.getMessage() + " writeValue Method";

        }
    }

    public static String writeValueRecordList(ArrayList<Record> records) {

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/records.json"), records);
            return "New Record Saved";
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return e.getMessage() + " writeValue Method";

        }
    }

    public static String updateRecord(Record record) {
        ArrayList<Record> allRecords = readValueRecords();

        for (Record rec : allRecords) {
            if (rec.getRecordId() == record.getRecordId()) {
                rec = record;
            }
        }
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/records.json"), allRecords);
            return "Record Updated";
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return e.getMessage() + " updateRecord Method";
        }
    }

    public static String deleteRecord(int recordId) {
        ArrayList<Record> allRecords = readValueRecords();

        for (Record rec : allRecords) {
            if (rec.getRecordId() == recordId){
                allRecords.remove(rec);
                break;
            }
        }

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/records.json"), allRecords);
            return "Record Deleted";
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return e.getMessage() + " deleteRecord Method";
        }
        
    }

    public static ArrayList<Record> readValueRecords() {
        ArrayList<Record> allRecords = new ArrayList<>();
        File file = new File("src/main/resources/records.json");

        if (file.length() == 0) {
            return allRecords;
        }

        try {
            return mapper.readValue(file,
                    new TypeReference<ArrayList<Record>>() {
                    });

        } catch (IOException e) {
            System.out.println(e.getMessage());
            allRecords.clear();
            System.out.println("JsonManager.(readValueRecords) : " + e.getMessage() );
            return allRecords;

        }
    }

    public static String writeValue(Expense expense) {
        mapper.registerModule(new JavaTimeModule());
        ArrayList<Expense> allExpenses = readValueExpenses();
        allExpenses.add(expense);

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/expenses.json"), allExpenses);
            return "New Expense Saved";
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return e.getMessage() + " writeValue Method";

        }
    }

    public static String writeValueExpenseList(ArrayList<Expense> expenses) {
        mapper.registerModule(new JavaTimeModule());

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/expenses.json"), expenses);
            return "New Expense Saved";
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getMessage() + " writeValue Method");
            return e.getMessage() + " writeValue Method";

        }
    }

    public static String updateExpense(Expense expense) {
        ArrayList<Expense> allExpenses = readValueExpenses();

        for (Expense exp : allExpenses) {
            if (exp.getExpenseId() == expense.getExpenseId()) {
                exp = expense;
            }
        }
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/records.json"), allExpenses);
            return "Record Updated";
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return e.getMessage() + " updateRecord Method";
        }
    }

    public static String deleteExpense(int expenseId) {
        ArrayList<Expense> allExpenses = readValueExpenses();

        for (Expense exp : allExpenses) {
            if (exp.getExpenseId() == expenseId){
                allExpenses.remove(exp);
                break;
            }
        }

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/expenses.json"), allExpenses);
            return "Expense Deleted";
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return e.getMessage() + " deleteExpense Method";
        }
        
    }

    public static ArrayList<Expense> readValueExpenses() {
        mapper.registerModule(new JavaTimeModule());
        ArrayList<Expense> allExpenses = new ArrayList<>();
        File file = new File("src/main/resources/expenses.json");
        if (file.length() == 0) {
            return allExpenses;
        }

        try {
            
            return mapper.readValue(file, new TypeReference<ArrayList<Expense>>() {});

        } catch (IOException e) {
            System.out.println(e.getMessage());
            allExpenses.clear();
            return allExpenses;

        }
    }


}
