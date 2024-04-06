/**
*
* @author Yahya Haliloğlu yahya.haliloglu@ogr.sakarya.edu.tr
* @since  ilk odev adımı -> 15.03.2024, Son revize-> 04.07.2024
* <p>
* Klon klösrdeki hesaplama yapılmayacak dosyaları silip, sadece hesaplnacak dosyaları bırakıyor.
* </p>
*/
package git;

import java.io.File;
import java.util.Scanner;

public class KlasörTemizleyici 
{
	public static void klasörTemizleyici(String klasorYolu)
	{
		//
		File folder =new File(klasorYolu);
		String silinenDosyalar="Silinen Dosyalar : ";
		//klasör oluşamış veya yanlış aders gelmiş olabilir
		if(!folder.exists()||!folder.isDirectory())
		{
			System.err.println("Klasör yok, Klasör temizleyici if bloğu");return;
		}
		 
		//klsör içindeki tüm dosyalararı aldık
		File[] files =folder.listFiles();
		
		//boş mu klasör içi
		if(files != null)
		{
			//demek ki dolu
			for(File file :files)
			{
				if(file.isDirectory())
				{
					//eğer klsör değil dosya ise bu dosya için özyineleme yaptık 
					klasörTemizleyici(file.getAbsolutePath());
				}
				else
				{
					if(!file.getName().endsWith(".java"))
					{
						//.java ile bitmiyorsa silinecek 
						file.delete();
						System.out.print(file.getName()+" / ");
					}
					else
					{
						//eğer java dosyası ise interface olanalrı da sileceğiz
						if(interfaceIceriyorMu(file))
						{
							//demek ki interface dosyası
							file.delete();
							System.out.print(file.getName()+" / ");
						}
					}
				}
				
			}
		}

	}
	private static boolean interfaceIceriyorMu(File file)
	{
		//java uzantılı dosyaların içinde interface geçiyor mu diye bakıyor ?
		//eğer geçiyor ise o dosyayı işlemler karışmasın diye sileceğim.
		boolean durum=false;
		try( Scanner cin = new Scanner(file))
		{
			while(cin.hasNextLine())
			{
				String line =cin.nextLine();
				if(line.contains("interface"))
				{
					durum=true;
					break;
					
				}
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}

		return durum;
	}

}
