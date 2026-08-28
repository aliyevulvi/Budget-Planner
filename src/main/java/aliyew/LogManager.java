package aliyew;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogManager {
    
    public LogManager(String className) {
        
    }

    public static Logger getLogger(String className) {
        File file = new File("src/main/resources/logs/"+className+".log");
        Logger logger = Logger.getLogger(className);
        logger.setUseParentHandlers(false);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileHandler fileHandler = new FileHandler("src/main/resources/logs/"+className+".log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);

            
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }


        return logger;
    }

}