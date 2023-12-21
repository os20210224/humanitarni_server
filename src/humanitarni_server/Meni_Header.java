package humanitarni_server;

public class Meni_Header {

	public static void header(Podmeni podmeni) {
		ServerThread.ka_klijentu.println("==================================================");
		ServerThread.ka_klijentu.println("=               SISTEM ZA DONACIJU               =");
		switch (podmeni) {
		case UPLATA:
			ServerThread.ka_klijentu.println("=                     Uplata                     =");
			break;
		case REGISTRACIJA:
			ServerThread.ka_klijentu.println("=                  Registracija                  =");
			break;
		case PRIJAVA:
			ServerThread.ka_klijentu.println("=                     Prijava                    =");
			break;
		case PREGLED_STANJA:
			ServerThread.ka_klijentu.println("=                 Pregled stanja                 =");
			break;
		case PREGLED_TRANSAKCIJA:
			ServerThread.ka_klijentu.println("=               Pregled transakcija              =");
			break;
		case GLAVNI_MENI:
			// nista c:
			break;
		default:
			break;
		}
		if (ServerThread.prijavljen_korisnik != null) {
			ServerThread.ka_klijentu.println("= ---------------------------------------------- =");			
			ServerThread.ka_klijentu.println(String.format("%s Korisnik:%37s %s", "=",ServerThread.prijavljen_korisnik.username , "="));		
		}

		ServerThread.ka_klijentu.println("==================================================");
	}

}