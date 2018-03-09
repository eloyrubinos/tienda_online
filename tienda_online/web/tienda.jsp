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

        <div class = "floatRight"><form class = "inline" action = "./ServletTienda" method = "POST"><input type = "hidden" name = "tipo" value = "gestionProductos" /><input class = "button" type = "submit" value = "Gestión" /></form>&nbsp<form class = "inline" method = "POST" action = "./ServletAutenticacionRegistro" ><input type = "hidden" name = "tipo" value = "logout" /><input type = "submit" value = "Logout" /></form></div>
        <h1>Música para DAWA</h1>
        <hr>
        
        <jsp:useBean id = "carrito" class = "modelo.Carrito" scope = "session">
            <jsp:setProperty name = "carrito" property = "*"/>
        </jsp:useBean>
        
        <h2 align = "center">Bienvenido a la tienda online de DAWA</h2>
        
        <div id="search">
            <label class = "text">Título:&nbsp<input type="text" id="stitulo" onkeyup="filter()" /></label>&nbsp
            <label class = "text">Autor:&nbsp<input type="text" id="sautor" onkeyup="filter()" /></label>&nbsp
            <label class = "text">Año:&nbsp<input type="text" id="sano" onkeyup="filter()" size="1" /></label>&nbsp
            <label class = "text">Precio máximo:&nbsp<input type="text" id="sprecio" onkeyup="filter()" size="1" /></label>
        </div>
        
        <form class = "center" id = "form" method = "POST" action = "./ServletTienda" >
            <input type = "hidden" id = "hidden" name = "tipo" value = "agregarCarrito" />
            
            <c:if test="${productos.size == 0}">
                  <h2>No hay productos en la tienda.</h2>
            </c:if>
                  
                  <table id="catalogo">
                      <thread>
                          <tr>
                              <th>Título</th>
                              <th>Artista</th>
                              <th>País</th>
                              <th>Año</th>
                              <th>Precio</th>
                              <th>Detalles</th>
                              <th>Stock</th>
                              <th>Unidades</th>
                          </tr>
                      </thread>
                      <tbody>
                          <c:forEach var="prod" items="${productos}">
                              <tr>
                                  <td><strong>${prod.value.nombre}</strong></td>
                                  <td>${prod.value.artista}</td>
                                  <td>${prod.value.pais}</td>
                                  <td>${prod.value.ano}</td>
                                  <td>${prod.value.precioUnitario}</td>
                                  <td><a href="#openModal${prod.key}"><span class="detalles">+</span></a></td>
                                  <td>${prod.value.stock}</td>
                                  <td><input type = "number" name = "${prod.key}" id = "cantidad" min = "0" max = "${prod.value.stock}" value = "0" required/></td>
                              </tr>
                          </c:forEach>
                      </tbody>
                  </table>          
                  
            <input class = "button" type = "submit" name = "submit" value = "Añade los productos"/>

            <c:choose>
                <c:when test = "${carrito.carrito.size() != 0}">
                    <input class = "button" type = "submit" name = "submit" onclick="document.getElementById('hidden').value = 'verCarrito';" value = "Visita el carrito"/>
                </c:when>

                <c:otherwise>
                    <input class = "button" type = "submit" name = "submit" onclick="document.getElementById('hidden').value = 'verCarrito';" value = "Visita el carrito" disabled style = "background-color: #999;"/>
                </c:otherwise>
            </c:choose>
                    
        </form>
        <hr>
        
        <c:forEach var="prod" items="${productos}">
            <fmt:formatNumber var="fnum" value="${valoraciones[prod.key][1]}" maxFractionDigits="0" />
            <div id="openModal${prod.key}" class="modalDialog">
                <div>
                    <a href="#close" title="Close" class="close">X</a>
                    <div class="wrap">
                    <h2 style="text-align: center;">Detalles de producto</h2>
                    <p class = "text"><strong>Nombre:</strong> ${prod.value.nombre}<br /><strong>Artista:</strong> ${prod.value.artista}<br /><strong>Pais:</strong> ${prod.value.pais}<br /><strong>Año:</strong> ${prod.value.ano}</p>
                    <p class = "text"><strong>Valoración:</strong> <img src="<c:url value='imagenes/${fnum}.png' />" alt="Puntuación" height="15"/><br />Número de valoraciones: <fmt:formatNumber value="${valoraciones[prod.key][0]}" maxFractionDigits="0" /><br />Puntuación media: ${valoraciones[prod.key][1]}</p>
                    <p class = "text"><strong>Comentarios:</strong>
                    <c:forEach var="comentario" items="${comentarios}">
                        <c:if test="${comentario.producto == prod.key}">
                            <div class="comentario">
                                <strong>${comentario.usuario} dijo:</strong> ${comentario.comentario}
                            </div>
                        </c:if>       
                    </c:forEach>
                    </div>
                </div>
            </div>
        </c:forEach>
    </body>
    <footer align="right"><strong>Contacto:</strong> tiendademusicadawa@gmail.com</footer>
</html>