package aliyew;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.logging.Logger;

public class BackupManager {
    
    private static final Logger logger = LogManager.getLogger(BackupManager.class.getName());
    
    private static final String BACKUP_DIR = "backups";
    
    private static final String[] SOURCE_FILES = {
        "src/main/resources/jsons/records.json",
        "src/main/resources/jsons/expenses.json"
    };

    public static String createBackup() {

        File dir = new File(BACKUP_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String zipFilePath = BACKUP_DIR + "/backup_" + timeStamp + ".zip";

        logger.info("Backup is starting: "+zipFilePath);

        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (String filePath : SOURCE_FILES) {
                File fileToZip = new File(filePath);
                
                if (!fileToZip.exists()) {
                    logger.warning("Backup File Not Found! : "+filePath);
                    continue;
                }

                try (FileInputStream fis = new FileInputStream(fileToZip)) {
                    ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                    zos.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) >= 0) {
                        zos.write(buffer, 0, length);
                    }

                    zos.closeEntry();
                    logger.info("File Backup Completed : "+fileToZip.getName());
                }
            }

            logger.info("Backup Finished Successfully : "+zipFilePath);
            return "Backup Successful : " + zipFilePath;

        } catch (IOException e) {
            logger.severe("Error Occured while Backup : "+e);
            return "Backup Failed: " + e.getMessage();
        }
    }
}