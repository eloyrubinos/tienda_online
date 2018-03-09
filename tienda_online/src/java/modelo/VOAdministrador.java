package modelo;

import java.io.Serializable;

public class VOAdministrador extends Usuario implements Serializable {

    public VOAdministrador() {
        super("admin");
    }

    public VOAdministrador(String nombre) {
        super(nombre, "admin");
    }

    public VOAdministrador(String nombre, String email, String pass) {
        super(nombre, email, pass, null, 0, "cliente");
    }

}
