package modelo;

import java.io.Serializable;
import java.util.HashMap;

public class Carrito implements Serializable {

    private HashMap<Integer, VOLineaVenta> carrito;
    private float total;
    private float iva = 21F;

    /*Estos dos nuevos constructores nos harán la vida más fácil =)*/
    public Carrito() {
        this.carrito = new HashMap<Integer, VOLineaVenta>();
        this.total = 0F;
        //this.iva = 0F;
    }

    public Carrito(Carrito c) {
        this.carrito = new HashMap<Integer, VOLineaVenta>(c.getCarrito());
        this.total = c.getTotal();
        //this.iva = c.getIva();
    }

    public HashMap<Integer, VOLineaVenta> getCarrito() {
        return carrito;
    }

    public void setCarrito(HashMap<Integer, VOLineaVenta> carrito) {
        this.carrito = new HashMap(carrito);
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

    /*Esta función nos calcula el precio total del carrito según los productos que tenga (y sus cantidades).
    Es privada porque está pensada para ser ejecutada por la propia clase tras ciertas operaciones (inserción/borrado).*/
    private void calcTotal() {
        float aux = 0F;
        for (int producto : this.carrito.keySet()) {
            aux += this.carrito.get(producto).getCantidad() * this.carrito.get(producto).getPrecio();
        }
        this.setTotal(aux);
    }

    /*Esta función lo automatiza todo por nosotros. Revisa si ya tenemos el producto en el carrito, en cuyo caso suma
    las cantidades; y agrega el producto y su cantidad si no existía ya.
    Nótese que no verifica el stock, sería una responsabilidad previa.
    En cualquier caso que suponga modificar el carrito, llama a calcTotal().*/
    public void add(VOLineaVenta lc) {
        if (!this.carrito.containsKey(lc.getProducto().getId())) {
            this.carrito.put(lc.getProducto().getId(), lc);
            this.calcTotal();
        } else {
            this.carrito.get(lc.getProducto().getId()).setCantidad(this.carrito.get(lc.getProducto().getId()).getCantidad() + lc.getCantidad());
            this.calcTotal();
        }
    }

    /*Borra un producto del carrito y actualiza el total.*/
    public void remove(int producto) {
        this.carrito.remove(producto);
        this.calcTotal();
    }

    // Devuelve una línea de venta si el producto está en el carrito. Si no, devuelve null. 
    public VOLineaVenta getLineaVenta(int producto) {
        return this.carrito.get(producto);
    }

    // Actualiza la cantidad de una línea del carrito. 
    public void actualizarCantidad(int producto, int cantidad) {
        int nuevaCantidad = this.carrito.get(producto).getCantidad() + cantidad;
        if(nuevaCantidad > 0) {
            this.carrito.get(producto).setCantidad(nuevaCantidad);
            this.calcTotal();
        }
        else this.carrito.remove(producto);
    }
}
