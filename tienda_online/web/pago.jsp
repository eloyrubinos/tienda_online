<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>

<html>
    <head>
        <title>Música para DAWA</title>

        <jsp:useBean id = "carrito" class = "modelo.Carrito" scope = "session">
            <jsp:setProperty name = "carrito" property = "*"/>
        </jsp:useBean>

        <link type = "text/css" rel = "stylesheet" href = "efectos/stylesheet.css"/>
    </head>

    <body>
        <hr>
        <h1>Música DAWA</h1>
        <hr>
        <h1>Mi Pedido</h1>
        <table>
            <thead>
                <tr>
                    <th>Título del CD</th>
                    <th>Cantidad</th>
                    <th>Importe</th>
                </tr>
            </thead>

            <tbody>
                <c:forEach var = "productoCarrito" items = "${carrito.carrito}">
                    <tr>
                        <td>${productoCarrito.value.producto.nombre}</td>
                        <td>${productoCarrito.value.cantidad}</td> 
                        <td><fmt:formatNumber type="currency" value=" ${productoCarrito.value.precio}" /></td>
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

        <c:set var = "precioCarro" value="${carrito.total}"/>

        <c:if test = "${esNuevoVIP == 'true' || usuario.rango == 'vip'}">
            <div class = "mensaje">
                <c:if test = "${esNuevoVIP == 'true'}">
                    <h4 class = "center">¡Enhorabuena! Ha sido ascendido al rango VIP, por lo que podrá disfrutar de exclusivos descuentos y promociones.</h4>
                </c:if>

                <c:if test = "${usuario.rango == 'vip'}">
                    <h4 class = "center">Se aplicará un 20% de descuento en el importe total por ser usuario VIP.</h4>
                </c:if>
            </div>
        </c:if>

        <h1>Datos del envío</h1>
        <div class = "direccion">
            <form class="direccionEnvio" name="direccionEnvio" action ="./ServletTienda" method="POST">
                <input type = "hidden" name = "tipo" value = "pagoConfirmado"/>
                <p class = "formText">Nombre y apellidos del destinatario*: <input type="text" class="nombre" name="nombre" style = "width: 350px;" required><br /></p>
                <p class = "formText">Calle*: <input type="text" class="calle" name="calle" required pattern="^[a-zA-Z ]{1,60}$" style = "width: 225px;"></p>
                <p class = "formText">&nbsp&nbspNúmero*: <input type="text" class="num" name ="numero" required pattern ="^[0-9]{1,3}$" size="1"></p>
                <p class = "formText">&nbsp&nbspPiso: <input type="text" class="num" name ="piso" pattern="^[0-9]{1,2}$" size="1"></p>
                <p class = "formText">&nbsp&nbspLetra: <input type="text" class="num" name ="letra" pattern="^[a-zA-Z]{1}$" size="1"><br /></p>
                <p class = "formText">Localidad*: <input type="text" class="loc" name ="localidad" required pattern="^[a-zA-Z ]{2,60}$"></p>
                <p class = "formText">&nbsp&nbspProvincia*: <input type="text" class="loc" name ="provincia" required pattern="^[a-zA-Z ]{2,60}$"></p>
                <p class = "formText">&nbsp&nbspCP*: <input type="text" class="num" name ="cp" required pattern="^[0-9]{5}$" size="3"></p>

                <p style="font-size: 70%; font-family: sans-serif;">Los campos marcados con '*' son obligatorios.</p>
                
                <div class="center">
                    <input type = "submit" name = "pagoConfirmado" value = "Confirmar pago"/>
                </div>
            </form>
        </div>
        <hr>

    </body>
    <footer align="right"><strong>Contacto:</strong> tiendademusicadawa@gmail.com</footer>
</html>