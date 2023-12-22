package humanitarni_server;

import java.io.*;
import java.net.*;

import objekti.Klijent_Info;

public class ServerThread extends Thread {

	Klijent_Info k = new Klijent_Info();

	public ServerThread(Socket klijent_soket) {
		this.k.klijent_soket = klijent_soket;
	}

	@Override
	public void run() {
		try {
			// inicijalizacija tokova
			k.od_klijenta = new BufferedReader(new InputStreamReader(k.klijent_soket.getInputStream()));
			k.ka_klijentu = new PrintStream(k.klijent_soket.getOutputStream());

			k.ka_klijentu.println("> Konekcija uspesna!");
			k.ka_klijentu.println("> Dobrodosli!\n");
			Meni.glavni_meni(k);

		} catch (IOException e) {
			System.err.println(">>> greska u konekciji sa klijentom: " + e);
		}
	}

}
