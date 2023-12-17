package humanitarni_server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
	// broj porta na kome ce se otvoriti server soket
	static int port = 7259;
	// ovo eliminise warning o ne zatvaranju soketa, nisam siguran u implikacije
	static ServerSocket soket_server = null;
	// sinhrinozovana lista, zbog multithreadovanja
	public static List<ServerThread> aktivni_klijenti = Collections.synchronizedList(new ArrayList<ServerThread>());

	public static void main(String[] args) {
		// terminal pokret
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}

		try {
			// otvara soket servera na zadatom portu
			soket_server = new ServerSocket(port);
			//
			while (true) {
				System.out
						.println("> server online na portu " + port + ";\n==========================================");
				System.out.println("> iscekuje se klijent...\n");
				// otvara soket za konkretnog klijenta
				Socket klijent_soket = soket_server.accept();
				System.out.println("> klijentski soket otvoren;");
				ServerThread novi_klijent = new ServerThread(klijent_soket);
				System.out.println("> thread novog klijenta kreiran;");
				aktivni_klijenti.add(novi_klijent);
				System.out.println("> novi klijent dodat u listu aktivnih klijenata;");
				novi_klijent.start();
				System.out.println("> thread novog klijenta startovan;\n");
			}
		} catch (IOException e) {
			System.err.println(">>> IOException: " + e);
		}

	}

}