package humanitarni_server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import exceptions.SrvGreskaException;
import objekti.Klijent_Info;

public class Op_Pregled {

	public static void pregled_stanja(Klijent_Info k) {
		Meni_Header.header(k, Podmeni.PREGLED_STANJA);
		int stanje = get_stanje(k);
		if (stanje == -1)
			k.ka_klijentu.println("Pregled stanja trenutno nije dostupan.\n");
		else
			k.ka_klijentu.println("Do sada je skupljeno " + stanje + " dinara!\n");
	}

	public static void pregled_transakcija(Klijent_Info k) {
		Meni_Header.header(k, Podmeni.PREGLED_TRANSAKCIJA);
		if (k.prijavljen_korisnik == null) {
			k.ka_klijentu.println("Morate biti ulogovani da bi videli transakcije!");
			return;
		}
		transakcije(k);
	}

	// METODE

	static boolean transakcije(Klijent_Info k) {
		ArrayList<String> lista = new ArrayList<String>();
		boolean status = true;
		try {
			RandomAccessFile uplate;
			uplate = new RandomAccessFile("uplate.txt", "r");

			String u;

			while ((u = uplate.readLine()) != null) {
				lista.add(0, u);
			}
			uplate.close();

			k.ka_klijentu.println("Evo najskorijih transakcija:");

			int end = Math.min(10, lista.size());
			for (int i = 0; i < end; i++) {
				k.ka_klijentu.println("\t" + lista.get(i));
			}
		} catch (FileNotFoundException e) {
			System.err.println("> Greska pri otvaranju stanja");
			SrvGreskaException.srv_greska_exception(k);
			status = false;
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("> Greska pri otvaranju stanja");
			SrvGreskaException.srv_greska_exception(k);
			status = false;
			e.printStackTrace();
		}
		return status;
	}

	static int get_stanje(Klijent_Info k) {
		int s = -1;
		try {
			RandomAccessFile stanje = new RandomAccessFile("stanje.txt", "r");
			s = Integer.parseInt(stanje.readLine());
			stanje.close();
		} catch (FileNotFoundException e) {
			System.err.println("> Greska pri otvaranju stanja");
			SrvGreskaException.srv_greska_exception(k);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("> Greska pri zatvaranju stanja");
			SrvGreskaException.srv_greska_exception(k);
			e.printStackTrace();
		}
		return s;
	}

}
