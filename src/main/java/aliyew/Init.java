package aliyew;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Init {

	public static void init() {
	    JsonManager.createCat("Home");
	    JsonManager.createCat("Bills");
	    JsonManager.createCat("Other");
	    
	}
	
	
}