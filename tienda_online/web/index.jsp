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

        <h1>Música para DAWA</h1>
        <hr>
        
        <div class="formulario">
        <form method = "POST" action = "./ServletAutenticacionRegistro" >
            <input type = "hidden" name = "tipo" value = "autenticacion" />

            <h2>Bienvenido a la tienda online de DAWA</h2>
            <h3>Identifícate para continuar</h3>
            <label class = "text">Usuario: <input type = "text" name = "usuario" pattern="^[a-zA-Z*@0-9_]{1,10}$" required/></label><br/>
            <label class = "text">Contraseña: <input type = "password" name = "clave" pattern="^[a-zA-Z *@0-9_-\.]{5,10}$" required/></label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<br/>


            <input class = "button" type = "submit" value = "Entrar"/>
            <p class="pregistro">¿Todavía no tienes cuenta? <a href="#openModal">¡Hazte una!</a></p>

        </form>
        </div>
        <hr>
        
        <div id="openModal" class="modalDialog">
            <div>
                <a href="#close" title="Close" class="close">X</a>
                <div class="wrap2">
                    <br/>
                <h2 style="text-align: center;">Registro</h2>
                <form method = "POST" action = "./ServletAutenticacionRegistro" >
                    <input type = "hidden" name = "tipo" value = "registro" />

                    <label class = "text">Usuario<span data-tooltip="Una cadena de máximo 10 caracteres alfanuméricos, '*', '@' o '_'." data-tooltip-position="top"><sup class="info">?</sup></span>:&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<input type = "text" name = "usuario" pattern="^[a-zA-Z*@0-9_]{1,10}$" required/></label><br/>
                    <label class = "text">Contraseña<span data-tooltip="Una cadena de entre 5 y 10 caracteres alfanuméricos, '*', '@', '_', '.' o '-'." data-tooltip-position="top"><sup class="info">?</sup></span>: <input type = "password" name = "clave" pattern="^[a-zA-Z *@0-9_-\.]{5,10}$" required/></label><br/>
                    <label class = "text">E-mail<span data-tooltip="Se requiere un email válido." data-tooltip-position="top"><sup class="info">?</sup></span>:&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<input type = "email" name = "email" pattern="^[a-zA-Z \._-0-9]{1,}@[a-zA-Z0-9]{1,}\.[a-zA-Z]{1,5}$" required/></label><br/>

                    <input class = "button" type = "submit" value = "Registrarse"/>
                    <br />

                </form>
                </div>
            </div>
        </div>

    </body>
    <footer align="right"><strong>Contacto:</strong> tiendademusicadawa@gmail.com</footer>
</html>
