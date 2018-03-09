package modelo;

public class VOComentario {
    private int producto;
    private String usuario;
    private String comentario;
    
    public VOComentario(){}
    
    public VOComentario(int producto, String usuario, String comentario){
        this.producto = producto;
        this.usuario = usuario;
        this.comentario = comentario;
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

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    
    
}
