package humanitarni_server;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import exceptions.SrvGreskaException;
import objekti.Klijent_Info;
import objekti.Korisnik;

public class Op_Prijava {

	public static void prijava(Klijent_Info k) throws IOException {
		Username_Password up = unos(k, null);
		log ishod;
		while ((ishod = validni_kredencijali(k, up)) != log.USPEH) {
			switch (ishod) {
			case POGRESNA_LOZINKA:
				up = unos(k, "Pogresna lozinka!\n");
				break;
			case NE_POSTOJI:
				up = unos(k, "Nije registrovan korisnik sa tim korisnickim imenom!\n");
				break;
			case SRV_GRESKA:
				return;
			default:
				break;
			}
		}
		k.ka_klijentu.println("\nUspesno ste se prijavili!");
	}

	// METODE

	static Username_Password unos(Klijent_Info k, String poruka) throws IOException {
		Username_Password up = new Username_Password();
		Meni_Header.header(k, Podmeni.PRIJAVA);
		if (poruka != null)
			k.ka_klijentu.println(poruka);
		k.ka_klijentu.println("Unesite korisnicko ime:");
		up.username = k.od_klijenta.readLine();
		k.ka_klijentu.println("Unesite sifru:");
		up.password = k.od_klijenta.readLine();
		return up;
	}

	static log validni_kredencijali(Klijent_Info k, Username_Password up) {
		File registrovani_korisnici = new File("registrovani_klijenti.dat");
		Korisnik kor;
		boolean korisnik_postoji = false;
		try {
			ObjectInputStream pogled = new ObjectInputStream(new FileInputStream(registrovani_korisnici));
			while ((kor = (Korisnik) pogled.readObject()) != null) {
				if (up.username.equals(kor.username)) {
					korisnik_postoji = true;
				}
				if (up.password.equals(kor.password)) {
					k.prijavljen_korisnik = kor;
					pogled.close();
					return log.USPEH;
				}
			}
			pogled.close();
		} catch (EOFException e) {
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			SrvGreskaException.srv_greska_exception(k);
			return log.SRV_GRESKA;
		}
		if (korisnik_postoji)
			return log.POGRESNA_LOZINKA;
		return log.NE_POSTOJI;
	}

}

enum log {
	NE_POSTOJI, POGRESNA_LOZINKA, USPEH, SRV_GRESKA
}

class Username_Password {
	String username;
	String password;

	public Username_Password() {

	}
}
