package modelo;
 
import java.io.Serializable;
 
public class VOProducto implements Serializable {
 
    private int id;
    private String nombre;
    private String artista;
    private String pais;
    private float precioUnitario;
    private String ano;
    private int stock;
     
    public VOProducto(){}

    // Para crear un producto a partir de un formulario
    public VOProducto(int id, String nombre, String artista, String pais, float precioUnitario, String ano, int stock) {
        this.id = id; 
        this.nombre = nombre;
        this.artista = artista;
        this.pais = pais;
        this.precioUnitario = precioUnitario;
        this.ano = ano;
        this.stock = stock;
    }

    // Para crear un dummy con propósitos identificativos
    public VOProducto(int id) {
        this.id = id;
    }
     
    public int getId(){
        return id;
    }
     
    public void setId(int id){
        this.id = id;
    }
     
    public String getNombre() {
        return nombre;
    }
 
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
 
    public String getArtista() {
        return artista;
    }
 
    public void setArtista(String artista) {
        this.artista = artista;
    }
 
    public String getPais() {
        return pais;
    }
 
    public void setPais(String pais) {
        this.pais = pais;
    }
 
    public float getPrecioUnitario() {
        return precioUnitario;
    }
 
    public void setPrecioUnitario(float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
     
    public int getStock(){
        return stock;
    }
     
    public void setStock(int stock){
        this.stock = stock;
    }
     
    public String getAno(){
        return ano;
    }
     
    public void setAno(String ano){
        this.ano = ano;
    }
 
}