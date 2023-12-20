package humanitarni_server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Op_Pregled {

	public static void pregled_stanja() {
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

	public static void pregled_transakcija() {
		ServerThread.ka_klijentu.println("==================================================");
		ServerThread.ka_klijentu.println("=               SISTEM ZA DONACIJU               =");
		ServerThread.ka_klijentu.println("=               Pregled transakcija              =");
		ServerThread.ka_klijentu.println("==================================================");
		if (ServerThread.prijavljen_korisnik == null) {
			ServerThread.ka_klijentu.println("Morate biti ulogovani da bi videli transakcije!");
			return;
		}
		if (!transakcije()) {
			ServerThread.ka_klijentu.println("Doslo je do greske pri ucitavanju transakcija.");
		}
	}
	// METODE

	static boolean transakcije() {
		ArrayList<String> lista = new ArrayList<String>();
		boolean status = true;
		ReadWriteLock lock = new ReentrantReadWriteLock();
		Lock upis_lock = lock.writeLock();
		try {
			upis_lock.lock();
			RandomAccessFile uplate;
			uplate = new RandomAccessFile("uplate.txt", "rw");
			
			String u;
//			String header = uplate.readLine();
			while ((u = uplate.readLine()) != null) {
				lista.add(0, u);
			}
			uplate.close();

			ServerThread.ka_klijentu.println("Evo najskorijih transakcija:");
			
//			ServerThread.ka_klijentu.println(header);

			int end = Math.min(10, lista.size() - 1);
			for (int i = 0; i < end; i++) {
				ServerThread.ka_klijentu.println("\t" + lista.get(i));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			status = false;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			status = false;
			e.printStackTrace();
		} finally {
			upis_lock.unlock();
		}
		return status;
	}

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
