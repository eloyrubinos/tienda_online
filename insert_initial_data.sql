
-- Volcado de datos para la tabla `productos`
INSERT INTO `productos` (`id`, `nombre`, `artista`, `pais`, `precio`, `ano`, `stock`) VALUES
(0, 'Yuan', 'The Guo Brothers', 'China', 14.95, '2017', 10),
(1, 'Drums of Passion', 'Babatunde Olatunji', 'Nigeria', 16.95, '2017', 10),
(2, 'Kaira', 'Tounami Diabate', 'Mali', 16.95, '2017', 10),
(3, 'The Lion is Loose', 'Eliades Ochoa', 'Cuba', 13.95, '2017', 10),
(4, 'Dance the Devil Away', 'Outback', 'Australia', 14.95, '2017', 10),
(5, 'Record of Changes', 'Samulnori', 'Korea', 12.95, '2017', 10),
(6, 'Djelika', 'Tounami Diabate', 'Mali', 14.95, '2017', 10),
(7, 'Rapture', 'Nusrat Fateh Ali Khan', 'Pakistan', 12.95, '2017', 10),
(8, 'Cesaria Evora', 'Cesaria Evora', 'Cape Verde', 16.95, '2017', 10),
(9, 'DAA', 'GSTIC', 'Spain', 50, '2017', 10);

-- Volcado de datos para la tabla `usuarios`
INSERT INTO `usuarios` (`nombreUsuario`, `email`, `pass`, `rango`, `gastoTotal`, `tipo`) VALUES
('Juan', 'juan22@gmail.com', 'juan123', 'vip', 0, 'cliente'),
('David', 'deivi@gmail.com', 'abc123', 'vip', 0, 'cliente'),
('cliente', 'cliente@gmail.com', 'cliente', 'vip', 0, 'cliente'),
('Marta', 'admmartagpc@gmail.com', '$G4zpach0.mgpc', 'vip', 0, 'admin'),
('Jose', 'admjosefp@gmail.com', '$G4zpach0.jfp', 'vip', 0, 'admin'),
('admin', 'admin@gmail.com', 'admin', 'vip', 0, 'admin'),
('Sandra', 'sandra.gomez@gmail.com', 'contrasenhasegura23', 'vip', 0, 'cliente');

-- Volcado de datos para la tabla `valoraciones`
INSERT INTO `valoraciones` (`producto`, `usuario`, `valoracion`) VALUES
(0, 'Juan', 3),
(1, 'Juan', 5),
(0, 'Sandra', 2),
(1, 'Sandra', 5),
(2, 'David', 1);

-- Volcado de datos para la tabla `comentarios`
INSERT INTO `comentarios` (`producto`, `usuario`, `comentario`) VALUES
(0, 'Juan', 'No está mal.'),
(1, 'Juan', 'Lo recomiendo.'),
(0, 'Sandra', 'No me gustó mucho.'),
(1, 'Sandra', 'Me ha encantado.'),
(2, 'David', 'Me arrepiento de haberlo comprado.');