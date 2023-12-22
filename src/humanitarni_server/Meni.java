package humanitarni_server;

import java.io.IOException;

import objekti.Klijent_Info;

public class Meni {

	// metoda koja pruza glavni meni sa opcijama
	public static void glavni_meni(Klijent_Info k) throws IOException { // ovde je bug sto thread ostane upaljen ako se
																		// klijent ugasi pre interakcije sa menijem;
																		// tenutno nemamideju kako to resiti
		while (!k.kraj) {
			Meni_Header.header(k, Podmeni.GLAVNI_MENI);
			k.ka_klijentu.println("\t1. Uplata");
			k.ka_klijentu.println("\t2. Registracija");
			k.ka_klijentu.println("\t3. Prijava");
			k.ka_klijentu.println("\t4. Pregled stanja");
			k.ka_klijentu.println("\t5. Pregled transakcija");
			k.ka_klijentu.println("\t0. Izlaz");
			k.ka_klijentu.println("--------------------------------------------------");
			k.ka_klijentu.println("\tIzbor: ");
			int izbor = 7;
			try {
				izbor = Integer.parseInt(k.od_klijenta.readLine());
			} catch (NumberFormatException e) {
				izbor = 7;
			}
			switch (izbor) {
			case 0:
				k.kraj = true;
				k.prijavljen_korisnik = null;
				k.ka_klijentu.println("> Prijatno;");
				break;
			case 1:
				if (k.prijavljen_korisnik == null)
					Op_Uplata.uplata(k);
				else
					Op_Uplata.p_uplata(k);
				k.ka_klijentu.println("> Pritisnite Enter za nastavak;");
				k.od_klijenta.readLine();
				break;
			case 2:
				Op_Registracija.registracija(k);
				k.ka_klijentu.println("> Pritisnite Enter za nastavak;");
				k.od_klijenta.readLine();
				break;
			case 3:
				Op_Prijava.prijava(k);
				k.ka_klijentu.println("> Pritisnite Enter za nastavak;");
				k.od_klijenta.readLine();
				break;
			case 4:
				Op_Pregled.pregled_stanja(k);
				k.ka_klijentu.println("> Pritisnite Enter za nastavak;");
				k.od_klijenta.readLine();
				break;
			case 5:
				Op_Pregled.pregled_transakcija(k);
				k.ka_klijentu.println("> Pritisnite Enter za nastavak;");
				k.od_klijenta.readLine();
				break;
			default:
				k.ka_klijentu.println("> Morate izabrati stavku izmedju 0 i 5;");
				k.ka_klijentu.println("> Pritisnite Enter za nastavak;");
				k.od_klijenta.readLine();
				break;
			}
		}
	}

}
