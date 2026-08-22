package aliyew;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonManager {
    

    public static String writeValue(Record record) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Record> allRecords = readValueRecords();
        allRecords.add(record);

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/records.json"), allRecords);
            return "New Record Saved";
        } catch (IOException e) {
            return e.getMessage() + " writeValue Method";

        }
    }

    public static String writeValueRecordList(ArrayList<Record> records) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/records.json"), records);
            return "New Record Saved";
        } catch (IOException e) {
            return e.getMessage() + " writeValue Method";

        }
    }

    public static String writeValue(Expense expense) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ArrayList<Expense> allExpenses = readValueExpenses();
        allExpenses.add(expense);

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/expenses.json"), allExpenses);
            return "New Expense Saved";
        } catch (IOException e) {
            return e.getMessage() + " writeValue Method";

        }
    }

    public static String writeValueExpenseList(ArrayList<Expense> expenses) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/expenses.json"), expenses);
            return "New Expense Saved";
        } catch (IOException e) {
            System.out.println(e.getMessage() + " writeValue Method");
            return e.getMessage() + " writeValue Method";

        }
    }


    public static ArrayList<Record> readValueRecords() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Record> allRecords = new ArrayList<>();

        try {
            return mapper.readValue(new File("src/main/resources/records.json"), new TypeReference<ArrayList<Record>>() {});

        } catch (IOException e) {
            allRecords.clear();
            return allRecords;

        }
    }
    public static ArrayList<Expense> readValueExpenses() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Expense> allExpenses = new ArrayList<>();

        try {
            
            return mapper.readValue(new File("src/main/resources/expenses.json"), new TypeReference<ArrayList<Expense>>() {});

        } catch (IOException e) {
            allExpenses.clear();
            return allExpenses;

        }
    }


}
