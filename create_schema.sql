
-- Base de datos: `bd_dawa`
CREATE DATABASE IF NOT EXISTS `bd_dawa` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `bd_dawa`;

-- Estructura de tabla para la tabla `productos`
CREATE TABLE IF NOT EXISTS `productos` (
  `id` int(11) NOT NULL,
  `nombre` varchar(40) NOT NULL,
  `artista` varchar(40) NOT NULL,
  `pais` varchar(40) NOT NULL,
  `precio` float NOT NULL,
  `ano` varchar(10) NOT NULL,
  `stock` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Estructura de tabla para la tabla `usuarios`
CREATE TABLE IF NOT EXISTS `usuarios` (
  `nombreUsuario` varchar(40) NOT NULL,
  `email` varchar(40) NOT NULL,
  `pass` varchar(30) NOT NULL,
  `rango` varchar(30) NOT NULL,
  `gastoTotal` float NOT NULL,
  `tipo` set('cliente','admin') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Estructura de tabla para la tabla `comentarios`
CREATE TABLE IF NOT EXISTS `comentarios` (
  `producto` int(11) NOT NULL,
  `usuario` varchar(40) NOT NULL,
  `comentario` longtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Estructura de tabla para la tabla `ventas`
CREATE TABLE IF NOT EXISTS `ventas` (
  `id` int(11) NOT NULL,
  `nombreUsuario` varchar(40) NOT NULL,
  `total` float NOT NULL,
  `iva` float NOT NULL,
  `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Estructura de tabla para la tabla `lineasventa`
CREATE TABLE IF NOT EXISTS `lineasventa` (
  `venta` int(11) NOT NULL,
  `producto` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precioUnitario` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Estructura de tabla para la tabla `valoraciones`
CREATE TABLE IF NOT EXISTS `valoraciones` (
  `producto` int(11) NOT NULL,
  `usuario` varchar(40) NOT NULL,
  `valoracion` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Indices de la tabla `productos`
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id`);
  
-- Indices de la tabla `usuarios`
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`nombreUsuario`);

-- Indices de la tabla `ventas`
ALTER TABLE `ventas`
  ADD PRIMARY KEY (`id`);

