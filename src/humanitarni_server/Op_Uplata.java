package humanitarni_server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.GregorianCalendar;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import objekti.Klijent_Info;
import objekti.Uplata;

public class Op_Uplata {

	public static void uplata(Klijent_Info k) throws IOException {
		Uplata nova_uplata = new Uplata();
		String kartica;
		// unos osnovnih podataka
		Meni_Header.header(k, Podmeni.UPLATA);
		k.ka_klijentu.println("Ime: ");
		nova_uplata.ime = k.od_klijenta.readLine();
		k.ka_klijentu.println("Prezime: ");
		nova_uplata.prezime = k.od_klijenta.readLine();
		k.ka_klijentu.println("Adresa: ");
		nova_uplata.email = k.od_klijenta.readLine();
		// unos i validacija kartice
		kartica = unos_kartice(k);
		while (!validna_kartica(kartica)) {
			Meni_Header.header(k, Podmeni.UPLATA);
			k.ka_klijentu.println("Podaci nisu validni!");
			kartica = unos_kartice(k);
		}
		// unos i validacija iznosa
		k.ka_klijentu.println("Iznos koji zelite uplatiti: ");
		try {
			nova_uplata.iznos = Integer.parseInt(k.od_klijenta.readLine());
		} catch (NumberFormatException e) {
			nova_uplata.iznos = 0;
		}
		while (nova_uplata.iznos < 200) {
			Meni_Header.header(k, Podmeni.UPLATA);
			k.ka_klijentu.println("Iznos mora biti najmanje broj 200!");
			k.ka_klijentu.println("Iznos koji zelite uplatiti: ");
			try {
				nova_uplata.iznos = Integer.parseInt(k.od_klijenta.readLine());
			} catch (NumberFormatException e) {
				nova_uplata.iznos = 0;
			}
		}
		// jasno iz naziva
		if (dodaj_uplatu(nova_uplata) && apdejtuj_stanje(nova_uplata)) {
			k.ka_klijentu.println("Vasa uplata je uspesno evidentirana!");
			k.ka_klijentu.println("> Fajl");
			k.ka_klijentu.print(formatiraj_uplatu(nova_uplata));
			k.ka_klijentu.println("Dobili ste fajl uplata.txt!");
		}
	}

	public static void p_uplata(Klijent_Info k) {
		Uplata nova_uplata = new Uplata(k.prijavljen_korisnik.ime, k.prijavljen_korisnik.prezime,
				k.prijavljen_korisnik.email);
		String kartica;
		try {
			kartica = unos_CVV(k);
			while (!validna_kartica(kartica)) {
				Meni_Header.header(k, Podmeni.UPLATA);
				k.ka_klijentu.println("Pogresan CVV!");
				kartica = unos_CVV(k);
			}
			// unos i validacija iznosa
			k.ka_klijentu.println("Iznos koji zelite uplatiti: ");
			try {
				nova_uplata.iznos = Integer.parseInt(k.od_klijenta.readLine());
			} catch (NumberFormatException e) {
				nova_uplata.iznos = 0;
			}
			while (nova_uplata.iznos < 200) {
				Meni_Header.header(k, Podmeni.UPLATA);
				k.ka_klijentu.println("Iznos mora biti najmanje broj 200!");
				k.ka_klijentu.println("Iznos koji zelite uplatiti: ");
				try {
					nova_uplata.iznos = Integer.parseInt(k.od_klijenta.readLine());
				} catch (NumberFormatException e) {
					nova_uplata.iznos = 0;
				}
			}
			if (dodaj_uplatu(nova_uplata) && apdejtuj_stanje(nova_uplata)) {
				k.ka_klijentu.println("Vasa uplata je uspesno evidentirana!");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// METODE

	static String unos_CVV(Klijent_Info k) throws IOException {
		k.ka_klijentu.println("Unesite CVV:");
		return k.prijavljen_korisnik.kartica + " " + k.od_klijenta.readLine();
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
			System.out.println("> Greska pri otvaranju liste uplata"); // TODO
			e.printStackTrace();
		} catch (IOException e) {
			status = false;
			System.out.println("> Greska pri zatvaranju liste uplata");// TODO
			e.printStackTrace();
		} finally {
			upis_lock.unlock();
		}
		return status;
	}

	static String formatiraj_uplatu(Uplata uplata) {
		String uplata_string = String.format("| %20s ", uplata.ime) + String.format("| %20s ", uplata.prezime)
				+ String.format("| %30s ", uplata.email)
				+ String.format("| %d.%d.%d %d:%2d ", uplata.vreme.get(GregorianCalendar.DAY_OF_MONTH),
						uplata.vreme.get(GregorianCalendar.MONTH), uplata.vreme.get(GregorianCalendar.YEAR),
						uplata.vreme.get(GregorianCalendar.HOUR_OF_DAY), uplata.vreme.get(GregorianCalendar.MINUTE))
				+ String.format("| %d |\n", uplata.iznos);
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
			System.out.println("> Greska pri otvaranju stanja"); // TODO
			e.printStackTrace();
		} catch (IOException e) {
			status = false;
			System.out.println("> Greska pri zatvaranju stanja");// TODO
			e.printStackTrace();
		} finally {
			upis_lock.unlock();
		}
		return status;
	}

	static String unos_kartice(Klijent_Info k) throws IOException {
		String kartica;
		k.ka_klijentu.println("Broj kreditne kartice: ");
		kartica = k.od_klijenta.readLine() + " ";
		k.ka_klijentu.println("CVV: ");
		kartica += k.od_klijenta.readLine();
		return kartica;
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
			System.err.println("> Greska pri otvaranju baze"); // TODO
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("> Greska pri zatvaranju baze"); // TODO
			e.printStackTrace();
		}
		return false;
	}

}
