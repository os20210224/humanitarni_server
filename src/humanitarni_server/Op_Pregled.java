package humanitarni_server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Op_Pregled {

	public static void pregled() {
		ServerThread.ka_klijentu.println("==================================================");
		ServerThread.ka_klijentu.println("=               SISTEM ZA DONACIJU               =");
		ServerThread.ka_klijentu.println("=                 Pregled stanja                 =");
		ServerThread.ka_klijentu.println("==================================================");
		int stanje = get_stanje();
		if (stanje == -1)
			ServerThread.ka_klijentu.println("Pregled stanja trenutno nije dostupan.\n");
		else
			ServerThread.ka_klijentu.println("Do sada je skupljeno " + stanje + " dinara!\n");
	}
	// METODE
	static int get_stanje() {
		int s = -1;
		try {
			RandomAccessFile stanje = new RandomAccessFile("stanje.txt", "r");
			s = Integer.parseInt(stanje.readLine());
			stanje.close();
		} catch (FileNotFoundException e) {
			System.err.println("> Greska pri otvaranju stanja");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("> Greska pri zatvaranju stanja");
			e.printStackTrace();
		}
		return s;
	}

}
