package humanitarni_server;

import java.io.*;
import java.net.*;

import objekti.Korisnik;

public class ServerThread extends Thread {

	Socket klijent_soket = null;

	public static BufferedReader od_klijenta = null;
	public static PrintStream ka_klijentu = null;
	
	public static Korisnik prijavljen_korisnik;

	public ServerThread(Socket klijent_soket) {
		this.klijent_soket = klijent_soket;
	}

	@Override
	public void run() {
		try {
			// inicijalizacija tokova
			od_klijenta = new BufferedReader(new InputStreamReader(klijent_soket.getInputStream()));
			ka_klijentu = new PrintStream(klijent_soket.getOutputStream());

			ka_klijentu.println("> Konekcija uspesna!");
			ka_klijentu.println("> Dobrodosli!\n");
			Meni.glavni_meni(false);
		} catch (IOException e) {
			System.err.println(">>> greska u konekciji sa klijentom: " + e);
		}
	}

}
