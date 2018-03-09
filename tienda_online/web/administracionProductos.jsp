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

        <form style = "float: right;" method = "POST" action = "./ServletAutenticacionRegistro" >
            <input type = "hidden" name = "tipo" value = "logout" />
            <input type = "submit" value = "Logout" />
        </form>
        <h1>Música para DAWA</h1>
        <hr>

        <h2 align = "center">Administración de productos</h2>

        <div class = "center"><form class = "inline" action = "./ServletAdministracionUsuarios" method = "POST"><input class = "button" type = "submit" value = "Usuarios" /></form>&nbsp<form class = "inline" action = "./ServletAdministracionProductos" method = "POST"><input class = "button" type = "submit" value = "Productos" disabled style = "background-color: #999;"/></form><br /><a href="#openModalNuevo"><button class = "button" type = "button">Nuevo</button></a></div>

            <c:if test="${productos.size == 0}">
                <h2>No hay productos en la tienda.</h2>
            </c:if>

            <table id="catalogo">
                <thead>
                    <tr>
                        <th>Título</th>
                        <th>Artista</th>
                        <th>País</th>
                        <th>Año</th>
                        <th>Precio</th>
                        <th>Detalles</th>
                        <th>Stock</th>
                    </tr>
                </thead>
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
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

        <hr>

          
        <c:forEach var="prod" items="${productos}">
            <form class = "center" id = "form" method = "POST" action = "./ServletAdministracionProductos" >
                <input type = "hidden" id = "tipo${prod.key}" name = "tipo" />
                <input type="hidden" id="productoId" name="producto" value="${prod.value.id}" />
                <div id="openModal${prod.key}" class="modalDialog">
                    <div>
                        <a href="#close" title="Close" class="close">X</a>
                        <div class="wrap formulario">
                            <h2 style="text-align: center;">Detalles de producto</h2>
                            <p class = "text"><strong>Nombre:</strong> <input type="text" name="nombre" value="${prod.value.nombre}"><br />&nbsp&nbsp<strong>Artista:</strong> <input type="text" name="artista" value="${prod.value.artista}"><br />&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<strong>Pais:</strong> <input type="text" name="pais" value="${prod.value.pais}"><br />&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<strong>Año:</strong> <input type="text" name="ano" value="${prod.value.ano}"><br /><strong>Precio Unitario:</strong> <input type="text" name="precioUnitario" value="${prod.value.precioUnitario}">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<br />&nbsp&nbsp&nbsp&nbsp<strong>Stock:</strong> <input type="text" name="stock" value="${prod.value.stock}"></p>
                            <button type = "submit" onclick = "document.getElementById('tipo${prod.key}').value='modificarProducto';">Modificar</button>
                            <button type = "submit" onclick = "document.getElementById('tipo${prod.key}').value='borrarProducto';">Borrar</button>
                        </div>
                    </div>
                </div>
            </form>
        </c:forEach>
            
        
        
        <form class = "center" id = "form" method = "POST" action = "./ServletAdministracionProductos" >
            <input type = "hidden" id = "hidden" name = "tipo"/>
                <div id="openModalNuevo" class="modalDialog">
                    <div>
                        <a href="#close" title="Close" class="close">X</a>
                        <div class="wrap formulario">
                            <h2 style="text-align: center;">Detalles de producto</h2>
                            <p class = "text"><strong>Nombre:</strong> <input type="text" name="nombre" ><br />&nbsp&nbsp<strong>Artista:</strong> <input type="text" name="artista"><br />&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<strong>Pais:</strong> <input type="text" name="pais"><br />&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<strong>Año:</strong> <input type="text" name="ano" ><br /><strong>Precio Unitario:</strong> <input type="text" name="precioUnitario">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<br />&nbsp&nbsp&nbsp&nbsp<strong>Stock:</strong> <input type="text" name="stock" ></p>
                            <button type = "submit" onclick = "document.getElementById('hidden').value='nuevoProducto';">Crear</button>
                        </div>
                    </div>
                </div>
        </form>
    </body>
    <footer align = "right"><strong>Contacto:</strong> tiendademusicadawa@gmail.com</footer>
</html>