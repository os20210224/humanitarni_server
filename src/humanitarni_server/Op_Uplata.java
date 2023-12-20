package humanitarni_server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.GregorianCalendar;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import objekti.Uplata;

public class Op_Uplata {

	public static void uplata() {
		Uplata nova_uplata = new Uplata();
		String kartica;
		try {
			// unos osnovnih podataka
			header();
			ServerThread.ka_klijentu.println("Ime: ");
			nova_uplata.ime = ServerThread.od_klijenta.readLine();
			ServerThread.ka_klijentu.println("Prezime: ");
			nova_uplata.prezime = ServerThread.od_klijenta.readLine();
			ServerThread.ka_klijentu.println("Adresa: ");
			nova_uplata.email = ServerThread.od_klijenta.readLine();
			// unos i validacija kartice
			kartica = unos_kartice();
			while (!validna_kartica(kartica)) {
				header();
				ServerThread.ka_klijentu.println("Podaci nisu validni!");
				kartica = unos_kartice();
			}
			// unos i validacija iznosa
			ServerThread.ka_klijentu.println("Iznos koji zelite uplatiti: ");
			try {
				nova_uplata.iznos = Integer.parseInt(ServerThread.od_klijenta.readLine());
			} catch (NumberFormatException e) {
				nova_uplata.iznos = 0;
			}
			while (nova_uplata.iznos < 200) {
				header();
				ServerThread.ka_klijentu.println("Iznos mora biti najmanje broj 200!");
				ServerThread.ka_klijentu.println("Iznos koji zelite uplatiti: ");
				try {
					nova_uplata.iznos = Integer.parseInt(ServerThread.od_klijenta.readLine());
				} catch (NumberFormatException e) {
					nova_uplata.iznos = 0;
				}
			}
			// jasno iz naziva
			if (dodaj_uplatu(nova_uplata) && apdejtuj_stanje(nova_uplata)) {
				ServerThread.ka_klijentu.println("Vasa uplata je uspesno evidentirana!");
			}
		} catch (IOException e) {
			System.err.println("> Greska u vezi sa klijentom;");
			e.printStackTrace();
		}
	}

	public static void p_uplata() {
		Uplata nova_uplata = new Uplata(ServerThread.prijavljen_korisnik.ime, ServerThread.prijavljen_korisnik.prezime,
				ServerThread.prijavljen_korisnik.email);
		String kartica;
		try {
			kartica = unos_CVV();
			while (!validna_kartica(kartica)) {
				header();
				ServerThread.ka_klijentu.println("Pogresan CVV!");
				kartica = unos_CVV();
			}
			// unos i validacija iznosa
			ServerThread.ka_klijentu.println("Iznos koji zelite uplatiti: ");
			try {
				nova_uplata.iznos = Integer.parseInt(ServerThread.od_klijenta.readLine());
			} catch (NumberFormatException e) {
				nova_uplata.iznos = 0;
			}
			while (nova_uplata.iznos < 200) {
				header();
				ServerThread.ka_klijentu.println("Iznos mora biti najmanje broj 200!");
				ServerThread.ka_klijentu.println("Iznos koji zelite uplatiti: ");
				try {
					nova_uplata.iznos = Integer.parseInt(ServerThread.od_klijenta.readLine());
				} catch (NumberFormatException e) {
					nova_uplata.iznos = 0;
				}
			}
			if (dodaj_uplatu(nova_uplata) && apdejtuj_stanje(nova_uplata)) {
				ServerThread.ka_klijentu.println("Vasa uplata je uspesno evidentirana!");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// METODE

	static String unos_CVV() throws IOException {
		ServerThread.ka_klijentu.println("Unesite CVV:");
		return ServerThread.prijavljen_korisnik.kartica + " " + ServerThread.od_klijenta.readLine();
	}

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
		String uplata_string = String.format("| Ime: %20s ", uplata.ime)
				+ String.format("| Prezime: %20s |", uplata.prezime) + String.format("Adresa: %30s |", uplata.email)
				+ String.format("Datum i vreme: %d.%d.%d %d:%2d |", uplata.vreme.get(GregorianCalendar.DAY_OF_MONTH),
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

	static String unos_kartice() throws IOException {
		String kartica;
		ServerThread.ka_klijentu.println("Broj kreditne kartice: ");
		kartica = ServerThread.od_klijenta.readLine() + " ";
		ServerThread.ka_klijentu.println("CVV: ");
		kartica += ServerThread.od_klijenta.readLine();
		return kartica;
	}

	static void header() {
		ServerThread.ka_klijentu.println("==================================================");
		ServerThread.ka_klijentu.println("=               SISTEM ZA DONACIJU               =");
		ServerThread.ka_klijentu.println("=                     Uplata                     =");
		ServerThread.ka_klijentu.println("==================================================");
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
