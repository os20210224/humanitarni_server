package humanitarni_server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.GregorianCalendar;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UplataMeni {

	public static void uplata_meni(BufferedReader od_klijenta, PrintStream ka_klijentu) {
		Uplata nova_uplata = new Uplata();
		String kartica;
		try {
			// unos osnovnih podataka
			header(ka_klijentu);
			ka_klijentu.println("Ime: ");
			nova_uplata.ime = od_klijenta.readLine();
			ka_klijentu.println("Prezime: ");
			nova_uplata.prezime = od_klijenta.readLine();
			ka_klijentu.println("Adresa: ");
			nova_uplata.adresa = od_klijenta.readLine();
			// unos i validacija kartice
			kartica = unos_kartice(od_klijenta, ka_klijentu);
			while (!validna_kartica(kartica)) {
				header(ka_klijentu);
				ka_klijentu.println("Podaci nisu validni!");
				kartica = unos_kartice(od_klijenta, ka_klijentu);
			}
			// unos i validacija iznosa
			ka_klijentu.println("Iznos koji zelite uplatiti: ");
			try {
				nova_uplata.iznos = Integer.parseInt(od_klijenta.readLine());
			} catch (NumberFormatException e) {
				nova_uplata.iznos = 0;
			}
			while (nova_uplata.iznos < 200) {
				header(ka_klijentu);
				ka_klijentu.println("Iznos mora biti najmanje broj 200!");
				ka_klijentu.println("Iznos koji zelite uplatiti: ");
				try {
					nova_uplata.iznos = Integer.parseInt(od_klijenta.readLine());
				} catch (NumberFormatException e) {
					nova_uplata.iznos = 0;
				}
			}
			// jasno iz naziva
			if (dodaj_uplatu(nova_uplata) && apdejtuj_stanje(nova_uplata)) {
				ka_klijentu.println("Vasa uplata je uspesno evidentirana!");
			}
		} catch (IOException e) {
			System.err.println("> Greska u vezi sa klijentom;");
			e.printStackTrace();
		}
	}

	// METODE
	
	static boolean posalji_fajl(Uplata uplata) {
		boolean stanje = true;
	
		String us = formatiraj_uplatu(uplata);
		// posalji fajl preko TCPa :D
		
		return stanje;
	}

	static boolean dodaj_uplatu(Uplata uplata) {
		boolean status = true;
		ReadWriteLock lock = new ReentrantReadWriteLock();
		Lock upis_lock = lock.writeLock();
		try {
			upis_lock.lock();
			RandomAccessFile uplate = new RandomAccessFile("uplate.txt", "rw");

			String uplata_string = formatiraj_uplatu(uplata);

			uplate.seek(uplate.length());
			uplate.writeBytes(uplata_string);

			uplate.close();
		} catch (FileNotFoundException e) {
			status = false;
			System.out.println("> Greska pri otvaranju liste uplata");
			e.printStackTrace();
		} catch (IOException e) {
			status = false;
			System.out.println("> Greska pri zatvaranju liste uplata");
			e.printStackTrace();
		} finally {
			upis_lock.unlock();
		}
		return status;
	}

	static String formatiraj_uplatu(Uplata uplata) {
		String uplata_string = String.format("Ime: %20s ", uplata.ime)
				+ String.format("| Prezime: %20s |", uplata.prezime) + String.format("Adresa: %30s |", uplata.adresa)
				+ String.format("Datum i vreme: %d.%d.%d %d:%d |", uplata.vreme.get(GregorianCalendar.DAY_OF_MONTH),
						uplata.vreme.get(GregorianCalendar.MONTH), uplata.vreme.get(GregorianCalendar.YEAR),
						uplata.vreme.get(GregorianCalendar.HOUR_OF_DAY), uplata.vreme.get(GregorianCalendar.MINUTE))
				+ String.format("Iznos: %d |\n", uplata.iznos);
		return uplata_string;
	}
	
	static boolean apdejtuj_stanje(Uplata uplata) {
		boolean status = true;
		ReadWriteLock lock = new ReentrantReadWriteLock();
		Lock upis_lock = lock.writeLock();
		try {
			upis_lock.lock();
			RandomAccessFile stanje = new RandomAccessFile("stanje.txt", "rw");

			int iznos = Integer.parseInt(stanje.readLine());
			stanje.seek(0);
			iznos += uplata.iznos;
			stanje.writeBytes(Integer.toString(iznos) + "\n");

			stanje.close();
		} catch (FileNotFoundException e) {
			status = false;
			System.out.println("> Greska pri otvaranju stanja");
			e.printStackTrace();
		} catch (IOException e) {
			status = false;
			System.out.println("> Greska pri zatvaranju stanja");
			e.printStackTrace();
		} finally {
			upis_lock.unlock();
		}
		return status;
	}

	static String unos_kartice(BufferedReader od_klijenta, PrintStream ka_klijentu) throws IOException {
		String kartica;
		ka_klijentu.println("Broj kreditne kartice: ");
		kartica = od_klijenta.readLine() + " ";
		ka_klijentu.println("CVV: ");
		kartica += od_klijenta.readLine();
		return kartica;
	}

	static void header(PrintStream ka_klijentu) {
		ka_klijentu.println("==================================================");
		ka_klijentu.println("=               SISTEM ZA DONACIJU               =");
		ka_klijentu.println("=                     Uplata                     =");
		ka_klijentu.println("==================================================");
	}

	static boolean validna_kartica(String kartica) {
		try {
			RandomAccessFile lista_kartica = new RandomAccessFile("baza.txt", "r");
			String k;
			while ((k = lista_kartica.readLine()) != null) {
				if (k.contains(kartica)) {
					lista_kartica.close();
					return true;
				}
			}
			lista_kartica.close();
		} catch (FileNotFoundException e) {
			System.err.println("> Greska pri otvaranju baze");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("> Greska pri zatvaranju baze");
			e.printStackTrace();
		}
		return false;
	}

}

// OBJEKAT

class Uplata {

	String ime;
	String prezime;
	String adresa;
	GregorianCalendar vreme;
	int iznos;

	public Uplata() {
		vreme = new GregorianCalendar();
	}

}