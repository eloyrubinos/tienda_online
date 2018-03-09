package modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class VOVenta implements Serializable {
    private int id;
    private VOCliente c;
    private ArrayList<VOLineaVenta> lvs;
    private float total;
    private float iva;
    private String fecha;
    
    public VOVenta(){
        lvs = new ArrayList();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VOCliente getC() {
        return c;
    }

    public void setC(VOCliente c) {
        this.c = c;
    }

    public ArrayList<VOLineaVenta> getLvs() {
        return lvs;
    }

    public void setLvs(ArrayList<VOLineaVenta> lvs) {
        this.lvs = new ArrayList(lvs);
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getIva() {
        return iva;
    }

    public void setIva(float iva) {
        this.iva = iva;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
}