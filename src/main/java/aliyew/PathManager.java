package aliyew;

import java.io.File;
import java.net.URISyntaxException;

public class PathManager {

    /**
     * Uygulamanın çalışma dizinini (ana klasörünü) dinamik olarak belirler.
     */
    public static String getAppDirectory() {
        try {
            // Çalıştırılabilen ana kaynağın (JAR veya sınıf dosyası) konumunu alır
            File path = new File(PathManager.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            
            // Eğer uygulama paketlenmiş bir .jar dosyası olarak çalıştırıldıysa:
            if (path.isFile()) {
                // JAR dosyasının fiziksel olarak bulunduğu klasörün yolunu döndürür
                return path.getParentFile().getAbsolutePath();
            }
            
            // Eğer doğrudan IDE içinden veya Maven (mvn exec) üzerinden çalıştırılıyorsa:
            // Projenin kök dizinini (pom.xml'in olduğu klasör) döndürür
            return System.getProperty("user.dir");
            
        } catch (URISyntaxException e) {
            e.printStackTrace();
            // Bir hata oluşursa güvenli yedek olarak sistemin o anki dizinini verir
            return System.getProperty("user.dir");
        }
    }

    /**
     * Verilerin saklanacağı 'data' klasörünün yolunu verir ve yoksa otomatik oluşturur.
     */
    public static File getDataDirectory() {
        File dataDir = new File(getAppDirectory(), "data");
        if (!dataDir.exists()) {
            dataDir.mkdir(); // Klasör yoksa oluştur
        }
        return dataDir;
    }
}
