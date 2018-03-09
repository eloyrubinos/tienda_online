<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>

<html>
    <head>
        <meta charset="utf-8"/>
        <title>Música para DAWA</title>
        <link type = "text/css" rel = "stylesheet" href = "efectos/stylesheet.css"/>
    </head>

    <body>

        <hr>
        <h1>Música para DAWA</h1>

        <jsp:useBean id = "carrito" class = "modelo.Carrito" scope = "session">
            <jsp:setProperty name = "carrito" property = "*"/>
        </jsp:useBean>

        <hr>
        <h1>Mi Carrito</h1>
        <table>
            <thead>
                <tr>
                    <th>Título del CD</th>
                    <th>Cantidad</th>
                    <th>Importe</th>
                    <th>Eliminar del carrito</th>
                </tr>
            </thead>

            <tbody>
            <form action="./ServletTienda" method="POST">
                <input type = "hidden" id = "hidden" name = "tipo" value = "eliminar"/>
                <c:forEach var = "productoCarrito" items = "${carrito.carrito}">
                    <tr>
                        <td>${productoCarrito.value.producto.nombre}</td>
                        <td>${productoCarrito.value.cantidad}</td> 
                        <td><fmt:formatNumber type="currency" value=" ${productoCarrito.value.precio}" /></td>
                        <td><input type = "number" name = "${productoCarrito.key}" min = "0" max = "${productoCarrito.value.cantidad}" value = "0" required/></td>
                    </tr>
                </c:forEach>
                <tr>
                    <td><strong>Total</strong></td>
                    <td></td>
                    <td></td>
                    <td><fmt:formatNumber type="currency" value=" ${carrito.total}" /></td>
                </tr>
                </tbody>
        </table>

        <div class = "center">
            <input class = "button" type = "submit" name = "submit" value = "Actualizar carrito" />
            <input class = "button" type = "submit" name = "submit" value = "Seguir comprando" onclick="document.getElementById('hidden').value = 'iniciarTienda';"/>
            <input class = "button" type = "submit" name = "submit" value = "Pagar" onclick="document.getElementById('hidden').value = 'pago';"/>
        </div>
    </form> 
                
    <hr>

</body>
<footer align="right"><strong>Contacto:</strong> tiendademusicadawa@gmail.com</footer>
</html>