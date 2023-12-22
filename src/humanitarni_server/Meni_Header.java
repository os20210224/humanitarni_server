package humanitarni_server;

import objekti.Klijent_Info;

public class Meni_Header {

	public static void header(Klijent_Info k, Podmeni podmeni) {
		ocisti_ekran(k);
		k.ka_klijentu.println("==================================================");
		k.ka_klijentu.println("=               SISTEM ZA DONACIJU               =");
		switch (podmeni) {
		case UPLATA:
			k.ka_klijentu.println("=                     Uplata                     =");
			break;
		case REGISTRACIJA:
			k.ka_klijentu.println("=                  Registracija                  =");
			break;
		case PRIJAVA:
			k.ka_klijentu.println("=                     Prijava                    =");
			break;
		case PREGLED_STANJA:
			k.ka_klijentu.println("=                 Pregled stanja                 =");
			break;
		case PREGLED_TRANSAKCIJA:
			k.ka_klijentu.println("=               Pregled transakcija              =");
			break;
		case GLAVNI_MENI:
			// nista c:
			break;
		default:
			break;
		}
		if (k.prijavljen_korisnik != null) {
			k.ka_klijentu.println("= ---------------------------------------------- =");
			k.ka_klijentu
					.println(String.format("%s Korisnik:%37s %s", "=", k.prijavljen_korisnik.username, "="));
		}

		k.ka_klijentu.println("==================================================");
	}

	static void ocisti_ekran(Klijent_Info k) {
		k.ka_klijentu.print("\033[H\033[2J\n");
		k.ka_klijentu.flush();
	}

}