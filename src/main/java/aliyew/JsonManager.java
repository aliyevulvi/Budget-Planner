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
	private static final File SYNC_FILE = new File("src/main/resources/jsons/syncQueue.json");
	private static final File CAT_FILE = new File("src/main/resources/jsons/category.json");
	private static final File PAYMENT_TEMPLATES_FILE = new File("src/main/resources/jsons/payment_templates.json");

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
				return "Create Record Failed (" + record.getRecordName() + ") Already Created";
			}
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
			if (allRecords.get(i).getRecordId().equals(record.getRecordId())) {
				allRecords.set(i, record);
			}
		}

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(RECORD_FILE, allRecords);
			return "Update Record Successfully";
		} catch (IOException e) {
			logger.severe(e.getMessage());
			return "Update Record Failed";
		}
	}

	public static String deleteRecord(Record record) {
		ArrayList<Record> allRecords = getRecords();

        allRecords.removeIf(rec -> rec.getRecordId().equals(record.getRecordId()));
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
				if (exp.getExpenseRecordId().equals(record.getRecordId())) {
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
			if (allExpenses.get(i).getExpenseId().equals(expense.getExpenseId())) {
				allExpenses.set(i, expense);
				break;
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

		allExpenses.removeIf(exp -> exp.getExpenseId().equals(expense.getExpenseId()));

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

	public static ArrayList<Synchronization> getOps() {
		try {
			return mapper.readValue(SYNC_FILE, new TypeReference<ArrayList<Synchronization>>() {});


		} catch (IOException e) {
			logger.severe(e.getMessage());

			return new ArrayList<Synchronization>();
		}

	}

	public static void addOp(Synchronization op) {
		ArrayList<Synchronization> allOps = getOps();

		if (allOps == null) {
			return;
		}

		allOps.add(op);

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(SYNC_FILE, allOps);
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}

	}

	public static void setAllOps(ArrayList<Synchronization> arr) {
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(SYNC_FILE, arr);
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}

	public static void deleteLastOp() {
		ArrayList<Synchronization> allOps = getOps();

		if (allOps == null || allOps.isEmpty()) {
			return;
		}

		allOps.remove(allOps.size() - 1);

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(SYNC_FILE, allOps);
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}

	public static ArrayList<String> getCats() {
		ArrayList<String> allCats = new ArrayList<>();

		try {

			if (!CAT_FILE.exists()) {
				CAT_FILE.createNewFile();
				return allCats;
			}
			return mapper.readValue(CAT_FILE, new TypeReference<ArrayList<String>>() {});


		} catch (IOException e) {
			logger.severe(e.getMessage());
			allCats.clear();
			return allCats;
		}
	}

	public static void createCat(String newCat) {
		ArrayList<String> allCats = getCats();
		
		if (allCats.contains(newCat)) {
		    return;
		}

		allCats.add(newCat);

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(CAT_FILE, allCats);
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}

	public static void deleteCat(String cat) {
		ArrayList<String> allCats = getCats();
		allCats.removeIf(e -> e.equals(cat));

		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(CAT_FILE, allCats);
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}
	
	public static ArrayList<PT> getPTs() {
		ArrayList<PT> allPTs = new ArrayList<>();

		try {

			if (!PAYMENT_TEMPLATES_FILE.exists()) {
				PAYMENT_TEMPLATES_FILE.createNewFile();
				return allPTs;
			}
			return mapper.readValue(PAYMENT_TEMPLATES_FILE, new TypeReference<ArrayList<PT>>() {});


		} catch (IOException e) {
			logger.severe(e.getMessage());
			allPTs.clear();
			return allPTs;
		}
	}
	
	public static void createPT(PT pt) {
		ArrayList<PT> allPTs = getPTs();

		for (PT pt1 : allPTs) {
		    if (pt.getPTCat().equals(pt1.getPTCat()) && pt.getPTAmt() == pt1.getPTAmt()) {
		        return;
		    }
		}
		
		allPTs.add(pt);

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(PAYMENT_TEMPLATES_FILE, allPTs);
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}
	
	public static void deletePT(PT pt) {
		ArrayList<PT> allPTs = getPTs();
		allPTs.removeIf(e -> (e.getPTCat().equals(pt.getPTCat()) && e.getPTAmt() == pt.getPTAmt()));

		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(PAYMENT_TEMPLATES_FILE, allPTs);
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}

}