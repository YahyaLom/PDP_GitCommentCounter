package git;
import java.io.File;
import java.io.IOException;
import java.util.Random;
public class GitCloner 
{
	 public static File cloneRepository(String gitUrl) {
	        // Jar dosyasının bulunduğu dizini alın
	        String jarPath = GitCloner.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	        File jarFile = new File(jarPath);
	        String parentDirectory = jarFile.getParent();
	        int randomNumber = generateRandomNumber();

	        // Klonlama işlemini gerçekleştir
	        try {
	            // Klonlama hedef dizini
	            File targetDir = new File(parentDirectory + File.separator + "cloned_repository_yahya_"+randomNumber);

	            // Git komutunu çalıştırarak deposu klonla
	            ProcessBuilder processBuilder = new ProcessBuilder("git", "clone", gitUrl, targetDir.getAbsolutePath());
	            Process process = processBuilder.start();
	            
	            // Klonlama işlemi tamamlanana kadar bekleyin
	            int exitCode = process.waitFor();
	            
	            // Klonlama işlemi başarılı mı kontrol edin
	            if (exitCode == 0) {
	                System.out.println("Git deposu başarıyla klonlandı: " + targetDir.getAbsolutePath());
	                return targetDir; // Klonlanan dizinin referansını geri döndür
	            } else {
	                System.err.println("Git deposu klonlanırken bir hata oluştu. Çıkış kodu: " + exitCode);
	            }
	        } catch (IOException | InterruptedException e) {
	            System.err.println("Klonlama işlemi sırasında bir hata oluştu: " + e.getMessage());
	        }
	        
	        return null; // Hata durumunda null döndür
	    }
	 public static int generateRandomNumber() 
	 {
	        Random random = new Random();
	        return random.nextInt(100); // 0 ile 99 arasında rastgele bir sayı üretir
	 }
}


