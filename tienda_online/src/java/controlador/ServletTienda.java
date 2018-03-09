package controlador;

import modelo.Carrito;
import modelo.VOProducto;
import modelo.VOLineaVenta;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.DAOComentario;
import modelo.DAOProducto;
import modelo.DAOValoracion;
import modelo.VOComentario;
import modelo.VOValoracion;
import javax.mail.*;
import javax.mail.internet.*;
import modelo.DAOCliente;
import modelo.DAOVenta;
import modelo.VOCliente;
import modelo.VOVenta;

public class ServletTienda extends HttpServlet {

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

        // Declaro un carrito
        Carrito carrito;

        // Declaro el HashMap de productos aquí porque necesito que sea accesible desde todo el servlet.
        HashMap<Integer, VOProducto> productos = null;

        DAOProducto daop = new DAOProducto(USUARIO, PASS);
        productos = new HashMap(daop.getProductosDisponibles());
        daop.cerrar();

        // Declaro una sesión
        HttpSession sesion = null;

        // Compruebo si la sesión y sus atributos existen. Si no, la creo y configuro sus atributos. 
        if (request.getSession(false) == null || request.getSession().getAttribute("productos") == null
                || request.getSession().getAttribute("comentarios") == null
                || request.getSession().getAttribute("valoraciones") == null) {

            carrito = new Carrito(); // Creo el carrito

            sesion = request.getSession(true); // Creo la sesión

            // Obtengo los productos que se mostrarán en la tienda. 
            // Obtengo los comentarios y valoraciones de estos productos. 
            // Me hacen falta desde el principio para la funcionalidad de ver 
            // los detalles de los CDs.
            DAOComentario daoco = new DAOComentario(USUARIO, PASS);
            ArrayList<VOComentario> alco = new ArrayList();

            DAOValoracion daova = new DAOValoracion(USUARIO, PASS);
            ArrayList<VOValoracion> alva = new ArrayList();
            ArrayList<VOValoracion> alvaTodas = new ArrayList();

            // En cuanto a los productos, solo me interesa número total de valoraciones y valoración media.
            HashMap<Integer, Float[]> hashva = new HashMap();

            for (int producto : productos.keySet()) {
                alco.addAll(daoco.getComentarios(producto)); // En alco quedarán TODOS los comentarios de TODOS los productos disponibles.
                alva = daova.getValoraciones(producto); // alva son las valoraciones de cada producto en la iteración
                alvaTodas.addAll(alva);
                
                float media = 0F;

                for (VOValoracion valoracion : alva) {
                    media += valoracion.getValoracion();
                }

                if (!alva.isEmpty()) {
                    media = media / alva.size();
                }

                Float[] aux = {(float) alva.size(), media}; // Guardo el total de valoraciones y la valoración media para el producto en iteración
                hashva.put(producto, aux); // Lo meto en "datos"
            }

            daoco.cerrar();
            daova.cerrar();

            sesion.setAttribute("carrito", carrito); // Creo el atributo de sesión con el carrito recién creado
            sesion.setAttribute("productos", productos); // Si no había sesión también tengo que pasar la lista de productos
            sesion.setAttribute("comentarios", alco); // Y de comentarios de esos productos
            sesion.setAttribute("valoracionesal", alvaTodas);
            sesion.setAttribute("valoraciones", hashva); // Y de valoraciones de esos productos

        } else if (((sesion = request.getSession(false)) != null) && (request.getSession().getAttribute("carrito") == null)) { // Si el carrito no existe, lo instanciamos
            carrito = new Carrito((Carrito) request.getSession().getAttribute("carrito"));
        }

        // EMPIEZA EL CÓDIGO DE CONTROL 
        if (request.getParameter("tipo").equals("iniciarTienda") || request.getParameter("tipo").equals("autenticacion")) { // Ir a la tienda
            gotoPage("/tienda.jsp", request, response);
        } else if (request.getParameter("tipo").equals("agregarCarrito")) { // Añadir un producto al carrito
            // Recuperamos el carrito
            carrito = (Carrito) request.getSession().getAttribute("carrito");

            //Recojo la lista de todos los parámetros del formulario, que no sé cuántos serán de antemano
            Enumeration params = request.getParameterNames();

            // Por cada parámetro... 
            while (params.hasMoreElements()) {
                String param = params.nextElement().toString(); //Obtengo el id del producto (en forma de string)

                // Compruebo que no sea un submit o un hidden, porque me daría error al hacer el parseInt. También compruebo que no sea 0. 
                if (!(param.equals("tipo") || param.equals("submit") || request.getParameter(param).equals("0"))) {
                    VOLineaVenta lv = new VOLineaVenta(productos.get(Integer.parseInt(param)), Integer.parseInt(request.getParameter(param)));
                    carrito.add(lv);

                    /*Por último, actualizamos el stock en la copia en sesión de los productos.*/
                    productos.get(Integer.parseInt(param)).setStock(productos.get(Integer.parseInt(param)).getStock() - Integer.parseInt(request.getParameter(param)));
                    sesion.setAttribute("productos", productos);
                }
            }

            gotoPage("/tienda.jsp", request, response);

        } else if (request.getParameter("tipo").equals("eliminar")) { // Eliminar del carrito 
            // Recuperamos el carrito
            carrito = (Carrito) request.getSession().getAttribute("carrito");
            productos = (HashMap<Integer, VOProducto>) request.getSession().getAttribute("productos");

            //Recojo la lista de todos los parámetros del formulario, que no sé cuántos serán de antemano
            Enumeration params = request.getParameterNames();

            // Por cada parámetro... 
            while (params.hasMoreElements()) {
                String param = params.nextElement().toString(); //Obtengo el id del producto (en forma de string)

                // Compruebo que no sea un submit o un hidden, porque me daría error al hacer el parseInt. También compruebo que no sea 0. 
                if (!(param.equals("tipo") || param.equals("submit") || request.getParameter(param).equals("0"))) {

                    carrito.actualizarCantidad(Integer.parseInt(param), -Integer.parseInt(request.getParameter(param)));

                    /*Por último, actualizamos el stock en la copia en sesión de los productos.*/
                    productos.get(Integer.parseInt(param)).setStock(productos.get(Integer.parseInt(param)).getStock() + Integer.parseInt(request.getParameter(param)));
                    sesion.setAttribute("productos", productos);
                }
            }

            if (carrito.getCarrito().isEmpty()) { // Si el carrito está vacío, devolvemos al usuario a la tienda
                gotoPage("/tienda.jsp", request, response);
            } else { // Si no, seguimos en la página del carrito
                gotoPage("/carrito.jsp", request, response);
            }

        } else if (request.getParameter("tipo").equals("pago")) { // Pagar compra
            // Recuperamos el carrito, la sesión y el usuario
            carrito = (Carrito) request.getSession().getAttribute("carrito");
            //sesion = request.getSession();
            VOCliente cliente = (VOCliente) request.getSession().getAttribute("usuario");

            // Si la compra es mayor que 100 y el usuario no era VIP...
            if (carrito.getTotal() >= 100 && !cliente.getRango().equals("vip")) {
                // Aplicamos descuento del 20%
                float descuento = (carrito.getTotal() / 100.0f) * 80.0f;
                carrito.setTotal(descuento);

                // Convertimos al usuario en VIP
                cliente.setRango("vip");
                sesion.setAttribute("usuario", cliente);

                // Añadimos un atributo a la sesión para indicar que el usuario es un nuevo VIP
                sesion.setAttribute("esNuevoVIP", "true");

            } else if (cliente.getRango().equals("vip")) { // Si el usuario ya era vip... 
                // Aplicamos descuento del 20%
                float descuento = (carrito.getTotal() / 100.0f) * 80.0f;
                carrito.setTotal(descuento);

                // Indicamos que el usuario no es un nuevo VIP. Se hace explícitamente para evitar inconsistencias
                sesion.setAttribute("esNuevoVIP", "false");

            } else { // Si no se cumplen las condidiones, indicamos que el usuario no es un nuevo VIP. Se hace explícitamente para evitar inconsistencias
                sesion.setAttribute("esNuevoVIP", "false");
            }

            gotoPage("/pago.jsp", request, response);

        } else if (request.getParameter("tipo").equals("pagoConfirmado")) {
            //Actualizamos la BD para incluír las líneas de venta que se han adquirido, así como la venta realizada
            VOCliente cliente = (VOCliente) request.getSession().getAttribute("usuario");
            Carrito carro = (Carrito) request.getSession().getAttribute("carrito");

            // Actualizamos el cliente en la BD
            DAOCliente daoCliente = new DAOCliente(USUARIO, PASS);
            daoCliente.actualizarCliente(cliente);
            daoCliente.cerrar();

            // Indicamos que el usuario ya ha sido convertido en VIP para que no se propague el valor
            request.getSession().removeAttribute("esNuevoVIP");

            DAOVenta daoventa = new DAOVenta(USUARIO, PASS);
            daoventa.comprar(cliente, carro);
            daoventa.cerrar();

            final String username = "tiendademusicadawa@gmail.com";
            final String password = "greidawa2017";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

            javax.mail.Session session = javax.mail.Session.getInstance(props,
                    new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("tiendademusicadawa@gmail.com"));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse("eloyrubinos@gmail.com"));//aquí se cambia la dirección a la que queremos enviar el mail
                //en teoría tiene que aparecer el correo del usuario que está con la sesión iniciada
                //pero como podríamos introducir correos que en el mundo real no existan, para comprobar que este código funciona,
                //vamos a poner una dirección de correo que podamos abrir para ver que efectivamente nos llegó el correo.

                message.setSubject("Gracias por tu compra");
                message.setText("Estimado cliente,"
                        + "\ntu pago ha sido realizado con éxito. Trataremos de hacer llegar tu pedido lo antes posible"
                        + "\n\n ¡Gracias por confiar en nosotros!");

                Transport.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            String calle = request.getParameter("calle");
            String num = request.getParameter("numero");
            String piso = request.getParameter("piso");
            String letra = request.getParameter("letra");
            String localidad = request.getParameter("localidad");
            String provincia = request.getParameter("provincia");
            String cp = request.getParameter("cp");

            if (piso.isEmpty()) {
                piso = "-";
            }
            if (letra.isEmpty()) {
                letra = "-";
            }

            String direccionCompleta = calle + ", " + num + ", " + piso + ", " + letra + ", " + localidad + ", " + provincia + ", " + cp;
            request.getSession().setAttribute("direccion", direccionCompleta);
            request.getSession().setAttribute("nombre", request.getParameter("nombre"));

            gotoPage("/factura.jsp", request, response);

        } else if (request.getParameter("tipo").equals("volver")) { // Volver a la tienda
            request.getSession().removeAttribute("carrito"); // Eliminamos el carrito

            gotoPage("/tienda.jsp", request, response); // Volvemos a la tienda

        } else if (request.getParameter("tipo").equals("verCarrito")) { // Visitar el carrito
            gotoPage("/carrito.jsp", request, response); // Vamos a la página del carrito

        } else if (request.getParameter("tipo").equals("gestionProductos")) { // Gestión de las propias compras
            VOCliente c = (VOCliente) request.getSession().getAttribute("usuario");

            DAOVenta daov = new DAOVenta(USUARIO, PASS);
            HashMap<Integer, VOVenta> ventas = new HashMap(daov.getVentas(c.getNombre()));
            daov.cerrar();

            HashMap<Integer, VOProducto> productosCliente = new HashMap();
            for (VOVenta v : ventas.values()) {
                for (VOLineaVenta lv : v.getLvs()) {
                    if (!productosCliente.containsKey(lv.getProducto().getId())) {
                        productosCliente.put(lv.getProducto().getId(), lv.getProducto());
                    }
                }
            }

            DAOComentario daoco = new DAOComentario(USUARIO, PASS);
            HashMap<Integer, VOComentario> comentariosCliente = new HashMap(daoco.getComentariosHash(c.getNombre()));
            daoco.cerrar();

            DAOValoracion daova = new DAOValoracion(USUARIO, PASS);
            HashMap<Integer, VOValoracion> valoracionesCliente = new HashMap(daova.getValoracionesHash(c.getNombre()));
            daova.cerrar();

            request.getSession().setAttribute("productosCliente", productosCliente);
            request.getSession().setAttribute("comentariosCliente", comentariosCliente);
            request.getSession().setAttribute("valoracionesCliente", valoracionesCliente);

            gotoPage("/administracionProductosComprados.jsp", request, response);
            
        } else if (request.getParameter("tipo").equals("modificarProductoCliente")) {
            // Recibo el usuario
            VOCliente c = (VOCliente) request.getSession().getAttribute("usuario");
            // Recibo los comentarios en sesión
            ArrayList<VOComentario> alco = (ArrayList<VOComentario>) request.getSession().getAttribute("comentarios");
            // Recibo las valoraciones en sesión
            ArrayList<VOValoracion> alva = (ArrayList<VOValoracion>) request.getSession().getAttribute("valoracionesal");
            // Recibo los atributos creados para esta funcionalidad
            HashMap<Integer, VOComentario> comentariosCliente = (HashMap<Integer, VOComentario>) request.getSession().getAttribute("comentariosCliente");
            HashMap<Integer, VOValoracion> valoracionesCliente = (HashMap<Integer, VOValoracion>) request.getSession().getAttribute("valoracionesCliente");
            // Instancio DAOs
            DAOComentario daoco = new DAOComentario(USUARIO, PASS);
            DAOValoracion daova = new DAOValoracion(USUARIO, PASS);
            // Creo el comentario y la valoración recibidos
            VOComentario vco = new VOComentario(Integer.parseInt(request.getParameter("prodId")), c.getNombre(), request.getParameter("comentario"));
            VOValoracion vva = new VOValoracion(Integer.parseInt(request.getParameter("prodId")), c.getNombre(), Integer.parseInt(request.getParameter("valoracion")));
            // Si ya existía el comentario, lo actualizo en BD y actualizo la copia en sesión; si no, lo creo en BD y lo añado al array en sesión
            if(comentariosCliente.containsKey(vco.getProducto())) {
                daoco.actualizarComentario(vco);
                comentariosCliente.get(vco.getProducto()).setComentario(vco.getComentario());
                for(VOComentario co : alco){
                    if(co.getProducto() == vco.getProducto()) co.setComentario(vco.getComentario());
                }
            }
            else {
                daoco.add(vco);
                alco.add(vco);
            }
            // Lo mismo para las valoracinoes
            if(valoracionesCliente.containsKey(vva.getProducto())) {
                daova.actualizarValoracion(vva);
                valoracionesCliente.get(vva.getProducto()).setValoracion(vva.getValoracion());
                for(VOValoracion va : alva){
                    if(va.getProducto() == vva.getProducto()) va.setValoracion(vva.getValoracion());
                }
            }
            else {
                daova.add(vva);
                alva.add(vva);
            }
            // Vuelvo a calcular los valores que me interesan de las valoraciones
            productos = (HashMap<Integer, VOProducto>) request.getSession().getAttribute("productos");
            ArrayList<VOValoracion> auxva = new ArrayList();
            HashMap<Integer, Float[]> hashva = new HashMap();
            for (int producto : productos.keySet()) {
                auxva = daova.getValoraciones(producto);
                float media = 0F;
                for (VOValoracion valoracion : auxva) {
                    media += valoracion.getValoracion();
                }
                if (!auxva.isEmpty()) {
                    media = media / alva.size();
                }
                Float[] aux = {(float) auxva.size(), media};
                hashva.put(producto, aux);
            }
            
            sesion.setAttribute("comentarios", alco);
            sesion.setAttribute("valoracionesal", alva);
            sesion.setAttribute("valoraciones", hashva);
            
            daoco.cerrar();
            daova.cerrar();
            
            gotoPage("/administracionProductosComprados.jsp", request, response);
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
            Logger.getLogger(ServletTienda.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletTienda.class
                    .getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServletTienda.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletTienda.class
                    .getName()).log(Level.SEVERE, null, ex);
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
