package objekti;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.Socket;

public class Klijent_Info {
	public Socket klijent_soket;
	public BufferedReader od_klijenta;
	public PrintStream ka_klijentu;
	public Korisnik prijavljen_korisnik;
	public boolean kraj;

	public Klijent_Info() {
		this.prijavljen_korisnik = null;
		this.kraj = false;
	}
}