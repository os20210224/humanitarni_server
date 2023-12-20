package humanitarni_server;

import java.io.Serializable;

public class Korisnik implements Serializable {
	private static final long serialVersionUID = 1L;
	String username;
	String password;
	String ime;
	String prezime;
	String JMBG;
	String kartica;
	String email;
}
