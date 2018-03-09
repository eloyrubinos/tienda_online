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
    </head>

    <body>

        <form style = "float: right;" method = "POST" action = "./ServletAutenticacionRegistro" >
            <input type = "hidden" name = "tipo" value = "logout" />
            <input type = "submit" value = "Logout" />
        </form>
        <h1>Música para DAWA</h1>
        <hr>

        <h2 align = "center">Administración de usuarios</h2>
        
        <div class = "center"><form class = "inline" action = "./ServletAdministracionUsuarios" method = "POST"><input class = "button" type = "submit" value = "Usuarios" disabled style = "background-color: #999;"/></form>&nbsp<form class = "inline" action = "./ServletAdministracionProductos" method = "POST"><input class = "button" type = "submit" value = "Productos" /></form></div>

            <c:if test="${clientes.size == 0}">
                <h2>No hay clientes.</h2>
            </c:if>

            <c:if test="${administradores.size == 0}">
                <h2>No hay administradores.</h2>
            </c:if>

            <table id="catalogo">
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>E-mail</th>
                        <th>Contraseña</th>
                        <th>Rango</th>
                        <th>Tipo</th>
                        <th>Detalles</th>
                    </tr>
                </thead>

                <tbody>
                <c:forEach var = "cl" items = "${clientes}">
                    <tr>
                        <td><strong>${cl.value.nombre}</strong></td>
                        <td>${cl.value.email}</td>
                        <td>${cl.value.pass}</td>
                        <td>${cl.value.rango}</td>
                        <td>${cl.value.tipo}</td>
                        <td><a href="#openModal${cl.value.nombre}"><span class="detalles">+</span></a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <table id="catalogo">
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>E-mail</th>
                        <th>Contraseña</th>
                        <th>Tipo</th>
                        <th>Detalles</th>
                    </tr>
                </thead>

                <tbody>
                <c:forEach var="adm" items="${administradores}">
                    <tr>
                        <td><strong>${adm.value.nombre}</strong></td>
                        <td>${adm.value.email}</td>
                        <td>${adm.value.pass}</td>
                        <td>${adm.value.tipo}</td>
                        <td><a href="#openModal${adm.value.nombre}"><span class="detalles">+</span></a></td>
                    </tr>
                </c:forEach>
            </tbody>
            </table>       
            
        <hr>

        <c:forEach var="adm" items="${administradores}">
            <form class = "center" id = "form" method = "POST" action = "./ServletAdministracionUsuarios" >
                <input type = "hidden" id = "tipo${adm.value.nombre}" name = "tipo" />
                <input type="hidden" id="clienteId" name="usuario" value="${adm.value.nombre}" />
                <div id="openModal${adm.value.nombre}" class="modalDialog">
                    <div>
                        <a href="#close" title="Close" class="close">X</a>
                        <div class="wrap formulario">
                            <h2 style="text-align: center;">Detalles de usuario</h2>
                            <p class = "text">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<strong>Email:</strong> <input type="text" name="email" value="${adm.value.email}"><br /><strong>Contraseña:</strong> <input type="text" name="pass" value="${adm.value.pass}"></p>
                            <button type = "submit" onclick = "document.getElementById('tipo${adm.value.nombre}').value='modificarUsuario';">Modificar</button>
                            <button type = "submit" onclick = "document.getElementById('tipo${adm.value.nombre}').value='borrarUsuario';">Borrar</button>
                        </div>
                    </div>
                </div>
            </form>
        </c:forEach>
        
        <c:forEach var="cl" items="${clientes}">
            <form class = "center" id = "form" method = "POST" action = "./ServletAdministracionUsuarios" >
                <input type = "hidden" id = "tipo${cl.value.nombre}" name = "tipo" />
                <input type="hidden" id="clienteId" name="usuario" value="${cl.value.nombre}" />
                <div id="openModal${cl.value.nombre}" class="modalDialog">
                    <div>
                        <a href="#close" title="Close" class="close">X</a>
                        <div class="wrap formulario">
                            <h2 style="text-align: center;">Detalles de usuario</h2>
                            <p class = "text">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<strong>Email:</strong> <input type="text" name="email" value="${cl.value.email}"><br /><strong>Contraseña:</strong> <input type="text" name="pass" value="${cl.value.pass}"></p>
                            <button type = "submit" onclick = "document.getElementById('tipo${cl.value.nombre}').value='modificarUsuario';">Modificar</button>
                            <button type = "submit" onclick = "document.getElementById('tipo${cl.value.nombre}').value='borrarUsuario';">Borrar</button>
                        </div>
                    </div>
                </div>
            </form>
        </c:forEach>
        
        
    </body>
</html>