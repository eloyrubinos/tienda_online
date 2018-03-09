package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class DAOComentario {
    private final String url_bd = "jdbc:mysql://localhost:3306/bd_dawa";
    private String usuario;
    private String pass;
    private Connection con;
    private PreparedStatement insert;
    private PreparedStatement actualizar;
    private PreparedStatement delete;
    
    public DAOComentario(String usuario, String pass) throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.jdbc.Driver"); //Carga del controlador MySQL
        this.usuario = usuario;
        this.pass = pass;
        this.con = DriverManager.getConnection(url_bd, this.usuario, this.pass);
        con.setTransactionIsolation(4);
        insert = con.prepareStatement("insert into COMENTARIOS "
                                        +"(producto, usuario, comentario) "
                                        +"values(?, ?, ?)");
        actualizar = con.prepareStatement("update COMENTARIOS set comentario = ? where producto = ? AND usuario = ?");
        delete = con.prepareStatement("delete from COMENTARIOS where producto = ? AND usuario = ?");
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
    
    /*Obtengo el comentario de un usuario sobre un producto.*/
    public VOComentario getComentario(int producto, String usuario) throws SQLException{
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from COMENTARIOS where producto = '"+producto+"' AND usuario = '"+usuario+"'");
        set.next();
        VOComentario co = new VOComentario(set.getInt(1), set.getString(2), set.getString(3));
        st.close();
        set.close();
        return co;
    }
    
    /*Obtengo todos los comentarios de un producto (de todos los usuarios).*/
    public ArrayList<VOComentario> getComentarios(int producto) throws SQLException{
        ArrayList<VOComentario> comentarios = new ArrayList();
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from COMENTARIOS where producto = '"+producto+"'");
        
        while(set.next()){
            VOComentario co = new VOComentario(set.getInt(1), set.getString(2), set.getString(3));
            comentarios.add(co);
        }
        
        st.close();
        set.close();
        
        return comentarios;
    }
    
    /*Obtengo todos los comentarios de un usuario (sobre todos los productos).*/
    public ArrayList<VOComentario> getComentarios(String usuario) throws SQLException{
        ArrayList<VOComentario> comentarios = new ArrayList();
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from COMENTARIOS where usuario = '"+usuario+"'");
        
        while(set.next()){
            VOComentario co = new VOComentario(set.getInt(1), set.getString(2), set.getString(3));
            comentarios.add(co);
        }
        
        st.close();
        set.close();
        
        return comentarios;
    }
    
    /*Obtengo todos los comentarios de un cliente (sobre todos los productos), esta vez como HashMap con clave = el ID del producto en cuestión.*/
    public HashMap<Integer, VOComentario> getComentariosHash(String usuario) throws SQLException{
        HashMap<Integer, VOComentario> comentarios = new HashMap();
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from COMENTARIOS where usuario = '"+usuario+"'");
        
        while(set.next()){
            VOComentario co = new VOComentario(set.getInt(1), set.getString(2), set.getString(3));
            comentarios.put(set.getInt(1), co);
        }
        
        st.close();
        set.close();
        
        return comentarios;
    }
    
    /*Obtengo TODOS los comentarios (de todos los productos y usuarios).*/
    public ArrayList<VOComentario> getComentarios() throws SQLException{
        ArrayList<VOComentario> comentarios = new ArrayList();
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from COMENTARIOS");
        
        while(set.next()){
            VOComentario co = new VOComentario(set.getInt(1), set.getString(2), set.getString(3));
            comentarios.add(co);
        }
        
        st.close();
        set.close();
        
        return comentarios;
    }
    
    /*Inserto un comentario.*/
    public void add(VOComentario co) throws SQLException{
        insert.setInt(1, co.getProducto());
        insert.setString(2, co.getUsuario());
        insert.setString(3, co.getComentario());
        insert.executeUpdate();
    }
    
    /*Inserto varios comentarios.*/
    public void add(ArrayList<VOComentario> comentarios) throws SQLException{
        for(VOComentario co : comentarios){
            insert.setInt(1, co.getProducto());
            insert.setString(2, co.getUsuario());
            insert.setString(3, co.getComentario());
            insert.addBatch();
        }
        insert.executeBatch();
    }
    
    /*Actualizo un comentario.*/
    public void actualizarComentario(VOComentario co) throws SQLException{
        actualizar.setString(1, co.getComentario());
        actualizar.setInt(2, co.getProducto());
        actualizar.setString(3, co.getUsuario());
        actualizar.executeUpdate();
    }
    
    /*Actualizo varios comentarios.*/
    public void actualizarComentarios(ArrayList<VOComentario> comentarios) throws SQLException{
       for(VOComentario co : comentarios){
           actualizar.setString(1, co.getComentario());
           actualizar.setInt(2, co.getProducto());
           actualizar.setString(3, co.getUsuario());
           actualizar.addBatch();
       }
       actualizar.executeBatch();
    }
    
    /*Borro el comentario de un usuario sobre un producto.*/
    public void delete(int producto, String usuario) throws SQLException{
        delete.setInt(1, producto);
        delete.setString(2, usuario);
        delete.executeBatch();
    }
    
    /*Borro los comentarios de un producto (de todos los usuarios).*/
    public void delete(int producto) throws SQLException{
        delete.setInt(1, producto);
        delete.setString(2, "*");
        delete.executeUpdate();
    }
    
    /*Borro los comentarios de un usuario (sobre todos los productos).*/
    public void delete(String usuario) throws SQLException{
        delete.setString(1, "*");
        delete.setString(2, usuario);
        delete.executeUpdate();
    }
    
    /*Borro una serie de comentarios.*/
    public void delete(ArrayList<VOComentario> comentarios) throws SQLException{
        for(VOComentario co : comentarios){
            delete.setInt(1, co.getProducto());
            delete.setString(2, co.getUsuario());
            delete.addBatch();
        }
        delete.executeBatch();
    }
    
    /*Cierro todas las conexiones. Debe llamarse SIEMPRE al menos antes de terminar el programa, para cada DAOComentario creado.*/
    public void cerrar() throws SQLException{
        insert.close();
        delete.close();
        actualizar.close();
        this.con.close();
    }
}
