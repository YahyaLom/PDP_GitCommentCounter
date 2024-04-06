/**
*
* @author Yahya Haliloğlu yahya.haliloglu@ogr.sakarya.edu.tr
* @since  ilk odev adımı -> 15.03.2024, Son revize-> 04.07.2024
* <p>
* Burası main, link alınıyor gerekli sınıflara yollanıyor
* </p>
*/
package git;
import java.io.File;
import java.util.Scanner;

public class YahyaPDP2023Proje1 {

	public static void main(String[] args) 
	{
		Scanner cin = new Scanner(System.in);
		System.out.print("Git deposunun URL'sini girin: ");
		String kullanicidanAlinanURL=cin.nextLine();
		//Kullanıcıdan git deposunun URL sini aldık.
		
		//GitKlonlayici sinifina alinan URL yi yolladik.
		File klonlanmisKlasor=GitKlonlayici.repoKlonlayici(kullanicidanAlinanURL);
		
		//klonlamış klsörün adresi döndü file türünde string türü ile işleme devam edeceğim
		String klonlanmisKlasörAdresi=klonlanmisKlasor.getAbsolutePath();
		
		//klonlanmış adreste veri var mı kontrol 
		if(klonlanmisKlasor!=null)
		{
			//Klonlama işlemi başarılı demek ki 
			System.out.println(":):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):)");
			System.out.println("Klonlanmış Reponun klasör dizini : "+klonlanmisKlasörAdresi);
			//System.out.println(":):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):):)");
			
			//System.out.println("!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)");
			System.out.print("Proje ile aynı yere klonladım, yeni klasör oluşturdum.");
			System.out.println("Bakmak istersiniz diye yazdırıyorum.");
			System.out.println("!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)!)");
			System.out.println();
		}
		else
		{
			System.err.println("Klon dosya oluşmadı, Main de else içindeyim...");
		}
		KlasörTemizleyici.klasörTemizleyici(klonlanmisKlasörAdresi);
		System.out.println("\nBu dosyalar ile işlem yapmayacağım için bu dosyaları klasörden sildim.");
		System.out.println();
		System.out.println("--------------------------------------------------------------------------");
		System.out.println();
		DosyaSayaci.fonksiyonSayaci(klonlanmisKlasörAdresi);
		
	
		
		cin.close();
		

	}

}
