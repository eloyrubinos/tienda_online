package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class DAOValoracion {
    private final String url_bd = "jdbc:mysql://localhost:3306/bd_dawa";
    private String usuario;
    private String pass;
    private Connection con;
    private PreparedStatement insert;
    private PreparedStatement actualizar;
    private PreparedStatement delete;
    
    public DAOValoracion(String usuario, String pass) throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.jdbc.Driver"); //Carga del controlador MySQL
        this.usuario = usuario;
        this.pass = pass;
        this.con = DriverManager.getConnection(url_bd, this.usuario, this.pass);
        con.setTransactionIsolation(4);
        insert = con.prepareStatement("insert into VALORACIONES "
                                        +"(producto, usuario, valoracion) "
                                        +"values(?, ?, ?)");
        actualizar = con.prepareStatement("update VALORACIONES set valoracion = ? where producto = ? AND usuario = ?");
        delete = con.prepareStatement("delete from VALORACIONES where producto = ? AND usuario = ?");
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
    
    
    /*Todos los métodos de este DAO son equivalentes a los de DAOComentario. Se pueden consultar ahí los comentarios.*/
    
    public VOValoracion getValoracion(int producto, String usuario) throws SQLException{
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from VALORACIONES where producto = '"+producto+"' AND usuario = '"+usuario+"'");
        set.next();
        VOValoracion vo = new VOValoracion(set.getInt(1), set.getString(2), set.getInt(3));
        st.close();
        set.close();
        return vo;
    }
    
    public ArrayList<VOValoracion> getValoraciones(int producto) throws SQLException{
        ArrayList<VOValoracion> valoraciones = new ArrayList();
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from VALORACIONES where producto = '"+producto+"'");
        
        while(set.next()){
            VOValoracion vo = new VOValoracion(set.getInt(1), set.getString(2), set.getInt(3));
            valoraciones.add(vo);
        }
        
        st.close();
        set.close();
        
        return valoraciones;
    }
    
    public ArrayList<VOValoracion> getValoraciones(String usuario) throws SQLException{
        ArrayList<VOValoracion> valoraciones = new ArrayList();
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from VALORACIONES where usuario = '"+usuario+"'");
        
        while(set.next()){
            VOValoracion vo = new VOValoracion(set.getInt(1), set.getString(2), set.getInt(3));
            valoraciones.add(vo);
        }
        
        st.close();
        set.close();
        
        return valoraciones;
    }
    
    public HashMap<Integer, VOValoracion> getValoracionesHash(String usuario) throws SQLException{
        HashMap<Integer, VOValoracion> valoraciones = new HashMap();
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from VALORACIONES where usuario = '"+usuario+"'");
        
        while(set.next()){
            VOValoracion vo = new VOValoracion(set.getInt(1), set.getString(2), set.getInt(3));
            valoraciones.put(set.getInt(1), vo);
        }
        
        st.close();
        set.close();
        
        return valoraciones;
    }
    
    public ArrayList<VOValoracion> getValoraciones() throws SQLException{
        ArrayList<VOValoracion> valoraciones = new ArrayList();
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from VALORACIONES");
        
        while(set.next()){
            VOValoracion vo = new VOValoracion(set.getInt(1), set.getString(2), set.getInt(3));
            valoraciones.add(vo);
        }
        
        st.close();
        set.close();
        
        return valoraciones;
    }
    
    public void add(VOValoracion vo) throws SQLException{
        insert.setInt(1, vo.getProducto());
        insert.setString(2, vo.getUsuario());
        insert.setInt(3, vo.getValoracion());
        insert.executeUpdate();
    }
    
    public void add(ArrayList<VOValoracion> valoraciones) throws SQLException{
        for(VOValoracion vo : valoraciones){
            insert.setInt(1, vo.getProducto());
            insert.setString(2, vo.getUsuario());
            insert.setInt(3, vo.getValoracion());
            insert.addBatch();
        }
        insert.executeBatch();
    }
    
    public void actualizarValoracion(VOValoracion vo) throws SQLException{
        actualizar.setInt(1, vo.getValoracion());
        actualizar.setInt(2, vo.getProducto());
        actualizar.setString(3, vo.getUsuario());
        actualizar.executeUpdate();
    }
    
    public void actualizarValoraciones(ArrayList<VOValoracion> valoraciones) throws SQLException{
       for(VOValoracion vo : valoraciones){
           actualizar.setInt(1, vo.getValoracion());
           actualizar.setInt(2, vo.getProducto());
           actualizar.setString(3, vo.getUsuario());
           actualizar.addBatch();
       }
       actualizar.executeBatch();
    }
    
    public void delete(int producto, String usuario) throws SQLException{
        delete.setInt(1, producto);
        delete.setString(2, usuario);
        delete.executeBatch();
    }
    
    public void delete(int producto) throws SQLException{
        delete.setInt(1, producto);
        delete.setString(2, "*");
        delete.executeUpdate();
    }
    
    public void delete(String usuario) throws SQLException{
        delete.setString(1, "*");
        delete.setString(2, usuario);
        delete.executeUpdate();
    }
    
    public void delete(ArrayList<VOValoracion> valoraciones) throws SQLException{
        for(VOValoracion vo : valoraciones){
            delete.setInt(1, vo.getProducto());
            delete.setString(2, vo.getUsuario());
            delete.addBatch();
        }
        delete.executeBatch();
    }
    
    public void cerrar() throws SQLException{
        insert.close();
        delete.close();
        actualizar.close();
        this.con.close();
    }
}
