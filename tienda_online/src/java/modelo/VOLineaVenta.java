package modelo;

import java.io.Serializable;

public class VOLineaVenta implements Serializable {

    private VOProducto producto;
    private int cantidad;
    private float precio;

    public VOLineaVenta(VOProducto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = producto.getPrecioUnitario();
    }

    public VOProducto getProducto() {
        return producto;
    }

    public void setProducto(VOProducto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

}
