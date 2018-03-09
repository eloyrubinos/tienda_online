package modelo;

import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class DAOVenta {

    private final String url_bd = "jdbc:mysql://localhost:3306/bd_dawa";
    private String usuario;
    private String pass;
    private Connection con;
    private PreparedStatement insert;
    private PreparedStatement insertLineas;

    public DAOVenta(String usuario, String pass) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver"); //Carga del controlador MySQL
        this.usuario = usuario;
        this.pass = pass;
        this.con = DriverManager.getConnection(url_bd, this.usuario, this.pass);
        con.setTransactionIsolation(4);
        insert = con.prepareStatement("insert into VENTAS "
                + "(id, nombreUsuario, total, iva, fecha) "
                + "values(?, ?, ?, ?, CURRENT_TIMESTAMP)");
        insertLineas = con.prepareStatement("insert into LineasVenta "
                + "(venta, producto, cantidad, precioUnitario) "
                + "values(?, ?, ?, ?)");
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

    /*Obtengo una venta por su ID (con todas sus LineaVenta).*/
    public VOVenta getVenta(int id) throws SQLException, ClassNotFoundException {
        VOVenta v = new VOVenta();
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from VENTAS where id = '" + id + "'");
        set.next();
        v.setId(set.getInt(1));

        DAOCliente daoc = new DAOCliente(this.usuario, this.pass);
        v.setC(daoc.getCliente(set.getString(2)));
        daoc.cerrar();

        v.setTotal(set.getFloat(3));
        v.setIva(set.getFloat(4));

        Date date = new Date(set.getTimestamp(5).getTime());
        String auxDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
        v.setFecha(auxDate);

        DAOProducto daop = new DAOProducto(this.usuario, this.pass);
        set = st.executeQuery("select * from LINEASVENTA where id = '" + id + "'");
        while (set.next()) {
            VOLineaVenta lv = new VOLineaVenta(daop.getProducto(set.getInt(2)), set.getInt(3));
            lv.setPrecio(set.getFloat(4));
            v.getLvs().add(lv);
        }

        daop.cerrar();
        st.close();
        set.close();
        return v;
    }

    /*Obtengo todas las ventas (con sus LineaVenta) de un usuario.*/
    public HashMap<Integer, VOVenta> getVentas(String usuario) throws SQLException, ClassNotFoundException {
        HashMap<Integer, VOVenta> ventas = new HashMap();
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from VENTAS where nombreUsuario = '" + usuario + "'");
        Statement st2 = con.createStatement();
        ResultSet set2;
        DAOCliente daoc = new DAOCliente(this.usuario, this.pass);
        DAOProducto daop = new DAOProducto(this.usuario, this.pass);
        VOVenta v;
        VOLineaVenta lv;
        Date date;

        while (set.next()) {
            v = new VOVenta();
            v.setId(set.getInt(1));
            v.setC(daoc.getCliente(set.getString(2)));
            v.setTotal(set.getFloat(3));
            v.setIva(set.getFloat(4));
            date = new Date(set.getTimestamp(5).getTime());
            v.setFecha(new SimpleDateFormat("dd-MM-yyyy").format(date));

            set2 = st2.executeQuery("select * from LINEASVENTA where venta = '" + v.getId() + "'");
            while (set2.next()) {
                lv = new VOLineaVenta(daop.getProducto(set2.getInt(2)), set2.getInt(3));
                lv.setPrecio(set2.getFloat(4));
                v.getLvs().add(lv);
            }
            set2.close();
            ventas.put(v.getId(), v);
        }

        daoc.cerrar();
        daop.cerrar();
        st.close();
        st2.close();
        set.close();

        return ventas;
    }

    /*Obtengo todas las ventas (con sus LineaVenta).*/
    public HashMap<Integer, VOVenta> getVentas() throws SQLException, ClassNotFoundException {
        HashMap<Integer, VOVenta> ventas = new HashMap();
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from VENTAS");
        ResultSet set2;
        DAOCliente daoc = new DAOCliente(this.usuario, this.pass);
        DAOProducto daop = new DAOProducto(this.usuario, this.pass);
        VOLineaVenta lv;
        Date date;

        while (set.next()) {
            VOVenta v = new VOVenta();
            v.setId(set.getInt(1));
            v.setC(daoc.getCliente(set.getString(2)));
            v.setTotal(set.getFloat(3));
            v.setIva(set.getFloat(4));
            date = new Date(set.getTimestamp(5).getTime());
            v.setFecha(new SimpleDateFormat("dd-MM-yyyy").format(date));

            set2 = st.executeQuery("select * from LINEASVENTA where id = '" + v.getId() + "'");
            while (set2.next()) {
                lv = new VOLineaVenta(daop.getProducto(set2.getInt(2)), set2.getInt(3));
                lv.setPrecio(set2.getFloat(4));
                v.getLvs().add(lv);
            }
            set2.close();
            ventas.put(v.getId(), v);
        }

        daoc.cerrar();
        daop.cerrar();
        st.close();
        set.close();

        return ventas;
    }

    /*Desactiva autocommit. Comprueba que haya stock en base de datos y mientras lo comprueba también lo va actualizando.
    Devuelve un array 'r' de dos enteros con la siguiente información:
        - Si r[1] == -1, la compra ha sido un éxito, y r[0] es el ID de la venta insertada (>=0).
        - Si r[0] == -1, la compra ha fallado, y r[1] tiene el ID del primer producto del carrito para el que no hubiese stock.
    Si falla, se hace rollback. Si tiene éxito, se hace commit.
    Finalmente cierra todos los recursos que ha abierto y vuelve a poner el autocommit.*/
    public int[] comprar(VOCliente cliente, Carrito carrito) throws SQLException, ClassNotFoundException {
        int[] r = {0, 0};
        con.setAutoCommit(false);

        /*Calculo qué ID le toca a la venta que estoy a punto de realizar.*/
        Statement st = con.createStatement();
        ResultSet set2 = st.executeQuery("select MAX(id) from VENTAS");
        set2.next();
        int nuevoId = set2.getInt(1) + 1;
        set2.close();
        st.close();

        /*Preparo lo que necesito para buscar productos en la BD y comprobar sus stocks reales.*/
        ResultSet set;
        DAOProducto daop = new DAOProducto(this.usuario, this.pass);

        /*Esta variable me sirve para controlar si todo ha ido bien o no.*/
        int flag = -1;

        /*Inserto la Venta en sí en la tabla VENTAS. No me preocupa que más adelante algo falle
        porque he desactivado el autocommit y puedo hacer rollback.*/
        insert.setInt(1, nuevoId);
        insert.setString(2, cliente.getNombre());
        insert.setFloat(3, carrito.getTotal());
        insert.setFloat(4, carrito.getIva());
        insert.executeUpdate();

        /*Empiezo a recorrer las LineaCompra del carrito.*/
        for (int linea : carrito.getCarrito().keySet()) {
            /*Obtengo el ResultSet ACTUALIZABLE que contiene el stock del producto en iteración.*/
            set = daop.getActuStock(carrito.getCarrito().get(linea).getProducto().getId());
            
            /*Comparo el stock con la cantidad que quiero comprar.
            Si no hay suficiente stock, me quedo con el producto (LineaCompra) que ha provocado el fallo guardando su ID en 'flag' y salgo del bucle.*/
            if (carrito.getCarrito().get(linea).getCantidad() > set.getInt("stock")) {
                flag = linea;
                break;
            } /*Si hay suficiente stock.*/ else {
                /*Actualizo el stock en BD del producto.*/
                set.updateInt("stock", set.getInt("stock") - carrito.getCarrito().get(linea).getCantidad());
                set.updateRow();
                /*Agrego las LineaCompra al batch.*/
                insertLineas.setInt(1, nuevoId);
                insertLineas.setInt(2, carrito.getCarrito().get(linea).getProducto().getId());
                insertLineas.setInt(3, carrito.getCarrito().get(linea).getCantidad());
                insertLineas.setFloat(4, carrito.getCarrito().get(linea).getProducto().getPrecioUnitario());
                insertLineas.addBatch();
            }

            set.close();
        }

        /*Si no ha habido ningún fallo: ejecuto el batch de inserciones de LineaCompra, 
                                        hago commit,
                                        y devuelvo el ID de la venta creada.*/
        if (flag == -1) {
            insertLineas.executeBatch();
            con.commit();
            r[0] = nuevoId;
            r[1] = -1;
        } /*Si ha habido algún fallo: ejecuto rollback, 
                                    y devuelvo el producto que ha dado el problema.*/ else {
            con.rollback();
            r[0] = -1;
            r[1] = flag;
        }

        /*Finalmente cierro las conexiones que he creado, vuelvo a activar el autocommit, y devuelvo r.*/
        daop.cerrar();
        con.setAutoCommit(true);
        return r;
    }

    /*Cierro todas las conexiones. Debe llamarse SIEMPRE al menos antes de terminar el programa, para cada DAOVenta creado.*/
    public void cerrar() throws SQLException {
        insert.close();
        insertLineas.close();
        this.con.close();
    }
}