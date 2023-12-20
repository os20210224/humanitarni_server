package objekti;

import java.io.Serializable;
import java.util.GregorianCalendar;

// implementira jer planiram da presaltam ovo na bin fajlove
public class Uplata implements Serializable {

	private static final long serialVersionUID = 2L;
	public String ime;
	public String prezime;
	public String email;
	public GregorianCalendar vreme;
	public int iznos;

	public Uplata() {
		this.vreme = new GregorianCalendar();
	}

	public Uplata(String ime, String prezime, String email) {
		this.ime = ime;
		this.prezime = prezime;
		this.email = email;
		this.vreme = new GregorianCalendar();
	}

}