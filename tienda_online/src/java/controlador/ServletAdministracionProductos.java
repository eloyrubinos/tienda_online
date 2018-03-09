package controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.DAOProducto;
import modelo.VOProducto;

public class ServletAdministracionProductos extends HttpServlet {

    //Creo una función para facilitar la devolución de páginas
    private void gotoPage(String dir, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher view = getServletContext().getRequestDispatcher(dir);
        view.forward(request, response);
    }

    public static final String USUARIO = "daw";
    public static final String PASS = "etse";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletTienda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {

        // Declaro el HashMap de productos y la sesión aquí porque necesito que sean accesibles desde todo el servlet.
        HashMap<Integer, VOProducto> productos = null;
        HttpSession sesion;

        // Si no hay sesión o productos cargados...
        if (request.getSession(false) == null || request.getSession(false).getAttribute("productos") == null) {
            sesion = request.getSession(true); // Creo la sesión

            // Cargo los productos
            DAOProducto daop = new DAOProducto(USUARIO, PASS);
            productos = new HashMap(daop.getProductosDisponibles());
            daop.cerrar();

            // Guardo los productos en la sesión
            sesion.setAttribute("productos", productos);

        } else { // Si hay, recupero la sesión y los productos
            sesion = request.getSession();
            productos = (HashMap<Integer, VOProducto>) request.getSession().getAttribute("productos");
        }

        String codControl = request.getParameter("tipo");
        if (codControl == null) codControl = "";
        if (codControl.equals("modificarProducto")) { // Modificar producto            
            // Creamos un nuevo producto con los datos del formulario y el id del producto a cambiar
            VOProducto producto = new VOProducto(Integer.parseInt(request.getParameter("producto")),
                    request.getParameter("nombre"),
                    request.getParameter("artista"),
                    request.getParameter("pais"),
                    Float.parseFloat(request.getParameter("precioUnitario")),
                    request.getParameter("ano"),
                    Integer.parseInt(request.getParameter("stock")));

            DAOProducto daoProductos = new DAOProducto(USUARIO, PASS); // Instanciamos el DAO
            daoProductos.actualizarProducto(producto); // Guardamos el producto en la base de datos
            daoProductos.cerrar(); // Cerramos el DAO
            productos.get(Integer.parseInt(request.getParameter("producto"))).setNombre(request.getParameter("nombre"));
            productos.get(Integer.parseInt(request.getParameter("producto"))).setArtista(request.getParameter("artista"));
            productos.get(Integer.parseInt(request.getParameter("producto"))).setPais(request.getParameter("pais"));
            productos.get(Integer.parseInt(request.getParameter("producto"))).setPrecioUnitario(Float.parseFloat(request.getParameter("precioUnitario")));
            productos.get(Integer.parseInt(request.getParameter("producto"))).setAno(request.getParameter("ano"));
            productos.get(Integer.parseInt(request.getParameter("producto"))).setStock(Integer.parseInt(request.getParameter("stock")));
            sesion.setAttribute("productos", productos);

        } else if (codControl.equals("borrarProducto")) { // Borrar producto
            DAOProducto daoProductos = new DAOProducto(USUARIO, PASS); // Instanciamos el DAO

            // Borramos el producto indicado de la BD
            daoProductos.delete(new VOProducto(Integer.parseInt(request.getParameter("producto"))));
            daoProductos.cerrar(); // Cerramos el DAO
            productos.remove(Integer.parseInt(request.getParameter("producto")));
            sesion.setAttribute("productos", productos);
                
        } else if (codControl.equals("nuevoProducto")) {
            // Creamos un nuevo producto con los datos del formulario y el id del producto a añadir
            VOProducto producto = new VOProducto(0,
                    request.getParameter("nombre"),
                    request.getParameter("artista"),
                    request.getParameter("pais"),
                    Float.parseFloat(request.getParameter("precioUnitario")),
                    request.getParameter("ano"),
                    Integer.parseInt(request.getParameter("stock")));

            DAOProducto daoProductos = new DAOProducto(USUARIO, PASS); // Instanciamos el DAO
            if(!daoProductos.isDuplicate(producto)){
                daoProductos.add(producto); // Guardamos el producto en la base de datos
                daoProductos.cerrar(); // Cerramos el DAO
                productos.put(producto.getId(), producto);
                sesion.setAttribute("productos", productos);
            }
            else {
                request.getSession().setAttribute("mensajeError", "Producto duplicado. Se ha abortado su inserción.");
                this.gotoPage("/error.jsp", request, response);
            }
        }

        // Redirigimos al usuario a la página de administración de productos
        gotoPage("/administracionProductos.jsp", request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);

        } catch (SQLException ex) {
            Logger.getLogger(ServletAdministracionProductos.class.getName()).log(Level.SEVERE, null, ex);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletAdministracionProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);

        } catch (SQLException ex) {
            Logger.getLogger(ServletAdministracionProductos.class.getName()).log(Level.SEVERE, null, ex);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletAdministracionProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}