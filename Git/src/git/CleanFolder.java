package git;

import java.io.File;
import java.util.Scanner;

public class CleanFolder {
	 public static void cleanFolder(String folderPath) {
	        File folder = new File(folderPath);
	        
	        // Eğer klasör mevcut değilse veya bir klasör değilse işlemi sonlandır
	        if (!folder.exists() || !folder.isDirectory()) {
	            System.err.println("Belirtilen klasör bulunamadı veya geçersiz.");
	            return;
	        }
	        
	        // Klasördeki tüm dosyaları listele
	        File[] files = folder.listFiles();
	        
	        // Dosyaları kontrol et
	        if (files != null) {
	            for (File file : files) {
	                if (file.isDirectory()) {
	                    // Eğer dosya bir alt klasörse, alt klasörü temizle (rekürsif çağrı)
	                    cleanFolder(file.getAbsolutePath());
	                } else {
	                    // Eğer dosya bir dosyaysa
	                    if (!file.getName().endsWith(".java")) {
	                        // Java dosyası değilse, dosyayı sil
	                        file.delete();
	                        System.out.println("Dosya silindi: " + file.getAbsolutePath());
	                    } else {
	                        // Java dosyasını kontrol et
	                        if (containsInterfaceKeyword(file)) {
	                            // Eğer dosya içinde "interface" kelimesi bulunuyorsa, dosyayı sil
	                            file.delete();
	                            System.out.println("Java dosyası silindi: " + file.getAbsolutePath());
	                        }
	                    }
	                }
	            }
	        }
	    }

	    private static boolean containsInterfaceKeyword(File file) {
	        boolean containsInterface = false;
	        try (Scanner scanner = new Scanner(file)) {
	            while (scanner.hasNextLine()) {
	                String line = scanner.nextLine();
	                if (line.contains("interface")) {
	                    containsInterface = true;
	                    break;
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return containsInterface;
	    }

}
