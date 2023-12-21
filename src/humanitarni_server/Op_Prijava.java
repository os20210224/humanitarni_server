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

		try {
			Username_Password up = unos(null);
			log ishod;
			while ((ishod = validni_kredencijali(up)) != log.USPEH) {
				switch (ishod) {
				case POGRESNA_LOZINKA:
					up = unos("Pogresna lozinka!\n");
					break;
				case NE_POSTOJI:
					up = unos("Nije registrovan korisnik sa tim korisnickim imenom!\n");
					break;
				default:
					break;
				}
			}
			ServerThread.ka_klijentu.println("\nUspesno ste se prijavili!");
		} catch (IOException e) {
			System.err.println("IOException: " + e);
		} catch (RegKlijentiException e) {
			System.err.println("registrovani_klijent Exception: " + e);
			e.printStackTrace();
		}
	}

	// METODE

	static Username_Password unos(String poruka) throws IOException {
		Username_Password up = new Username_Password();
		Meni_Header.header(Podmeni.PRIJAVA);
		if (poruka != null)
			ServerThread.ka_klijentu.println(poruka);
		ServerThread.ka_klijentu.println("Unesite korisnicko ime:");
		up.username = ServerThread.od_klijenta.readLine();
		ServerThread.ka_klijentu.println("Unesite sifru:");
		up.password = ServerThread.od_klijenta.readLine();
		return up;
	}

	static log validni_kredencijali(Username_Password up) throws RegKlijentiException {
		File registrovani_korisnici = new File("registrovani_klijenti.txt"); // TODO prebaci na dat
		Korisnik k;
		boolean korisnik_postoji = false;
		try {
			ObjectInputStream pogled = new ObjectInputStream(new FileInputStream(registrovani_korisnici));
			while ((k = (Korisnik) pogled.readObject()) != null) {
				if (up.username.equals(k.username)) {
					korisnik_postoji = true;
				}
				if (up.password.equals(k.password)) {
					ServerThread.prijavljen_korisnik = k;
					pogled.close();
					return log.USPEH;
				}
			}
			pogled.close();
		} catch (EOFException e) {
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			throw new RegKlijentiException();
		}
		if (korisnik_postoji)
			return log.POGRESNA_LOZINKA;
		return log.NE_POSTOJI;
	}

}

enum log {
	NE_POSTOJI, POGRESNA_LOZINKA, USPEH
}

class Username_Password {
	String username;
	String password;

	public Username_Password() {

	}
}
