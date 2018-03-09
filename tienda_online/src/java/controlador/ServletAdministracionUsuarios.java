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
import modelo.DAOAdministrador;
import modelo.DAOCliente;
import modelo.VOAdministrador;
import modelo.VOCliente;

public class ServletAdministracionUsuarios extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");

        // Declaramos e inicializamos un hashmap de usuarios, y declaramos uno de administradores y uno de clientes
        HashMap<String, VOAdministrador> administradores = null;
        HashMap<String, VOCliente> clientes = null;

        // Instanciamos el DAO de administradores, recuperamos los administradores y lo cerramos
        DAOAdministrador daoAdmin = new DAOAdministrador(USUARIO, PASS);
        administradores = daoAdmin.getAdministradores();
        daoAdmin.cerrar();

        // Instanciamos el DAO de clientes, recuperamos los clientes y lo cerramos
        DAOCliente daoCliente = new DAOCliente(USUARIO, PASS);
        clientes = daoCliente.getClientes();
        daoCliente.cerrar();

        // Colocamos los usuarios como atributos de la sesión
        HttpSession sesion = request.getSession(true); // Si no existe, la creamos
        sesion.setAttribute("clientes", clientes);
        sesion.setAttribute("administradores", administradores);

        // Código de control
        String codControl=request.getParameter("tipo");
        if(codControl == null) codControl = "";
        if (codControl.equals("borrarUsuario")) { // Borrar usuario
            if(administradores.get(request.getParameter("usuario")) != null) { // El usuario es admin
                // Instanciamos el DAO, borramos el usuario y cerramos el DAO
                daoAdmin = new DAOAdministrador(USUARIO, PASS);
                daoAdmin.delete(administradores.get(request.getParameter("usuario")));
                daoAdmin.cerrar();
                clientes.remove(request.getParameter("usuario"));
                sesion.setAttribute("administradores", administradores);

            } else if (clientes.get(request.getParameter("usuario")) != null) { // El usuario es cliente
                // Instanciamos el DAO, borramos el usuario y cerramos el DAO
                daoCliente = new DAOCliente(USUARIO, PASS);
                daoCliente.delete(clientes.get(request.getParameter("usuario")));
                daoCliente.cerrar();
                clientes.remove(request.getParameter("usuario"));
                sesion.setAttribute("clientes", clientes);
            }
        } else if (codControl.equals("modificarUsuario")) { // Modificar la clave
            if(administradores.get(request.getParameter("usuario")) != null) { // El usuario es admin
                // Instanciamos el DAO
                daoAdmin = new DAOAdministrador(USUARIO, PASS);
                
                // Recuperamos el cliente y cambiamos la contraseña
                VOAdministrador admin = administradores.get(request.getParameter("usuario")); 
                admin.setPass(request.getParameter("pass"));
                admin.setEmail(request.getParameter("email"));
                daoAdmin.actualizarAdministrador(admin); // Actualizamos el usuario
                daoAdmin.cerrar(); // Cerramos el DAO
                administradores.get(request.getParameter("usuario")).setEmail(request.getParameter("email"));
                administradores.get(request.getParameter("usuario")).setPass(request.getParameter("pass"));
                sesion.setAttribute("administradores", administradores);

            } else if (clientes.get(request.getParameter("usuario")) != null) { // El usuario es cliente
                // Instanciamos el DAO
                daoCliente = new DAOCliente(USUARIO, PASS);
                
                // Recuperamos el cliente y cambiamos la contraseña
                VOCliente cliente = clientes.get(request.getParameter("usuario")); 
                cliente.setPass(request.getParameter("pass"));
                cliente.setEmail(request.getParameter("email"));
                daoCliente.actualizarCliente(cliente); // Actualizamos el usuario
                daoCliente.cerrar(); // Cerramos el DAO
                clientes.get(request.getParameter("usuario")).setEmail(request.getParameter("email"));
                clientes.get(request.getParameter("usuario")).setPass(request.getParameter("pass"));
                sesion.setAttribute("clientes", clientes);
            }
        }

        // Redirigimos al usuario a la página de administración
        this.gotoPage("/administracionUsuarios.jsp", request, response);
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
            Logger.getLogger(ServletAdministracionUsuarios.class.getName()).log(Level.SEVERE, null, ex);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletAdministracionUsuarios.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServletAdministracionUsuarios.class.getName()).log(Level.SEVERE, null, ex);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletAdministracionUsuarios.class.getName()).log(Level.SEVERE, null, ex);
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