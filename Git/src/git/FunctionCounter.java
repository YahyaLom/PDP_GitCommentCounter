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
        Map<String, Integer[]> fileCounts = countFunctionsAndCommentsInJavaFiles(directory);

        // Toplam LOC (Line of Code) sayısı
        int totalLOC = 0;

        // Dosya adı, tanımlı fonksiyon sayısı, normal yorum sayısı, ve dökümantasyon yorum sayısını yazdır
        for (String fileName : fileCounts.keySet()) {
            Integer[] counts = fileCounts.get(fileName);
            totalLOC += counts[3]; // Her dosya için hesaplanan LOC sayısını topla
            System.out.println("Dosya Adı: " + fileName + 
                               ", Tanımlı Fonksiyon Sayısı: " + counts[0] +
                               ", Normal Yorum Sayısı: " + counts[1] +
                               ", Dökümantasyon Yorum Sayısı: " + counts[2] +
                               ", LOC: " + counts[3] +
                               ", KSS: " + counts[4]);
        }

        // Toplam LOC sayısını ekrana bas
        System.out.println("Toplam LOC (Line of Code): " + totalLOC);
    }

    private static Map<String, Integer[]> countFunctionsAndCommentsInJavaFiles(String directory) {
        Map<String, Integer[]> fileCounts = new HashMap<>();

        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("Belirtilen dizin bulunamadı veya bir dizin değil.");
            return fileCounts;
        }

        // Dizindeki her dosya ve alt dizin için işlem yap
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Eğer dosya bir dizinse, bu dizindeki dosyaları işle
                    fileCounts.putAll(countFunctionsAndCommentsInJavaFiles(file.getAbsolutePath()));
                } else if (file.getName().endsWith(".java")) {
                    // Eğer dosya bir Java dosyasıysa, tanımlı fonksiyon sayısını, normal yorum sayısını, dökümantasyon yorum sayısını, LOC ve KSS sayılarını hesapla
                    Integer[] counts = new Integer[5];
                    counts[0] = countFunctions(file);
                    counts[1] = countRegularComments(file);
                    counts[2] = countDocumentationComments(file);
                    counts[3] = countLinesOfCode(file);
                    counts[4] = countKSS(file);
                    fileCounts.put(file.getName(), counts);
                }
            }
        }
        return fileCounts;
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

    private static int countRegularComments(File file) {
        int commentCount = 0;
        boolean isDocComment = false; // /** işareti belirtecini kontrol etmek için bir bayrak

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Satırda "//" veya "/*" bulunursa, bir yorum satırı vardır
                if (line.contains("//") || (line.contains("/*") && !line.contains("/**"))) {
                    commentCount++;
                }
            }
        } catch (IOException e) {
            System.err.println("Dosya okunurken bir hata oluştu: " + e.getMessage());
        }
        return commentCount;
    }

    private static int countDocumentationComments(File file) {
        int commentCount = 0;
        boolean isDocComment = false; // /** işareti belirtecini kontrol etmek için bir bayrak

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // /** işareti belirteciyle karşılaşıldığında, bu bir dökümantasyon yorumu olarak kabul edilir
                if (line.contains("/**")) {
                    isDocComment = true;
                }

                // /** işareti belirteci varsa ve */ işareti yoksa, dökümantasyon yorumu devam ediyor demektir
                if (isDocComment && !line.contains("*/")) {
                    commentCount++;
                }

                // */ işareti varsa, dökümantasyon yorumu tamamlanmış olur
                if (line.contains("*/")) {
                    isDocComment = false;
                }
            }
        } catch (IOException e) {
            System.err.println("Dosya okunurken bir hata oluştu: " + e.getMessage());
        }
        // Dökümantasyon yorum sayısından /** işaretlerinin sayısını çıkar
        return commentCount - countOccurrences(file, "/**");
    }
    
    // Belirli bir dosyada belirtilen metni kaç kez içerdiğini sayan yardımcı metot
    private static int countOccurrences(File file, String text) {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int index = line.indexOf(text);
                while (index != -1) {
                    count++;
                    index = line.indexOf(text, index + 1);
                }
            }
        } catch (IOException e) {
            System.err.println("Dosya okunurken bir hata oluştu: " + e.getMessage());
        }
        return count;
    }

    // Dosyadaki toplam satır sayısını hesaplayan metot
    private static int countLinesOfCode(File file) {
        int loc = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) {
                loc++;
            }
        } catch (IOException e) {
            System.err.println("Dosya okunurken bir hata oluştu: " + e.getMessage());
        }
        return loc;
    }
    
    // Dosyadaki kod satırı sayısını hesaplayan metot
    private static int countKSS(File file) {
        int kss = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                // Satır alfabedeki bir harfle başlıyorsa veya { veya } ile başlıyorsa, bu bir kod satırıdır
                if ((!line.isEmpty() && Character.isLetter(line.charAt(0))) || line.startsWith("{") || line.startsWith("}")||line.startsWith("@")) {
                    kss++;
                }
            }
        } catch (IOException e) {
            System.err.println("Dosya okunurken bir hata oluştu: " + e.getMessage());
        }
        return kss;
    }
}













//normal yorumları ve fonksiynları doğur sayıyor
/*package git;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FunctionCounter {
    public static void countAndPrintFunctions(String directory) {
        // Klonlanmış repodaki Java dosyalarını işle
        Map<String, Integer[]> fileCounts = countFunctionsAndCommentsInJavaFiles(directory);

        // Dosya adı, tanımlı fonksiyon sayısı ve yorum satırı sayısını yazdır
        for (String fileName : fileCounts.keySet()) {
            Integer[] counts = fileCounts.get(fileName);
            System.out.println("Dosya Adı: " + fileName + 
                               ", Tanımlı Fonksiyon Sayısı: " + counts[0] +
                               ", Yorum Satırı Sayısı: " + counts[1]);
        }
    }

    private static Map<String, Integer[]> countFunctionsAndCommentsInJavaFiles(String directory) {
        Map<String, Integer[]> fileCounts = new HashMap<>();

        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("Belirtilen dizin bulunamadı veya bir dizin değil.");
            return fileCounts;
        }

        // Dizindeki her dosya ve alt dizin için işlem yap
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Eğer dosya bir dizinse, bu dizindeki dosyaları işle
                    fileCounts.putAll(countFunctionsAndCommentsInJavaFiles(file.getAbsolutePath()));
                } else if (file.getName().endsWith(".java")) {
                    // Eğer dosya bir Java dosyasıysa, tanımlı fonksiyon sayısını ve yorum satırı sayısını hesapla
                    Integer[] counts = new Integer[2];
                    counts[0] = countFunctions(file);
                    counts[1] = countComments(file);
                    fileCounts.put(file.getName(), counts);
                }
            }
        }
        return fileCounts;
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

    private static int countComments(File file) {
        int commentCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Satırda "//" bulunursa, bir yorum satırı vardır
                if (line.contains("//")) {
                    commentCount++;
                }
            }
        } catch (IOException e) {
            System.err.println("Dosya okunurken bir hata oluştu: " + e.getMessage());
        }
        return commentCount;
    }
}*/































































/*
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


*/



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

