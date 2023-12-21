package humanitarni_server;

import java.io.IOException;
import java.io.PrintStream;

public class Meni {

	static void ocisti_ekran(PrintStream tok) { // TODO implementirati ovo zapravo
		tok.print("\033[H\033[2J\n");
		tok.flush();
	}

	// metoda koja pruza glavni meni sa opcijama
	public static void glavni_meni()
			throws IOException {
		while (!ServerThread.kraj) {
			//	TODO neko resenje za ciscenje ekrana konzole
			Meni_Header.header(Podmeni.GLAVNI_MENI);
			ServerThread.ka_klijentu.println("\t1. Uplata");
			ServerThread.ka_klijentu.println("\t2. Registracija");
			ServerThread.ka_klijentu.println("\t3. Prijava");
			ServerThread.ka_klijentu.println("\t4. Pregled stanja");
			ServerThread.ka_klijentu.println("\t5. Pregled transakcija");
			ServerThread.ka_klijentu.println("\t0. Izlaz");
			ServerThread.ka_klijentu.println("--------------------------------------------------");
			ServerThread.ka_klijentu.println("\tIzbor: ");
			int izbor = 7;
			try {
				izbor = Integer.parseInt(ServerThread.od_klijenta.readLine());
			} catch (NumberFormatException e) {
				izbor = 7;
			}
			switch (izbor) {
			case 0:
				ServerThread.kraj = true;
				ServerThread.ka_klijentu.println("> Prijatno;");
				break;
			case 1:
				if(ServerThread.prijavljen_korisnik == null)
					Op_Uplata.uplata();
				else
					Op_Uplata.p_uplata();
				ServerThread.ka_klijentu.println("> Pritisnite Enter za nastavak;");
				ServerThread.od_klijenta.readLine();
				break;
			case 2:
				Op_Registracija.registracija();
				ServerThread.ka_klijentu.println("> Pritisnite Enter za nastavak;");
				ServerThread.od_klijenta.readLine();
				break;
			case 3:
				Op_Prijava.prijava();
				ServerThread.ka_klijentu.println("> Pritisnite Enter za nastavak;");
				ServerThread.od_klijenta.readLine();
				break;
			case 4:
				Op_Pregled.pregled_stanja();
				ServerThread.ka_klijentu.println("> Pritisnite Enter za nastavak;");
				ServerThread.od_klijenta.readLine();
				break;
			case 5:
				Op_Pregled.pregled_transakcija();
				ServerThread.ka_klijentu.println("> Pritisnite Enter za nastavak;");
				ServerThread.od_klijenta.readLine();
				break;
			default:
				ServerThread.ka_klijentu.println("> Morate izabrati stavku izmedju 0 i 5;");
				ServerThread.ka_klijentu.println("> Pritisnite Enter za nastavak;");
				ServerThread.od_klijenta.readLine();
				break;
			}
		}
	}

}
