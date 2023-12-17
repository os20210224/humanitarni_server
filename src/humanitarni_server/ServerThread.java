package humanitarni_server;

import java.io.*;
import java.net.*;

public class ServerThread extends Thread {

	Socket klijent_soket = null;

	BufferedReader od_klijenta = null;
	PrintStream ka_klijentu = null;

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
			Meni.glavni_meni(false, od_klijenta, ka_klijentu);
		} catch (IOException e) {
			System.err.println(">>> greska pri kreiranju toka podataka sa klijentom: " + e);
		}
	}

}
