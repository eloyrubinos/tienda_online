package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DAOAdministrador {

    private final String url_bd = "jdbc:mysql://localhost:3306/bd_dawa";
    private String usuario;
    private String pass;
    private Connection con;
    private PreparedStatement insert;
    private PreparedStatement actualizar;
    private PreparedStatement delete;

    public DAOAdministrador(String usuario, String pass) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver"); //Carga del controlador MySQL
        this.usuario = usuario;
        this.pass = pass;
        this.con = DriverManager.getConnection(url_bd, this.usuario, this.pass);
        con.setTransactionIsolation(4);
        insert = con.prepareStatement("insert into USUARIOS "
                + "(nombreUsuario, email, pass, rango, gastoTotal, tipo) "
                + "values(?, ?, ?, ?, ?, ?)");
        actualizar = con.prepareStatement("update USUARIOS set email = ?, pass = ? where nombreUsuario = ?");
        delete = con.prepareStatement("delete from USUARIOS where nombreUsuario = ?");
    }

    public String getUrl_bd() {
        return url_bd;
    }

    public String getUsuarioBD() {
        return usuario;
    }

    public String getPassBD() {
        return pass;
    }

    public Connection getCon() {
        return con;
    }


    /*Todos los métodos de este DAO son equivalentes a los de DAOCliente. Se pueden consultar ahí los comentarios.*/
    public short autenticacionAdmin(String usuario, String clave) throws SQLException {
        short flag = 0; //Variable auxiliar para devolver si fallan el nombre o la contraseña.
        // Realizamos una consulta que comprueba si existe una tupla con ese nombre, 
        // y que sea de tipo cliente. 
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select COUNT(*) from USUARIOS where nombreUsuario = '" + usuario
                + "' AND tipo = 'admin'");

        set.next(); // Avanzamos el resultSet

        // Si existe alguna (no puede haber más por restricciones de la BD), 
        // sumamos 1.       
        if (set.getInt("count(*)") == 1) {
            flag++;
        }
        
        //Ahora consultamos si existe alguna tupla con ese nombre Y contraseña
        set = st.executeQuery("select COUNT(*) from USUARIOS where nombreUsuario = '" + usuario
                + "' AND pass = '" + clave
                + "' AND tipo = 'admin'");

        set.next(); // Avanzamos el resultSet
        
        // Si existe alguna (no puede haber más por restricciones de la BD), 
        // sumamos 1.       
        if (set.getInt("count(*)") == 1) {
            flag++;
        }
        
        return flag;
    }

    public VOAdministrador getAdministrador(String usuario) throws SQLException{
        VOAdministrador c = new VOAdministrador();
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from USUARIOS where nombreUsuario = '"+usuario+"' AND tipo = 'admin'");
        set.next();
        c.setNombre(set.getString(1));
        c.setEmail(set.getString(2));
        c.setPass(set.getString(3));
        c.setRango(set.getString(4));
        c.setGastoTotal(set.getFloat(5));
        st.close();
        set.close();
        return c;
    }

    public HashMap<String, VOAdministrador> getAdministradores() throws SQLException{
        HashMap<String, VOAdministrador> administradores = new HashMap();
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from USUARIOS where tipo = 'admin'");

        while(set.next()){
            VOAdministrador c = new VOAdministrador();
            c.setNombre(set.getString(1));
            c.setEmail(set.getString(2));
            c.setPass(set.getString(3));
            c.setRango(set.getString(4));
            c.setGastoTotal(set.getFloat(5));
            administradores.put(c.getNombre(), c);
        }

        st.close();
        set.close();

        return administradores;
    }

    public void add(VOAdministrador c) throws SQLException{
        insert.setString(1, c.getNombre());
        insert.setString(2, c.getEmail());
        insert.setString(3, c.getPass());
        insert.setString(4, c.getRango());
        insert.setFloat(5, c.getGastoTotal());
        insert.setString(6, c.getTipo());
        insert.executeUpdate();
    }

    public void add(HashMap<String, VOAdministrador> administradores) throws SQLException{
        for(String nombreUsuario : administradores.keySet()){
            insert.setString(1, administradores.get(nombreUsuario).getNombre());
            insert.setString(2, administradores.get(nombreUsuario).getEmail());
            insert.setString(3, administradores.get(nombreUsuario).getPass());
            insert.setString(4, administradores.get(nombreUsuario).getRango());
            insert.setFloat(5, administradores.get(nombreUsuario).getGastoTotal());
            insert.setString(6, administradores.get(nombreUsuario).getTipo());
            insert.addBatch();
        }
        insert.executeBatch();
    }

    public void actualizarAdministrador(VOAdministrador c) throws SQLException{
        actualizar.setString(1, c.getEmail());
        actualizar.setString(2, c.getPass());
        actualizar.setString(3, c.getNombre());
        actualizar.executeUpdate();
    }

    public void actualizarAdministradores(HashMap<String, VOAdministrador> administradores) throws SQLException{       
        for(String nombreUsuario : administradores.keySet()){
            actualizar.setString(1, administradores.get(nombreUsuario).getEmail());
            actualizar.setString(2, administradores.get(nombreUsuario).getPass());
            actualizar.setString(3, administradores.get(nombreUsuario).getNombre());
            actualizar.addBatch();
        }
        actualizar.executeBatch();
    }

    public void delete(VOAdministrador c) throws SQLException {
        delete.setString(1, c.getNombre());
        delete.executeUpdate();
    }

    public void delete(HashMap<String, VOAdministrador> administradores) throws SQLException {
        for (String nombreUsuario : administradores.keySet()) {
            delete.setString(1, administradores.get(nombreUsuario).getNombre());
            delete.addBatch();
        }
        delete.executeBatch();
    }

    /*Cierro todas las conexiones. Debe llamarse SIEMPRE al menos antes de terminar el programa, para cada DAOAdministrador creado.*/
    public void cerrar() throws SQLException {
        insert.close();
        delete.close();
        actualizar.close();
        this.con.close();
    }

}
