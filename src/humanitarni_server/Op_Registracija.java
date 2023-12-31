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

import exceptions.SrvGreskaException;
import objekti.Klijent_Info;
import objekti.Korisnik;

// dopuni provere i feedback
public class Op_Registracija {

	static boolean serverska_greska = false;

	public static void registracija(Klijent_Info k) throws IOException {
		Korisnik novi_korisnik = new Korisnik();
		Meni_Header.header(k, Podmeni.REGISTRACIJA);
		k.ka_klijentu.println("Unesite korisnicko ime:");
		while (!validan_username(k, novi_korisnik.username = k.od_klijenta.readLine())) {
			Meni_Header.header(k, Podmeni.REGISTRACIJA);
			if (serverska_greska) {
				SrvGreskaException.srv_greska_exception(k);
				return;
			}
			k.ka_klijentu.println("To ime je zauzeto!");
			k.ka_klijentu.println("Unesite korisnicko ime:");
		}
		k.ka_klijentu.println("Unesite sifru:");
		while (!validan_password(novi_korisnik.password = k.od_klijenta.readLine())) {
			Meni_Header.header(k, Podmeni.REGISTRACIJA);
			k.ka_klijentu.println("Sifra mora imati minimum 8 karaktera");
			k.ka_klijentu.println("Unesite sifru:");
		}
		k.ka_klijentu.println("Unesite ime:");
		while (!validno_ime_prezime(novi_korisnik.ime = k.od_klijenta.readLine())) {
			Meni_Header.header(k, Podmeni.REGISTRACIJA);
			k.ka_klijentu.println("Ime se pise velikim slovom!");
			k.ka_klijentu.println("Unesite ime:");
		}
		k.ka_klijentu.println("Unesite prezime:");
		while (!validno_ime_prezime(novi_korisnik.prezime = k.od_klijenta.readLine())) {
			Meni_Header.header(k, Podmeni.REGISTRACIJA);
			k.ka_klijentu.println("Prezime se pise velikim slovom!");
			k.ka_klijentu.println("Unesite prezime:");
		}
		k.ka_klijentu.println("Unesite JMBG:");
		while (!validan_JMBG(novi_korisnik.JMBG = k.od_klijenta.readLine())) {
			Meni_Header.header(k, Podmeni.REGISTRACIJA);
			k.ka_klijentu.println("Unesite validan JMBG:");
		}
		k.ka_klijentu.println("Unesite broj kreditne kartice:");
		while (!Op_Uplata.validna_kartica(k, novi_korisnik.kartica = k.od_klijenta.readLine())) {
			Meni_Header.header(k, Podmeni.REGISTRACIJA);
			k.ka_klijentu.println("Ta kartica ne postoji!");
			k.ka_klijentu.println("Unesite broj kreditne kartice:");
		}
		k.ka_klijentu.println("Unesite email:");
		while (!validan_email(novi_korisnik.email = k.od_klijenta.readLine())) {
			Meni_Header.header(k, Podmeni.REGISTRACIJA);
			k.ka_klijentu.println(novi_korisnik.email + " nije validan email!");
			k.ka_klijentu.println("Unesite email:");
		}

		if (unos_registracije(k, novi_korisnik)) {
			Meni_Header.header(k, Podmeni.REGISTRACIJA);
			k.ka_klijentu.println("Uspesno ste se registrovali!");
			k.ka_klijentu.println("\tKorisnicko ime: " + novi_korisnik.username);
		} else {
			k.ka_klijentu.println("Greska pri registraciji!");
			SrvGreskaException.srv_greska_exception(k);
		}
	}

	// METODE

	static boolean unos_registracije(Klijent_Info k, Korisnik korisnik) {
		ReadWriteLock lock = new ReentrantReadWriteLock();
		Lock upis_lock = lock.writeLock();
		File registrovani_korisnici = new File("registrovani_klijenti.dat");
		try {
			// true u file output konstrukturu otvara fajl za apendovanje // boolean append
			upis_lock.lock();
			ObjectOutputStream upis = new ObjectOutputStream(new FileOutputStream(registrovani_korisnici, true));
			upis.writeObject(korisnik);
			upis.close();
		} catch (IOException e) {
			System.err.println("Greska pri otvaranju baze registrovanih korisnika");
			serverska_greska = true;
			e.printStackTrace();
			upis_lock.unlock();
			return false;
		} finally {
			upis_lock.unlock();
		}
		return true;
	}

	// provere

	static boolean validan_username(Klijent_Info k, String username) { // potencijlano ogranici na 35 TODO
		if (username == null || username.equals(""))
			return false;
		Korisnik korisnik;
		File registrovani_korisnici = new File("registrovani_klijenti.dat");
		try {
			ObjectInputStream pogled = new ObjectInputStream(new FileInputStream(registrovani_korisnici));
			while ((korisnik = (Korisnik) pogled.readObject()) != null) {
				if (korisnik.username.equals(username)) {
					pogled.close();
					return false;
				}
			}
			pogled.close();
		} catch (EOFException e) {
			return true;
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Greska pri otvaranju baze registrovanih korisnika");
			serverska_greska = true;
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
		if (ime_prezime == null || ime_prezime.equals(""))
			return false;
		Character prvo_slovo = ime_prezime.charAt(0);
		if (prvo_slovo != Character.toUpperCase(prvo_slovo))
			return false;
		return true;
	}

}