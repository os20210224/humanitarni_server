package humanitarni_server;

import java.io.*;
import java.net.*;

public class Server {
	// broj porta na kome ce se otvoriti server soket
	static int port = 7259;
	// ovo eliminise warning o ne zatvaranju soketa, nisam siguran u implikacije
	static ServerSocket soket_server = null;

	public static void main(String[] args) {
		// terminal pokret
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}

		try {
			// otvara soket servera na zadatom portu
			soket_server = new ServerSocket(port);
			System.out.println("> server online na portu " + port + ";");
			System.out.println("==========================================");
			//
			while (true) {
				System.out.println("> iscekuje se klijent...\n");
				// otvara soket za konkretnog klijenta
				Socket klijent_soket = soket_server.accept();
				System.out.println("> klijentski soket otvoren;");
				ServerThread novi_klijent = new ServerThread(klijent_soket);
				System.out.println("> thread novog klijenta kreiran;");
				novi_klijent.start();
				System.out.println("> thread novog klijenta startovan;\n");
			}
		} catch (IOException e) {
			System.err.println(">>> IOException: " + e);
			e.printStackTrace();
		}

	}

}