package humanitarni_server;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;

// dopuni provere i feedback
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
			ka_klijentu.println("Unesite ime:");
			while (!validno_ime_prezime(novi_korisnik.ime = od_klijenta.readLine())) {
				header(ka_klijentu);
				ka_klijentu.println("Ime se pise velikim slovom!");
				ka_klijentu.println("Unesite ime:");
			}
			ka_klijentu.println("Unesite prezime:");
			while (!validno_ime_prezime(novi_korisnik.prezime = od_klijenta.readLine())) {
				header(ka_klijentu);
				ka_klijentu.println("Prezime se pise velikim slovom!");
				ka_klijentu.println("Unesite prezime:");
			}
			ka_klijentu.println("Unesite JMBG:");
			while (!validan_JMBG(novi_korisnik.JMBG = od_klijenta.readLine())) {
				header(ka_klijentu);
				ka_klijentu.println("Unesite validan JMBG:");
			}
			ka_klijentu.println("Unesite broj kreditne kartice:");
			while (!UplataMeni.validna_kartica(novi_korisnik.kartica = od_klijenta.readLine())) {
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

			if (unos_registracije(novi_korisnik)) {
				header(ka_klijentu);
				ka_klijentu.println("Uspesno ste se registrovali!");
				ka_klijentu.println("\tKorisnicko ime: " + novi_korisnik.username);
				ka_klijentu.println("Sada mozete videti najskorije uplate.");
			} else {
				ka_klijentu.println("Greska pri registraciji!");
			}
		} catch (IOException e) {
			System.err.println("IOException: " + e);
		}
	}

	// METODE

	static boolean unos_registracije(Korisnik korisnik) {
		File registrovani_korisnici = new File("registrovani_klijenti.txt");
		try {
			// true u file output konstrukturu otvara fajl za apendovanje // boolean append
			ObjectOutputStream upis = new ObjectOutputStream(new FileOutputStream(registrovani_korisnici, true));
			upis.writeObject(korisnik);
			upis.close();
		} catch (IOException e) {
			System.err.println("Greska pri otvaranju baze registrovanih korisnika");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// provere

	static boolean validan_username(String username) {
		Korisnik k;
		File registrovani_korisnici = new File("registrovani_klijenti.txt");
		try {
			ObjectInputStream pogled = new ObjectInputStream(new FileInputStream(registrovani_korisnici));
			while ((k = (Korisnik) pogled.readObject()) != null) {
				if (k.username.equals(username)) {
					pogled.close();
					return false;
				}
			}
			pogled.close();
		} catch (EOFException e) {
			return true;
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Greska pri otvaranju baze registrovanih korisnika");
			// ovde je potrebno sofisticiranije handlovanje greske, obavestiti korisnika da
			// je problem server side
			e.printStackTrace();
			return false;
		}
		return true;
	}

	static boolean validan_password(String password) {
		if (password.length() < 8)
			return false;
		return true;
	}

	static boolean validan_JMBG(String jmbg) {
		int duzina = jmbg.length();
		if (duzina < 13 || duzina > 13)
			return false;
		for (Character c : jmbg.toCharArray()) {
			if (!Character.isDigit(c))
				return false;
		}
		return true;
	}

	static boolean validan_email(String email) {
		if (!email.contains("@"))
			return false;
		return true;
	}

	static boolean validno_ime_prezime(String ime_prezime) {
		Character prvo_slovo = ime_prezime.charAt(0);
		if (prvo_slovo != Character.toUpperCase(prvo_slovo))
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