package exceptions;

import objekti.Klijent_Info;

public class SrvGreskaException {

	public static void srv_greska_exception(Klijent_Info k) {
		k.ka_klijentu.println("Imamo tehnickih problema");
		k.ka_klijentu.println("Pokusajte kasnije ili nam prijavite problem na email@host.domen");
	}
	
}
