<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>
    <head>
        <title>Música para DAWA</title>
        <meta name="description" content="Tienda online de música">
        <meta name="keywords" content="tienda,música">
        <meta name="robots" content="all">
        <link type = "text/css" rel = "stylesheet" href = "efectos/stylesheet.css"/>
        <script type="text/javascript" src="efectos/jquery-2.1.4.min.js"></script>
        <script type="text/javascript" src="efectos/scripts.js"></script>
    </head>

    <body>

        <div class = "floatRight"><form class = "inline" action = "./ServletTienda" method = "POST"><input type = "hidden" name = "tipo" value = "iniciarTienda" /><input class = "button" type = "submit" value = "Tienda" /></form>&nbsp<form class = "inline" method = "POST" action = "./ServletAutenticacionRegistro" ><input type = "hidden" name = "tipo" value = "logout" /><input type = "submit" value = "Logout" /></form></div>
        <h1>Música para DAWA</h1>
        <hr>

        <h2 align = "center">Gestión de productos para el usuario ${usuario.nombre}</h2>

            <c:if test="${productosCliente.size == 0}">
                <h2>No hay productos en la tienda.</h2>
            </c:if>

            <table id="catalogo">
                <thead>
                    <tr>
                        <th>Título</th>
                        <th>Detalles</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="prod" items="${productosCliente}">
                        <tr>
                            <td><strong>${prod.value.nombre}</strong></td>
                            <td><a href="#openModal${prod.key}"><span class="detalles">+</span></a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

        <hr>

          
        <c:forEach var="prod" items="${productosCliente}">
            <form class = "center" id = "form${prod.key}" method = "POST" action = "./ServletTienda" >
                <input type = "hidden" name = "tipo" value = "modificarProductoCliente" />
                <input type = "hidden" name = "prodId" value = "${prod.key}" />
                <div id="openModal${prod.key}" class="modalDialog">
                    <div>
                        <a href="#close" title="Close" class="close">X</a>
                        <div class="wrap formulario">
                            <p class = "text"><strong>Comentario:</strong><br /><textarea name = "comentario" form = "form${prod.key}"><c:if test = "${comentariosCliente.containsKey(prod.key)}">${comentariosCliente.get(prod.key).getComentario()}</c:if></textarea> <br /><strong>Valoración:</strong><br /><input type="number" name="valoracion" value="${valoracionesCliente.get(prod.key).getValoracion()}" min = "0" max = "5"></p>
                            <button type = "submit">Modificar</button>
                        </div>
                    </div>
                </div>
            </form>
        </c:forEach>

    </body>
    <footer align = "right"><strong>Contacto:</strong> tiendademusicadawa@gmail.com</footer>
</html>