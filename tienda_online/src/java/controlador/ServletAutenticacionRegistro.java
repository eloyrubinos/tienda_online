package controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.DAOAdministrador;
import modelo.DAOCliente;
import modelo.VOAdministrador;
import modelo.VOCliente;

public class ServletAutenticacionRegistro extends HttpServlet {

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

        if (request.getParameter("tipo").equals((String) "autenticacion")) { // Autenticación
            // Variables para guardar el usuario y la contraseña
            String usuario = null, clave = null;

            // Si existen las credenciales, las guardamos en variables locales
            if (request.getParameter("usuario") != null && request.getParameter("clave") != null) {
                usuario = request.getParameter("usuario");
                clave = request.getParameter("clave");
            } else {
                request.getSession().setAttribute("mensajeError", "No se han especificado credenciales válidas. ");
                this.gotoPage("/error.jsp", request, response);
            }

            //Variables auxiliares para saber si fallan el nombre de usuario o la contraseña
            short flagu, flaga;
            
            // Instanciamos los DAOs
            DAOAdministrador daoAdmin = new DAOAdministrador(USUARIO, PASS);
            DAOCliente daoCliente = new DAOCliente(USUARIO, PASS);

            // Si existe un administrador con nombre y contraseña coincidentes... 
            if ((flaga = daoAdmin.autenticacionAdmin(usuario, clave)) == 2) {
                VOAdministrador admin = daoAdmin.getAdministrador(usuario); // Lo recuperamos de la BD

                HttpSession sesion = request.getSession(); // Recuperamos la sesión; si no existe, la creamos
                sesion.setAttribute("usuario", admin); // Colocamos el usuario como atributo de la sesión

                // Redirigimos el usuario al servlet de administración
                gotoPage("/ServletAdministracionProductos", request, response);

            } else if ((flagu = daoCliente.autenticacionCliente(usuario, clave)) == 2) { // Si en su lugar, existe un cliente con nombre y contraseña coincidentes... 
                VOCliente cliente = daoCliente.getCliente(usuario); // Lo recuperamos de la BD

                HttpSession sesion = request.getSession(); // Recuperamos la sesión; si no existe, la creamos
                sesion.setAttribute("usuario", cliente); // Colocamos el usuario como atributo de la sesión
                
                // Redirigimos al usuario al servlet de la tienda
                gotoPage("/ServletTienda", request, response);

            } else if (flaga == 1 || flagu == 1) { // Si el usuario está, pero la contraseña no ha sido correcta
                request.getSession().setAttribute("mensajeError", "Contraseña incorrecta.");
                this.gotoPage("/error.jsp", request, response);
            } else if (flaga == 0 || flagu == 0) {// Si el usuario no está en la BD, lo redirigimos a la página de error
                request.getSession().setAttribute("mensajeError", "No existe ese usuario.");
                this.gotoPage("/error.jsp", request, response);
            } else {
                request.getSession().setAttribute("mensajeError", "Error desconocido.");
                this.gotoPage("/error.jsp", request, response);
            }

            daoAdmin.cerrar();
            daoCliente.cerrar();

        } else if (request.getParameter("tipo").equals((String) "registro")) { // Registro
            // Variables para guardar el usuario, su nombre y la contraseña
            String usuario = null, clave = null, email = null;

            // Si existen las credenciales, las guardamos en variables locales
            if (request.getParameter("usuario") != null && request.getParameter("clave") != null && request.getParameter("email") != null) {
                usuario = request.getParameter("usuario");
                clave = request.getParameter("clave");
                email = request.getParameter("email"); 

            } else {
                request.getSession().setAttribute("mensajeError", "No se han especificado datos de registro válidos. ");
                this.gotoPage("/error.jsp", request, response);
            }
            
            // Creo patrones de expresiones regulares para verificar los campos
            String pusuario = "^[a-zA-Z*@0-9_]{1,10}$";
            Pattern pu = Pattern.compile(pusuario);
            String pclave = "^[a-zA-Z *@0-9_\\-\\.]{5,10}$";
            Pattern pc = Pattern.compile(pclave);
            String pemail = "^[a-zA-Z \\._\\-0-9]{1,}@[a-zA-Z0-9]{1,}\\.[a-zA-Z]{1,5}$";
            Pattern pe = Pattern.compile(pemail);
            Matcher mu = pu.matcher(usuario);
            Matcher mc = pc.matcher(clave);
            Matcher me = pe.matcher(email);
            
            if(mu.matches() && mc.matches() && me.matches()){
                
                // Instanciamos un nuevo cliente
                VOCliente cliente = new VOCliente(usuario, email, clave, "normal", 0F); 

                // Instanciamos el DAO
                DAOCliente daoCliente = new DAOCliente(USUARIO, PASS);

                try {
                    daoCliente.add(cliente); // Añadimos el cliente

                } catch (SQLException ex) { // Si hay una excepción, redirigimos al usuario a una página de error
                    request.getSession().setAttribute("mensajeError", ex);
                    this.gotoPage("/error.jsp", request, response);
                }

                // Cerramos el DAO
                daoCliente.cerrar();

                // Redirigimos al cliente a inicio
                this.gotoPage("/index.jsp", request, response);
            } else {
                request.getSession().setAttribute("mensajeError", "No se han especificado datos de registro válidos. ");
                this.gotoPage("/error.jsp", request, response);
            }
            
        } else if (request.getParameter("tipo").equals((String) "logout")) {
            
            HttpSession sesion = request.getSession(false); //Recuperamos la sesión, si la hay
            if (sesion != null) sesion.invalidate(); //Si la hay, la borramos con todos los objetos asociados a ella
            
            //Redirigimos a la página inicial
            gotoPage("/index.jsp", request, response);
        }
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
            Logger.getLogger(ServletAutenticacionRegistro.class.getName()).log(Level.SEVERE, null, ex);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletAutenticacionRegistro.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServletAutenticacionRegistro.class.getName()).log(Level.SEVERE, null, ex);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletAutenticacionRegistro.class.getName()).log(Level.SEVERE, null, ex);

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
