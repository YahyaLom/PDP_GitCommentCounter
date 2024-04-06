package git;
import java.io.File;
import java.util.Random;
import java.io.IOException;
public class GitKlonlayici 
{
	public static File repoKlonlayici(String kullanicidanAlinanURL)
	{
		//Jar dosyasını bulunduğu dizini sakladık
		String jarYolu=GitKlonlayici.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		File jarDosyasi=new File(jarYolu);
		//uygulamamızın nerede olduğunu aldık, repoyu buraya klonlayacağız
		String ustKlasor=jarDosyasi.getParent();
		//Random sayi oluşturuyoruz, eşsiz bir klsör ismi için
		Random random=new Random();
		int randomSayi=random.nextInt(1000);
		
		try{
			//klon oluşturmak için jar ile aynı yerde bir klasör oluşturduk
			File klonOlusacakAdres = new File(ustKlasor+File.separator+"klonlanmis_repo_yahya_"+randomSayi);
			
			
			//Git komutları ile depoyu klonluyoruz
			ProcessBuilder processBuilder=new ProcessBuilder("git","clone",kullanicidanAlinanURL,klonOlusacakAdres.getAbsolutePath());
			Process process = processBuilder.start();
			
			//Klonlama işlemi bitene kadar işlemlere devam etmiyoruz
			int klonlamaBitti=process.waitFor();
			
			if(klonlamaBitti==0)
			{
				System.out.println("Git repository başarı ile klonlandı.");
				//Klon oluşan klasör döndürüldü
				return klonOlusacakAdres;
			}else {
				System.err.println("Klonlama işlemi try-catch -> try bloğu if-else yapısı : "+klonlamaBitti);
			}
		}catch(IOException | InterruptedException e){
			System.err.println("Klonlama işlemi try-catch-> catch bloğu : "+ e.getMessage());
		}
		//eğer buraya gelmişse hata vardır ve boş döner işlem başarılı olunca başarı bilgisi dönüyor 
		return null;

		
	}

}
