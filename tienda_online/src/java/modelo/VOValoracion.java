package modelo;

public class VOValoracion {
    private int producto;
    private String usuario;
    private int valoracion;
    
    public VOValoracion(){}
    
    public VOValoracion(int producto, String usuario, int valoracion){
        this.producto = producto;
        this.usuario = usuario;
        this.valoracion = valoracion;
    }

    public int getProducto() {
        return producto;
    }

    public void setProducto(int producto) {
        this.producto = producto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }
    
    
}
