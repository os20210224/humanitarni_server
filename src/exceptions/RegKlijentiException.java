package exceptions;

import humanitarni_server.ServerThread;

public class RegKlijentiException extends Exception {

	public RegKlijentiException() {
		ServerThread.ka_klijentu.println("Imamo tehnickih problema");
		ServerThread.ka_klijentu.println("Pokusajte kasnije ili nam prijavite problem na email@host.domen");
	}
	
}
