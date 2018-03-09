package modelo;

import java.io.Serializable;

public abstract class Usuario implements Serializable {

    private String nombre;
    private String email;
    private String pass;
    private String rango;
    private float gastoTotal;
    private final String tipo;

    public Usuario(String tipo) {
        this.tipo = tipo;
    }
    
    public Usuario(String nombre, String tipo) {
        this.nombre = nombre; 
        this.tipo = tipo; 
    }

    public Usuario(String nombre, String email, String pass, String rango, float gastoTotal, String tipo) {
        this.nombre = nombre;
        this.email = email;
        this.pass = pass;
        this.rango = rango;
        this.gastoTotal = gastoTotal;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRango() {
        return rango;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }

    public String getTipo() {
        return tipo;
    }

    public float getGastoTotal() {
        return gastoTotal;
    }

    public void setGastoTotal(float gastoTotal) {
        this.gastoTotal = gastoTotal;
    }

}
