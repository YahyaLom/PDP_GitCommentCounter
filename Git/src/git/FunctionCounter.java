package git;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FunctionCounter {
    public static void countAndPrintFunctions(String directory) {
        // Klonlanmış repodaki Java dosyalarını işle
        Map<String, Integer> functionCounts = countFunctionsInJavaFiles(directory);

        // Dosya adı ve tanımlı fonksiyon sayısını yazdır
        for (String fileName : functionCounts.keySet()) {
            int count = functionCounts.get(fileName);
            System.out.println("Dosya Adı: " + fileName + ", Tanımlı Fonksiyon Sayısı: " + count);
        }
    }

    private static Map<String, Integer> countFunctionsInJavaFiles(String directory) {
        Map<String, Integer> functionCounts = new HashMap<>();

        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("Belirtilen dizin bulunamadı veya bir dizin değil.");
            return functionCounts;
        }

        // Dizindeki her dosya ve alt dizin için işlem yap
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Eğer dosya bir dizinse, bu dizindeki dosyaları işle
                    functionCounts.putAll(countFunctionsInJavaFiles(file.getAbsolutePath()));
                } else if (file.getName().endsWith(".java")) {
                    // Eğer dosya bir Java dosyasıysa, tanımlı fonksiyon sayısını hesapla
                    int functionCount = countFunctions(file);
                    functionCounts.put(file.getName(), functionCount);
                }
            }
        }
        return functionCounts;
    }

    private static int countFunctions(File file) {
        int functionCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                // Sadece metod tanımlarını dikkate al (public, private veya protected ile başlayan)
                if (line.matches("(public|private|protected)\\s+.*\\(.*\\)\\s*\\{?") && !line.startsWith("public class")) {
                    functionCount++;
                }
            }
        } catch (IOException e) {
            System.err.println("Dosya okunurken bir hata oluştu: " + e.getMessage());
        }
        return functionCount;
    }
}






//////////////////////////ikinci deneme










/*package git;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FunctionCounter 
{
	public static void countAndPrintFunctions(String directory) 
	{
        // Klonlanmış repodaki Java dosyalarını işle
        Map<String, Integer> functionCounts = countFunctionsInJavaFiles(directory);

        // Dosya adı ve tanımlı fonksiyon sayısını yazdır
        for (String fileName : functionCounts.keySet()) {
            int count = functionCounts.get(fileName);
            System.out.println("Dosya Adı: " + fileName + ", Tanımlı Fonksiyon Sayısı: " + count);
        }
    }

    private static Map<String, Integer> countFunctionsInJavaFiles(String directory) 
    {
        Map<String, Integer> functionCounts = new HashMap<>();

        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("Belirtilen dizin bulunamadı veya bir dizin değil.");
            return functionCounts;
        }

        // Dizindeki her dosya ve alt dizin için işlem yap
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Eğer dosya bir dizinse, bu dizindeki dosyaları işle
                    functionCounts.putAll(countFunctionsInJavaFiles(file.getAbsolutePath()));
                } else if (file.getName().endsWith(".java")) {
                    // Eğer dosya bir Java dosyasıysa, tanımlı fonksiyon sayısını hesapla
                    int functionCount = countFunctions(file);
                    functionCounts.put(file.getName(), functionCount);
                }
            }
        }
        return functionCounts;
    }

    private static int countFunctions(File file) {
        int functionCount = 0;
        boolean inFunction = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("public") || line.startsWith("private") || line.startsWith("protected")) {
                    inFunction = true;
                    functionCount++;
                } else if (inFunction && line.equals("}")) {
                    inFunction = false;
                }
            }
        } catch (IOException e) {
            System.err.println("Dosya okunurken bir hata oluştu: " + e.getMessage());
        }
        return functionCount;
    }
	    
}*/

