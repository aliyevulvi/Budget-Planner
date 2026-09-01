package aliyew;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


public class JsonManager {
    private static final Logger logger = LogManager.getLogger(JsonManager.class.getName());
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final File RECORD_FILE = new File("src/main/resources/jsons/records.json");
    private static final File EXPENSE_FILE = new File("src/main/resources/jsons/expenses.json");
    private static final File FAILES_FILE = new File("src/main/resources/jsons/failedOps.json");

    public static ArrayList<Record> getRecords() {
        ArrayList<Record> allRecords = new ArrayList<>();
        try {
            return mapper.readValue(RECORD_FILE, new TypeReference<ArrayList<Record>>() {});


        } catch (IOException e) {
            logger.severe(e.getMessage());
            allRecords.clear();
            return allRecords;
        }
    }

    public static String createRecord(Record record) {
        ArrayList<Record> allRecords = getRecords();
        
        for (Record rec : allRecords) {
            if (rec.getRecordName().equals(record.getRecordName())) {
                return "Create Record Failed ("+record.getRecordName() + ") Already Created";
            }
        }

        if (!record.getRecordSync()) {
            record.setRecordId(getMinIdRecord());
        }

        allRecords.add(record);

        try {
            
            mapper.writerWithDefaultPrettyPrinter().writeValue(RECORD_FILE, allRecords);
            return "Create Record Successfully";
        } catch (IOException e) {
            logger.severe(e.getMessage());
            return "Create Record Failed";
        }
    }

    public static String updateRecord(Record record) {
        ArrayList<Record> allRecords = getRecords();

        for (int i = 0; i < allRecords.size(); i++) {
            if (allRecords.get(i).getRecordId() == record.getRecordId()) {
                allRecords.set(i, record);
            }
        }

        try {
            
            mapper.writerWithDefaultPrettyPrinter().writeValue(RECORD_FILE, allRecords);
            return "Update Updated Successfully";
        } catch (IOException e) {
            logger.severe(e.getMessage());
            return "Update Record Failed";
        }
    }

    public static String deleteRecord(Record record) {
        ArrayList<Record> allRecords = getRecords();

        for (Record rec : allRecords) {
            if (rec.getRecordName().equals(record.getRecordName())) {
                allRecords.remove(rec);
                break;
            }
        }

        deleteExpense(record);

        try {
            
            mapper.writerWithDefaultPrettyPrinter().writeValue(RECORD_FILE, allRecords);
            return "Delete Record Successfully";
        } catch (IOException e) {
            logger.severe(e.getMessage());
            return "Delete Record Failed";
        }
    }

    public static ArrayList<Expense> getExpenses() {
        ArrayList<Expense> allExpenses = new ArrayList<>();
        try {
            return mapper.readValue(EXPENSE_FILE, new TypeReference<ArrayList<Expense>>() {});


        } catch (IOException e) {
            logger.severe(e.getMessage());
            allExpenses.clear();
            return allExpenses;
        }
    }

    public static ArrayList<Expense> getExpenses(Record record) {
        ArrayList<Expense> allExpenses = new ArrayList<>();
        try {
            allExpenses = mapper.readValue(EXPENSE_FILE, new TypeReference<ArrayList<Expense>>() {});
            ArrayList<Expense> recordExpenses = new ArrayList<>();

            for (Expense exp : allExpenses) {
                if (exp.getExpenseRecordId() == record.getRecordId()) {
                    recordExpenses.add(exp);
                }
            }

            return recordExpenses;
            


        } catch (IOException e) {
            logger.severe(e.getMessage());
            allExpenses.clear();
            return allExpenses;
        }
    }

    public static String createExpense(Expense expense) {
        ArrayList<Expense> allExpenses = getExpenses();

        if (!expense.getExpenseSync()) {
            expense.setExpenseId(getMinIdExpense());
        }

        allExpenses.add(expense);

        try {
            
            mapper.writerWithDefaultPrettyPrinter().writeValue(EXPENSE_FILE, allExpenses);
            return "Create Expense Successfully";
        } catch (IOException e) {
            logger.severe(e.getMessage());
            return "Create Expense Failed";
        }
    }

    public static String updateExpense(Expense expense) {
        ArrayList<Expense> allExpenses = getExpenses();
        

        for (int i = 0; i < allExpenses.size(); i++) {
            if (allExpenses.get(i).getExpenseId() == expense.getExpenseId()) {
                allExpenses.set(i, expense);
            }
        }

        try {
            
            mapper.writerWithDefaultPrettyPrinter().writeValue(EXPENSE_FILE, allExpenses);
            return "Update Expense Successfully";
        } catch (IOException e) {
            logger.severe(e.getMessage());
            return "Update Expense Failed";
        }
    }

    public static String deleteExpense(Expense expense) {
        ArrayList<Expense> allExpenses = getExpenses();

        for (Expense exp : allExpenses) {
            if (exp.getExpenseId() == expense.getExpenseId()) {
                allExpenses.remove(exp);
                break;
            }
        }

        try {
            
            mapper.writerWithDefaultPrettyPrinter().writeValue(EXPENSE_FILE, allExpenses);
            return "Delete Expense Successfully";
        } catch (IOException e) {
            logger.severe(e.getMessage());
            return "Delete Expense Failed";
        }
    }

    public static String deleteExpense(Record rec) {
        ArrayList<Expense> allExpenses = getExpenses();

       allExpenses.removeIf(exp -> exp.getExpenseRecordId() == rec.getRecordId());

        try {
            
            mapper.writerWithDefaultPrettyPrinter().writeValue(EXPENSE_FILE, allExpenses);
            return "Delete Expense Successfully";
        } catch (IOException e) {
            logger.severe(e.getMessage());
            return "Delete Expense Failed";
        }
    }

    public static int getMinIdRecord() {
        ArrayList<Record> allRecords = getRecords();
        int minId = -1;

        for (Record rec: allRecords) {
            if (rec.getRecordId() <= minId) {
                minId--;
            }
        }

        return minId;
    }

    public static int getMinIdExpense() {
        ArrayList<Expense> allExpenses = getExpenses();
        int minId = -1;

        for (Expense exp : allExpenses) {
            if (exp.getExpenseId() <= minId) {
                minId--;
            }
        }

        return minId;
    }

    public static ArrayList<FailedSync> getFailedOps() {
        ArrayList<FailedSync> allFailedOps = new ArrayList<>();

        try {
            if (!FAILES_FILE.exists()) {
                FAILES_FILE.createNewFile();
            }

            allFailedOps = mapper.readValue(FAILES_FILE, new TypeReference<ArrayList<FailedSync>>() {});
            return allFailedOps;
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return null;
        }


    }

    public static void syncOp(FailedSync failedOp) {
        ArrayList<FailedSync> allFailedOps = getFailedOps();
        allFailedOps.add(failedOp);

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(FAILES_FILE, allFailedOps);

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
    

}