package humanitarni_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class RegistracijaMeni {

	public static void registracija(BufferedReader od_klijenta, PrintStream ka_klijentu) {
		Korisnik novi_korisnik = new Korisnik();
		try {
			header(ka_klijentu);
			ka_klijentu.println("Unesite korisnicko ime:");
			while (!validan_username(novi_korisnik.username = od_klijenta.readLine())) {
				header(ka_klijentu);
				ka_klijentu.println("To ime je zauzeto!");
				ka_klijentu.println("Unesite korisnicko ime:");
			}
			ka_klijentu.println("Unesite sifru:");
			while (!validan_password(novi_korisnik.password = od_klijenta.readLine())) {
				header(ka_klijentu);
				ka_klijentu.println("Sifra mora imati minimum 8 karaktera");
				ka_klijentu.println("Unesite sifru:");
			}
			ka_klijentu.println("Unesite JMBG:");
			while (!validan_JMBG(novi_korisnik.JMBG = od_klijenta.readLine())) {
				header(ka_klijentu);
				ka_klijentu.println("Unesite validan JMBG:");
			}
			ka_klijentu.println("Unesite broj kreditne kartice:");
			while (!validna_kartica(novi_korisnik.kartica = od_klijenta.readLine())) {
				header(ka_klijentu);				
				ka_klijentu.println("Ta kartica ne postoji!");
				ka_klijentu.println("Unesite broj kreditne kartice:");
			}
			ka_klijentu.println("Unesite email:");
			while (!validan_email(novi_korisnik.email = od_klijenta.readLine())) {
				header(ka_klijentu);
				ka_klijentu.println(novi_korisnik.email + " nije validan email!");
				ka_klijentu.println("Unesite email:");
			}
			//
			// UPIS KORISNIKA U BAZU KORSNIKA
			//
			header(ka_klijentu);
			ka_klijentu.println("Uspesno ste se registrovali!");
			// printuj pregled infa
			ka_klijentu.println("Sada mozete videti najskorije uplate.");
		} catch (IOException e) {
			System.err.println("IOException: " + e);
		}
	}

	// METODE

	static boolean validan_username(String username) {
		// logika
		return true;
	}

	static boolean validan_password(String password) {
		if (password.length() < 8)
			return false;
		return true;
	}

	static boolean validan_JMBG(String jmbg) {
		if (jmbg.length() < 13)
			return false;
		return true;
	}
	
	static boolean validna_kartica(String kartica) {
		// logika
		return true;
	}
	
	static boolean validan_email(String email) {
		if(!email.contains("@"))
			return false;
		return true;
	}

	static void header(PrintStream ka_klijentu) {
		ka_klijentu.println("==================================================");
		ka_klijentu.println("=               SISTEM ZA DONACIJU               =");
		ka_klijentu.println("=                  Registracija                  =");
		ka_klijentu.println("==================================================");
	}

}