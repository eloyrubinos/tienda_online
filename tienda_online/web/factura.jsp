<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>

<html>
    <head>
        <title>Música para DAWA</title>

        <link type = "text/css" rel = "stylesheet" href = "efectos/stylesheet.css"/>
    </head>

    <body>
        <hr>
        <h1>Música DAWA</h1>
        
        <jsp:useBean id = "carrito" class = "modelo.Carrito" scope = "session">
            <jsp:setProperty name = "carrito" property = "*"/>
        </jsp:useBean>
        
        <hr>
        <h3 class="center">¡Gracias por su compra! Hemos enviado un correo de confirmación del pago a ${sessionScope.usuario.email}</h3>
        <hr>
        <h1>Mi Factura</h1>
        <table>
            <thead>
                <tr>
                    <th>Título del CD</th>
                    <th>Cantidad</th>
                    <th>Importe total</th>
                </tr>
            </thead>

            <tbody>
                <c:forEach var = "productoCarrito" items = "${carrito.carrito}">
                    <tr>
                        <td>${productoCarrito.value.producto.nombre}</td>
                        <td>${productoCarrito.value.cantidad}</td> 
                        <td><fmt:formatNumber type="currency" value=" ${carrito.total}" /></td>
                    </tr>
                </c:forEach>
                    <tr>
                        <td><strong>IVA</strong></td>
                        <td></td>
                        <td>${carrito.iva} %</td>
                    </tr>
                    <tr>
                        <td><strong>Total</strong></td>
                        <td></td>
                        <td><fmt:formatNumber type="currency" value=" ${carrito.total}" /></td>
                    </tr>
            </tbody>
        </table>

        <div class="mensaje">
            <p class = "text"><strong>Destinatario: </strong>${sessionScope.nombre}</p>
            <p class = "text"><strong>Dirección de envío: </strong>${sessionScope.direccion}</p>
        </div>

        <form class="center" action ="./ServletTienda" method="POST">
            <input type = "hidden" name = "tipo" value = "volver"/>
            <input type = "submit" name ="index" value = "Volver a la tienda"/>           
        </form>
        <hr>

    </body>
    <footer align="right"><strong>Contacto:</strong> tiendademusicadawa@gmail.com</footer>
</html>