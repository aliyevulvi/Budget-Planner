package aliyew;

import java.time.*;
import java.time.format.*;

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
}