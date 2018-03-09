package modelo;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
 
public class DAOProducto {
    private final String url_bd = "jdbc:mysql://localhost:3306/bd_dawa";
    private String usuario;
    private String pass;
    private Connection con;
    private PreparedStatement insert;
    private PreparedStatement actualizar;
    private PreparedStatement delete;
     
    public DAOProducto(String usuario, String pass) throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.jdbc.Driver"); //Carga del controlador MySQL
        this.usuario = usuario;
        this.pass = pass;
        this.con = DriverManager.getConnection(url_bd, this.usuario, this.pass);
        con.setTransactionIsolation(4);
        insert = con.prepareStatement("insert into PRODUCTOS "
                                        +"(id, nombre, artista, pais, precio, ano, stock) "
                                        +"values(?, ?, ?, ?, ?, ?, ?)");
        actualizar = con.prepareStatement("update PRODUCTOS set nombre = ?, artista = ?, pais = ?, precio = ?, ano = ?, stock = ? where id = ?");
        delete = con.prepareStatement("delete from PRODUCTOS where id = ?");
    }
     
    public String getUrl_bd() {
        return url_bd;
    }
 
    public String getUsuario() {
        return usuario;
    }
 
    public String getPass() {
        return pass;
    }
 
    public Connection getCon() {
        return con;
    }
     
     /*Compruebo si un producto es duplicado*/
    public boolean isDuplicate(VOProducto p) throws SQLException{
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select COUNT(*) from PRODUCTOS where nombre = '"+p.getNombre()
                                        +"' AND artista = '"+p.getArtista()+"' AND pais = '"+p.getPais()
                                        +"' AND precio = '"+p.getPrecioUnitario()+"' AND ano = '"+p.getAno()+"'");
        set.next();
        if(set.getInt(1) > 0) return true;
        else return false;
    }
    
    /*Busco un producto por su ID.*/
    public VOProducto getProducto(int id) throws SQLException{
        VOProducto p = new VOProducto();
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from PRODUCTOS where id = '"+id+"'");
        set.next();
        p.setId(set.getInt(1));
        p.setNombre(set.getString(2));
        p.setArtista(set.getString(3));
        p.setPais(set.getString(4));
        p.setPrecioUnitario(set.getFloat(5));
        p.setAno(set.getString(6));
        p.setStock(set.getInt(7));
        st.close();
        set.close();
        return p;
    }
     
    /*Pido todos los productos.*/
    public HashMap<Integer, VOProducto> getProductos() throws SQLException{
        HashMap<Integer, VOProducto> productos = new HashMap();
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from PRODUCTOS");
         
        while(set.next()){
            VOProducto p = new VOProducto();
            p.setId(set.getInt(1));
            p.setNombre(set.getString(2));
            p.setArtista(set.getString(3));
            p.setPais(set.getString(4));
            p.setPrecioUnitario(set.getFloat(5));
            p.setAno(set.getString(6));
            p.setStock(set.getInt(7));
            productos.put(p.getId(), p);
        }
         
        st.close();
        set.close();
         
        return productos;
    }
     
    /*Pido todos los productos en stock (stock > 0)*/
    public HashMap<Integer, VOProducto> getProductosDisponibles() throws SQLException{
        HashMap<Integer, VOProducto> productos = new HashMap();
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from PRODUCTOS where stock > 0");
         
        while(set.next()){
            VOProducto p = new VOProducto();
            p.setId(set.getInt(1));
            p.setNombre(set.getString(2));
            p.setArtista(set.getString(3));
            p.setPais(set.getString(4));
            p.setPrecioUnitario(set.getFloat(5));
            p.setAno(set.getString(6));
            p.setStock(set.getInt(7));
            productos.put(p.getId(), p);
        }
         
        st.close();
        set.close();
         
        return productos;
    }
     
    /*Guardo un producto en la BD. Da igual qué ID se le haya metido al Producto al crearlo (o que vaya vacío), ya que esta función determina qué ID le corresponde y lo actualiza antes de insertarlo en la BD.*/
    public void add(VOProducto p) throws SQLException{
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select max(id) from PRODUCTOS");
        set.next();
        p.setId(set.getInt(1)+1);
         
        st.close();
        set.close();
         
        insert.setInt(1, p.getId());
        insert.setString(2, p.getNombre());
        insert.setString(3, p.getArtista());
        insert.setString(4, p.getPais());
        insert.setFloat(5, p.getPrecioUnitario());
        insert.setString(6, p.getAno());
        insert.setInt(7, p.getStock());
        insert.executeUpdate();
    }
     
    /*Hace lo mismo que la función para un único Producto, pero con un Array.
    Se usa un ArrayList porque los HashMap usan el ID del producto como clave, y a la hora de insertar un nuevo producto no tenemos este dato; es recomendable
    dejar ese campo por defecto o vacío y crear un AL.*/
    public void add(ArrayList<VOProducto> productos) throws SQLException{
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select max(id) from PRODUCTOS");
        set.next();
        int idcount = set.getInt(1);
         
        st.close();
        set.close();
         
        for(VOProducto p : productos){
            idcount++;
            p.setId(idcount);
            insert.setInt(1, p.getId());
            insert.setString(2, p.getNombre());
            insert.setString(3, p.getArtista());
            insert.setString(4, p.getPais());
            insert.setFloat(5, p.getPrecioUnitario());
            insert.setString(6, p.getAno());
            insert.setInt(7, p.getStock());
            insert.addBatch();
        }
        insert.executeBatch();
    }
     
    /*Actualizo los datos de un producto.*/
    public void actualizarProducto(VOProducto p) throws SQLException{
        actualizar.setString(1, p.getNombre());
        actualizar.setString(2, p.getArtista());
        actualizar.setString(3, p.getPais());
        actualizar.setFloat(4, p.getPrecioUnitario());
        actualizar.setString(5, p.getAno());
        actualizar.setInt(6, p.getStock());
        actualizar.setInt(7, p.getId());
        actualizar.executeUpdate();
    }
     
    /*Actualizo los datos de varios productos.*/
    public void actualizarProductos(HashMap<Integer, VOProducto> productos) throws SQLException{       
        for(int id : productos.keySet()){
            actualizar.setString(1, productos.get(id).getNombre());
            actualizar.setString(2, productos.get(id).getArtista());
            actualizar.setString(3, productos.get(id).getPais());
            actualizar.setFloat(4, productos.get(id).getPrecioUnitario());
            actualizar.setString(5, productos.get(id).getAno());
            actualizar.setInt(6, productos.get(id).getStock());
            actualizar.setInt(7, id);
            actualizar.addBatch();
        }
         
        actualizar.executeBatch();
    }
     
    /*Obtengo el ResultSet ACTUALIZABLE que contiene el stock actual (en BD) de un producto.*/
    public ResultSet getActuStock(int id) throws SQLException{
        Statement st = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet set = st.executeQuery("select * from PRODUCTOS where id = "+id+"");
        set.next();
        
        return set; 
    }
     
    /*Borro un producto.*/
    public void delete(VOProducto p) throws SQLException{
        con.setAutoCommit(false);
         
        Statement st = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet set = st.executeQuery("select id from PRODUCTOS where id > "+p.getId()+"");
         
        delete.setInt(1, p.getId());
        delete.executeUpdate();
         
        while(set.next()){
            set.updateInt(1, set.getInt(1)-1);
            set.updateRow();
        }
         
        con.commit();
         
        st.close();
        set.close();
         
        con.setAutoCommit(true);
    }
     
    /*Cierro todas las conexiones. Debe llamarse SIEMPRE al menos antes de terminar el programa, para cada DAOProducto creado.*/
    public void cerrar() throws SQLException{
        insert.close();
        delete.close();
        actualizar.close();
        this.con.close();
    }
}