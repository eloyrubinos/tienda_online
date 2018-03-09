package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DAOCliente {

    private final String url_bd = "jdbc:mysql://localhost:3306/bd_dawa";
    private String usuario;
    private String pass;
    private Connection con;
    private PreparedStatement insert;
    private PreparedStatement actualizar;
    private PreparedStatement delete;

    public DAOCliente(String usuario, String pass) throws SQLException, ClassNotFoundException {
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

    public short autenticacionCliente(String usuario, String clave) throws SQLException {
        short flag = 0; //Variable auxiliar para devolver si fallan el nombre o la contraseña.
        // Realizamos una consulta que comprueba si existe una tupla con ese nombre, 
        // y que sea de tipo cliente. 
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select COUNT(*) from USUARIOS where nombreUsuario = '" + usuario
                + "' AND tipo = 'cliente'");

        set.next(); // Avanzamos el resultSet

        // Si existe alguna (no puede haber más por restricciones de la BD), 
        // sumamos 1.       
        if (set.getInt("count(*)") == 1) {
            flag++;
        }
        
        //Ahora consultamos si existe alguna tupla con ese nombre Y contraseña
        set = st.executeQuery("select COUNT(*) from USUARIOS where nombreUsuario = '" + usuario
                + "' AND pass = '" + clave
                + "' AND tipo = 'cliente'");

        set.next(); // Avanzamos el resultSet
        
        // Si existe alguna (no puede haber más por restricciones de la BD), 
        // sumamos 1.       
        if (set.getInt("count(*)") == 1) {
            flag++;
        }
        
        return flag;
    }

    /*Obtengo un cliente por su nombre de usuario.*/
    public VOCliente getCliente(String usuario) throws SQLException{
        VOCliente c = new VOCliente();
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from USUARIOS where nombreUsuario = '"+usuario+"' AND tipo = 'cliente'");
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

    /*Obtengo todos los clientes.*/
    public HashMap<String, VOCliente> getClientes() throws SQLException{
        HashMap<String, VOCliente> clientes = new HashMap();
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from USUARIOS where tipo = 'cliente'");
        
        while(set.next()){
            VOCliente c = new VOCliente();
            c.setNombre(set.getString(1));
            c.setEmail(set.getString(2));
            c.setPass(set.getString(3));
            c.setRango(set.getString(4));
            c.setGastoTotal(set.getFloat(5));
            clientes.put(c.getNombre(), c);
        }
        
        st.close();
        set.close();
        
        return clientes;
    }

    /*Agrego un cliente a la BD.*/
    /*Agrego un cliente a la BD.*/
    public void add(VOCliente c) throws SQLException{
        insert.setString(1, c.getNombre());
        insert.setString(2, c.getEmail());
        insert.setString(3, c.getPass());
        insert.setString(4, c.getRango());
        insert.setFloat(5, c.getGastoTotal());
        insert.setString(6, c.getTipo());
        insert.executeUpdate();
    }

    /*Agrego varios clientes a la BD.*/
    public void add(HashMap<String, VOCliente> clientes) throws SQLException{
        for(String nombreUsuario : clientes.keySet()){
            insert.setString(1, clientes.get(nombreUsuario).getNombre());
            insert.setString(2, clientes.get(nombreUsuario).getEmail());
            insert.setString(3, clientes.get(nombreUsuario).getPass());
            insert.setString(4, clientes.get(nombreUsuario).getRango());
            insert.setFloat(5, clientes.get(nombreUsuario).getGastoTotal());
            insert.setString(6, clientes.get(nombreUsuario).getTipo());
            insert.addBatch();
        }
        insert.executeBatch();
    }

    /*Actualizo los datos de un cliente.*/
    public void actualizarCliente(VOCliente c) throws SQLException{
        actualizar.setString(1, c.getEmail());
        actualizar.setString(2, c.getPass());
        actualizar.setString(3, c.getNombre());
        actualizar.executeUpdate();
    }

    /*Actualizo los datos de una serie de clientes.*/
    public void actualizarClientes(HashMap<String, VOCliente> clientes) throws SQLException {
        for (String nombreUsuario : clientes.keySet()) {
            actualizar.setString(1, clientes.get(nombreUsuario).getEmail());
            actualizar.setString(2, clientes.get(nombreUsuario).getPass());
            actualizar.setString(3, clientes.get(nombreUsuario).getNombre());
            actualizar.addBatch();
        }
        actualizar.executeBatch();
    }

    /*Borro un cliente.*/
    public void delete(VOCliente c) throws SQLException {
        delete.setString(1, c.getNombre());
        delete.executeUpdate();
    }

    /*Borro una serie de clientes.*/
    public void delete(HashMap<String, VOCliente> clientes) throws SQLException {
        for (String nombreUsuario : clientes.keySet()) {
            delete.setString(1, clientes.get(nombreUsuario).getNombre());
            delete.addBatch();
        }
        delete.executeBatch();
    }

    /*Cierro todas las conexiones. Debe llamarse SIEMPRE al menos antes de terminar el programa, para cada DAOCliente creado.*/
    public void cerrar() throws SQLException {
        insert.close();
        delete.close();
        actualizar.close();
        this.con.close();
    }
}
