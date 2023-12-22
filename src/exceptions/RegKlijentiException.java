package exceptions;

import objekti.Klijent_Info;

public class RegKlijentiException extends Exception {

	public RegKlijentiException(Klijent_Info k) {
		k.ka_klijentu.println("Imamo tehnickih problema");
		k.ka_klijentu.println("Pokusajte kasnije ili nam prijavite problem na email@host.domen");
	}
	
}
