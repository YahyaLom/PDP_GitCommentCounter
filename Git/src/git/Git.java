package git;
import java.io.File;
import java.util.Scanner;
public class Git 
{

	public static void main(String[] args) 
	{
		Scanner cin = new Scanner(System.in);
	   
        System.out.print("Git deposu URL'sini girin: ");
		String gitUrl = cin.nextLine();// Kullanıcıdan Git deposu URL'sini alın

		// Git deposunu klonlamak için yardımcı sınıfı alınan linki yolladık   
		File clonedDirectory = GitCloner.cloneRepository(gitUrl);
		String klonlanmisKlasor = clonedDirectory.getAbsolutePath();

        // Klonlanan dizinin referansını al ve eğer varsa işlem yap
        if (clonedDirectory != null) {
            System.out.println("Klonlanan repository'nin dizini: " + clonedDirectory.getAbsolutePath());
            // Burada klonlanan dizine ilişkin başka işlemler yapabilirsiniz
        } else {
            System.err.println("Git deposu klonlanırken bir hata oluştu.");
        }
        FunctionCounter.countAndPrintFunctions(klonlanmisKlasor);

		cin.close();

	}

}
