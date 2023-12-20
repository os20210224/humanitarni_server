package humanitarni_server;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import objekti.Korisnik;

// dopuni provere i feedback
public class Op_Registracija {

	public static void registracija() {
		Korisnik novi_korisnik = new Korisnik();
		try {
			Meni_Header.header(Podmeni.REGISTRACIJA);
			ServerThread.ka_klijentu.println("Unesite korisnicko ime:");
			while (!validan_username(novi_korisnik.username = ServerThread.od_klijenta.readLine())) {
				Meni_Header.header(Podmeni.REGISTRACIJA);
				ServerThread.ka_klijentu.println("To ime je zauzeto!");
				ServerThread.ka_klijentu.println("Unesite korisnicko ime:");
			}
			ServerThread.ka_klijentu.println("Unesite sifru:");
			while (!validan_password(novi_korisnik.password = ServerThread.od_klijenta.readLine())) {
				Meni_Header.header(Podmeni.REGISTRACIJA);
				ServerThread.ka_klijentu.println("Sifra mora imati minimum 8 karaktera");
				ServerThread.ka_klijentu.println("Unesite sifru:");
			}
			ServerThread.ka_klijentu.println("Unesite ime:");
			while (!validno_ime_prezime(novi_korisnik.ime = ServerThread.od_klijenta.readLine())) {
				Meni_Header.header(Podmeni.REGISTRACIJA);
				ServerThread.ka_klijentu.println("Ime se pise velikim slovom!");
				ServerThread.ka_klijentu.println("Unesite ime:");
			}
			ServerThread.ka_klijentu.println("Unesite prezime:");
			while (!validno_ime_prezime(novi_korisnik.prezime = ServerThread.od_klijenta.readLine())) {
				Meni_Header.header(Podmeni.REGISTRACIJA);
				ServerThread.ka_klijentu.println("Prezime se pise velikim slovom!");
				ServerThread.ka_klijentu.println("Unesite prezime:");
			}
			ServerThread.ka_klijentu.println("Unesite JMBG:");
			while (!validan_JMBG(novi_korisnik.JMBG = ServerThread.od_klijenta.readLine())) {
				Meni_Header.header(Podmeni.REGISTRACIJA);
				ServerThread.ka_klijentu.println("Unesite validan JMBG:");
			}
			ServerThread.ka_klijentu.println("Unesite broj kreditne kartice:");
			while (!Op_Uplata.validna_kartica(novi_korisnik.kartica = ServerThread.od_klijenta.readLine())) {
				Meni_Header.header(Podmeni.REGISTRACIJA);
				ServerThread.ka_klijentu.println("Ta kartica ne postoji!");
				ServerThread.ka_klijentu.println("Unesite broj kreditne kartice:");
			}
			ServerThread.ka_klijentu.println("Unesite email:");
			while (!validan_email(novi_korisnik.email = ServerThread.od_klijenta.readLine())) {
				Meni_Header.header(Podmeni.REGISTRACIJA);
				ServerThread.ka_klijentu.println(novi_korisnik.email + " nije validan email!");
				ServerThread.ka_klijentu.println("Unesite email:");
			}

			if (unos_registracije(novi_korisnik)) {
				Meni_Header.header(Podmeni.REGISTRACIJA);
				ServerThread.ka_klijentu.println("Uspesno ste se registrovali!");
				ServerThread.ka_klijentu.println("\tKorisnicko ime: " + novi_korisnik.username);
			} else {
				ServerThread.ka_klijentu.println("Greska pri registraciji!");
			}
		} catch (IOException e) {
			System.err.println("IOException: " + e);
		}
	}

	// METODE

	static boolean unos_registracije(Korisnik korisnik) {
		ReadWriteLock lock = new ReentrantReadWriteLock();
		Lock upis_lock = lock.writeLock();
		File registrovani_korisnici = new File("registrovani_klijenti.txt");
		try {
			// true u file output konstrukturu otvara fajl za apendovanje // boolean append
			upis_lock.lock();
			ObjectOutputStream upis = new ObjectOutputStream(new FileOutputStream(registrovani_korisnici, true));
			upis.writeObject(korisnik);
			upis.close();
		} catch (IOException e) {
			System.err.println("Greska pri otvaranju baze registrovanih korisnika");
			e.printStackTrace();
			upis_lock.unlock();
			return false;
		}
		finally {
			upis_lock.unlock();
		}
		return true;
	}

	// provere

	static boolean validan_username(String username) { // potencijlano ogranici na 35 TODO
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
			// ovde je potrebno sofisticiranije handlovanje greske, obavestiti korisnika da TODO
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

}