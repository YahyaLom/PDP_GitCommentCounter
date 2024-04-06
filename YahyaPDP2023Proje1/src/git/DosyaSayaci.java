package git;
//satır 42 de kaldık 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DosyaSayaci 
{
	public static void fonksiyonSayaci(String yol)
	{
		//bir map oluşturduk ve bu map dosya adı ile beraber tam sayı değerleri beraber saklayacak
		Map<String, Integer[]> dosyaHesapcisi=fonkVeYorumSayici(yol);
		//System.out.println("ekran yazdırma öncesi");
		for(String fileName : dosyaHesapcisi.keySet()) 
		{
			Integer[] counts = dosyaHesapcisi.get(fileName);
            //totalLOC += counts[3]; // Her dosya için hesaplanan LOC sayısını topla
			if(counts[0]==0)
			{
				continue;
			}
            System.out.println("Sınıf: " + fileName );
            System.out.println("Javadoc Satır Sayısı : "+counts[2]);
            System.out.println("Yorum Satır Sayısı   : "+counts[1]);
            System.out.println("Kod Satır Sayısı     : "+counts[4]);
            System.out.println("LOC                  : "+counts[3]);
            System.out.println("Fonksiyon Sayısı     : "+counts[0]);       
            double fonkSayisi=counts[0];
            double yorumSayisi=counts[1];
            double javaDocSayisi=counts[2];
            double locSayisi=counts[3];
            double kodSayisi=counts[4];
            double YG=((javaDocSayisi+yorumSayisi)*0.80)/fonkSayisi;
            double YH=(kodSayisi/fonkSayisi)*0.3;
            double yorumSapmaYuzdesi=(((100*YG)/YH))-100;
            System.out.printf("Yorum Sapma Yüzdesi: %.2f%n", yorumSapmaYuzdesi);//ekran çıktısnda virgülden sonra iki basamak olsun diye
            //System.out.println("Yorum Sapma Yüzdesi  : "+ yorumSapmaYuzdesi );
            System.out.println("------------------------------------");
	            
	    }
		//System.out.println("ekran yazdırma sonrası");
	}
	
	
	//fonksiyon sayma sınıfı
	private static Map<String, Integer[]>fonkVeYorumSayici(String yol)
	{
		//System.out.println("geldim fonkveyorum sayıcı");
		Map<String, Integer[]> dosyaHesapcisi=new HashMap<>();
		
		File gecici=new File(yol);
		if(!gecici.exists()||!gecici.isDirectory())
		{
			System.err.println("Fonksiyon sayaci if bloğu belirtilen dizin bulunamadı");
			return dosyaHesapcisi;
		}
		
		File[] files=gecici.listFiles();
		if(files!=null)
		{
			for(File file: files)
			{
				if(file.isDirectory())
				{
					dosyaHesapcisi.putAll(fonkVeYorumSayici(file.getAbsolutePath()));
				}
				else if (file.getName().endsWith(".java")) 
				{
					//eğer dosya java dosyası ise işlem yapacak
					Integer[] counts=new Integer[5];
					counts[0]=fonksiyonSayici(file);
					counts[1]=normalYorumSayici(file);
					counts[2]=javaDocSayici(file);
					counts[3]=locSayici(file);
					counts[4]=kss(file);
					dosyaHesapcisi.put(file.getName(), counts);
							
					
				}//else if
			}//for
		}//if parent
		return dosyaHesapcisi;
	}//sınıf
	
	
	
	
	//klasördeki fonkisyonu sayacak olan fonksiyon
	private static int fonksiyonSayici(File file)
	{
		//System.out.println("geldim fonksiyonSayici");
		int fonksiyonSayisi=0;
		
		try(BufferedReader reader =new BufferedReader(new FileReader(file)))
		{
			String line;
			while((line=reader.readLine())!=null)
			{
				line=line.trim();
				//sadece public private ve proteceetd ile başlayan satırlar ile işlem yapacak
				/*if(line.matches("(public|private|protected)\\s+.*\\(.*\\)\\s*\\{?")&&!line.startsWith("public class")||line.matches("main"))
				{
					fonksiyonSayisi++;
				}*/
				if(line.matches("(public|private|protected)\\s+.*\\(.*\\)\\s*\\{?")&&!line.startsWith("public class")||line.startsWith("public static"))
				{
					fonksiyonSayisi++;
				}
			}//while
			
		}//try
		catch(IOException e)
		{
			System.err.println("fonksiyon sayıcı fonksiyonu dosya okunurken hata -> "+e.getMessage());
		}
		return fonksiyonSayisi;
	}
	
	private static int normalYorumSayici(File file)
	{
		//System.out.println("geldim normalYorumSayici");
		int yorumSayisi=0;
		boolean javaDocMu=false;//!** varsa saymamak için 
		
		 try (BufferedReader reader = new BufferedReader(new FileReader(file)))
		 {
			 String line;
			 while((line=reader.readLine())!=null)
			 {
				 // // -> ve /* bu var ise yorum sayısını bir arttıracağız. /** bu değilse ama 
				 if (line.contains("//") || (line.contains("/*") && !line.contains("/**"))) 
				 {
					 yorumSayisi++;
	             }
			 }//while
		 }//try
		 catch (IOException e) 
		 {
	            System.err.println("Hata, normal yorum sayma sınıfı -> " + e.getMessage());
	     }//catch
		 return yorumSayisi;
	}//normal Yorum Sayici bitiş
	private static int javaDocSayici(File file)
	{
		//System.out.println("geldim javaDocSayici");
		 int javaDocYorumSayisi = 0;
	     boolean javaDocMu = false;
	     try (BufferedReader reader = new BufferedReader(new FileReader(file)))
	     {
	    	 String line;
	            while ((line = reader.readLine()) != null)
	            {
	            	// /** ile karşılaştık ve başladık saymaya
	            	 if (line.contains("/**")) 
	            	 {
	            		 javaDocMu = true;
	                 }

	                 // /** ile */ arasında mı hala diye kontorl 
	                 if (javaDocMu && !line.contains("*/")) 
	                 {
	                	 javaDocYorumSayisi++;
	                 }

	                 // */ işareti tespit edildiyese
	                 if (line.contains("*/")) 
	                 {
	                	 javaDocMu = false;
	                 }
	            	
	            }
	    	 
	     }
	     catch (IOException e) 
	     {
	            System.err.println("javaDoc Yorum sayıcı try catch bloğu hata -> " + e.getMessage());
	     }
	     return javaDocYorumSayisi-yahya(file,"/**");
		
	}//javaDoc Sayıcı bitti 
	
	private static int yahya(File file, String text) 
	{
		//System.out.println("geldim yahya");
        int sayim = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int index = line.indexOf(text);
                while (index != -1) {
                	sayim++;
                    index = line.indexOf(text, index + 1);
                }
            }
        } catch (IOException e) {
            System.err.println("yahya yardımcı methoduna bir hata var  " + e.getMessage());
        }
        return sayim;
    }//yahya yardımcı fnk bitiş
	
	//loc sayılıyor
	private static int locSayici(File file)
	{
		int locSayisi=0;
		 try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	            while (reader.readLine() != null) {
	            	locSayisi++;
	            }
	        } catch (IOException e) {
	            System.err.println("LOC sayım bloğu catch bloğu hata ->  " + e.getMessage());
	        }
	        return locSayisi;
		
	}//loc sayıcı bitti 
	
	//kod satırını saymka için fonksiyon
	private static int kss(File file)
	{
		int kss = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                // harfse , @ işareti , { ve } ile başlıyorsa kod satırıdır
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
