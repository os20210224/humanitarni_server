package objekti;

import java.io.Serializable;
import java.util.GregorianCalendar;
// implementira jer planiram da presaltam ovo na bin fajlove
public class Uplata implements Serializable {

	private static final long serialVersionUID = 2L;
	public String ime;
	public String prezime;
	public String adresa;
	public GregorianCalendar vreme;
	public int iznos;

	public Uplata() {
		vreme = new GregorianCalendar();
	}

}