package aliyew;

import java.time.*;
import java.time.format.*;
import java.util.ArrayList;

public class Validation {
	
	
	public static boolean isValidDate(String input) {
		try {
			LocalDate.parse(input, DateTimeFormatter.ofPattern("[dd.MM.yy][yyyy-MM-dd][d.M.yy][dd.M.yy][d.MM.yy]"));
			return true;
		} catch (Exception a) {
			return false;
		}
	}
	
	public static boolean isValidAmount(String input) {
	    if (input.matches("-?\\d+(\\.\\d+)?")) {
	        return true;
	    } else {
	        return false;
	    }
	}
	
	public static boolean isValidName(String name, ArrayList<Record> allRecords) {
	    for (Record rec : allRecords) {
	        if (rec.getRecordName().equals(name)) {
	            return false;
	        }
	    }
	    
	    return true;
	}
}