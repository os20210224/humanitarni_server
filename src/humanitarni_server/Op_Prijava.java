package humanitarni_server;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import exceptions.RegKlijentiException;
import objekti.Korisnik;

public class Op_Prijava {

	public static void prijava() {
		String username;
		String password;

		try {
			header();
			ServerThread.ka_klijentu.println("Unesite korisnicko ime:");
			username = ServerThread.od_klijenta.readLine();
			ServerThread.ka_klijentu.println("Unesite sifru:");
			password = ServerThread.od_klijenta.readLine();
			int ishod;
			while ((ishod = validni_kredencijali(username, password)) != 0) {
				switch (ishod) {
				case 1:
					header();
					ServerThread.ka_klijentu.println("Negde ste pogresili!\n");
					ServerThread.ka_klijentu.println("Unesite korisnicko ime:");
					username = ServerThread.od_klijenta.readLine();
					ServerThread.ka_klijentu.println("Unesite sifru:");
					password = ServerThread.od_klijenta.readLine();
					break;
				case 2:
					header();
					ServerThread.ka_klijentu.println("Nije registrovan korisnik sa tim korisnickim imenom!\n");
					ServerThread.ka_klijentu.println("Unesite korisnicko ime:");
					username = ServerThread.od_klijenta.readLine();
					ServerThread.ka_klijentu.println("Unesite sifru:");
					password = ServerThread.od_klijenta.readLine();
					break;
				default:
					break;
				}
			}
			ServerThread.ka_klijentu.println("\nUspesno ste se prijavili!");
			ServerThread.ka_klijentu.println("Sada mozete videti najskorije uplate.");
		} catch (IOException e) {
			System.err.println("IOException: " + e);
		} catch (RegKlijentiException e) {
			System.err.println("registrovani_klijent Exception: " + e);
			e.printStackTrace();
		}
	}

	// METODE

	static int validni_kredencijali(String username, String password) throws RegKlijentiException {
		File registrovani_korisnici = new File("registrovani_klijenti.txt");
		Korisnik k;
		boolean korisnik_postoji = false;
		try {
			ObjectInputStream pogled = new ObjectInputStream(new FileInputStream(registrovani_korisnici));
			while ((k = (Korisnik) pogled.readObject()) != null) {
				if (username.equals(k.username)) {
					korisnik_postoji = true;
				}
				if (password.equals(k.password)) {
					ServerThread.prijavljen_korisnik = k;
					pogled.close();
					return 0;
				}
			}
			pogled.close();
		}catch (EOFException e) {
			if (korisnik_postoji)
				return 1;
			return 2;
		} 
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			throw new RegKlijentiException();
		}
		if (korisnik_postoji)
			return 1;
		return 2;
	}

	static void header() {
		ServerThread.ka_klijentu.println("==================================================");
		ServerThread.ka_klijentu.println("=               SISTEM ZA DONACIJU               =");
		ServerThread.ka_klijentu.println("=                     Prijava                    =");
		ServerThread.ka_klijentu.println("==================================================");
	}

}
