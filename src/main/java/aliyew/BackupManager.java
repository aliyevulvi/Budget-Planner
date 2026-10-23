package aliyew;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.*;
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
		String zipFilePath = BACKUP_DIR + "/budgetplanner_backup_" + timeStamp + ".zip";

		logger.info("Backup is starting: " + zipFilePath);

		try (FileOutputStream fos = new FileOutputStream(zipFilePath);
					ZipOutputStream zos = new ZipOutputStream(fos)) {

			for (String filePath : SOURCE_FILES) {
				File fileToZip = new File(filePath);

				if (!fileToZip.exists()) {
					logger.warning("Backup File Not Found! : " + filePath);
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
					logger.info("File Backup Completed : " + fileToZip.getName());
				}
			}

			logger.info("Backup Finished Successfully : " + zipFilePath);
			return "Backup Successful : " + zipFilePath;

		} catch (IOException e) {
			logger.severe("Error Occured while Backup : " + e);
			return "Backup Failed: " + e.getMessage();
		}
	}

	public static String restoreBackup(String zipFilePath) {
		File destDir = new File("src/main/resources/jsons");
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		boolean found = false;

		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
			ZipEntry zipEntry = zis.getNextEntry();

			while (zipEntry != null) {
				File newFile = new File(destDir, zipEntry.getName());

				String destDirPath = destDir.getCanonicalPath();
				String destFilePath = newFile.getCanonicalPath();

				if (!destFilePath.startsWith(destDirPath + File.separator)) {
					throw new IOException("ZIPSlip Error : " + zipEntry.getName());
				}


				try (FileOutputStream fos = new FileOutputStream(newFile)) {
					byte[] buffer = new byte[1024];
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
				}

				
				zipEntry = zis.getNextEntry();
			}

			zis.closeEntry();
			return "Restore Successful!";

		} catch (IOException e) {
			e.printStackTrace();
			return "Restore Failed! " + e.getMessage();
		}
	}

}