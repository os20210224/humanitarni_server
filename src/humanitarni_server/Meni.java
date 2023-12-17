package humanitarni_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class Meni {

	static void ocisti_ekran(PrintStream tok) {
		tok.print("\033[H\033[2J\n");
		tok.flush();
	}

	// metoda koja pruza glavni meni sa opcijama
	public static void glavni_meni(boolean izlaz, BufferedReader od_klijenta, PrintStream ka_klijentu)
			throws IOException {
		while (!izlaz) {
			//	neko resenje za ciscenje ekrana konzole
			ka_klijentu.println("==================================================");
			ka_klijentu.println("=               SISTEM ZA DONACIJU               =");
			ka_klijentu.println("==================================================");
			ka_klijentu.println("\t1. Uplata");
			ka_klijentu.println("\t2. Registracija");
			ka_klijentu.println("\t3. Prijava");
			ka_klijentu.println("\t4. Pregled stanja");
			ka_klijentu.println("\t5. Pregled transakcija");
			ka_klijentu.println("\t0. Izlaz");
			ka_klijentu.println("--------------------------------------------------");
			ka_klijentu.println("\tIzbor: ");
			int izbor = Integer.parseInt(od_klijenta.readLine());
			switch (izbor) {
			case 0:
				izlaz = true;
				ka_klijentu.println("> Prijatno;");
				od_klijenta.read();
				break;
			case 1:
				ka_klijentu.println("> uplata placehodler;");
				break;
			case 2:
				ka_klijentu.println("> registracija placehodler;");
				break;
			case 3:
				ka_klijentu.println("> prijava placehodler;");
				break;
			case 4:
				ka_klijentu.println("> pregled stanja placehodler;");
				break;
			case 5:
				ka_klijentu.println("> pregled transakcija placehodler;");
				break;
			default:
				ka_klijentu.println("> " + izbor + " nije validan izbor;");
				od_klijenta.read();
				break;
			}
		}
	}

}
