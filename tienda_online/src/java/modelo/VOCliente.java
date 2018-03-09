package modelo;

import java.io.Serializable;

public class VOCliente extends Usuario implements Serializable {

    public VOCliente(){
        super("cliente");
    }
    
    public VOCliente(String nombre) {
        super(nombre, "cliente"); 
    }

    public VOCliente(String nombre, String email, String pass, String rango, Float gastoTotal) {
        super(nombre, email, pass, rango, gastoTotal, "cliente");
    }
    
}