insert into afiliados values(1, 'Soriano', 'Morales', 52953, 'No cuenta con NSS', 0, 'MOSL891031HMCRRS09', 'Coahuila 4', 'dean.kikin@gmail.com', 'Estado de México', 'universidad (Trunca)', 'Soltero', null, 'Renta', '2019/03/11', '1989/10/#1', '2019/04/12', null, 'Tlalneapantla Edo. de Méx.', 'Atizapán de Zaragoza', 'Luis Enrique', 912939764839, 0, null, 'Desarrollador', 'MEX', 'Ninguno', 'MOSL891031CBA', 'M', 5558257904, 5532255338, 'Médico', 'Titular' )

INSERT INTO `usuarios` (username, password, enabled) VALUES ('luis','$2a$10$WkpRCpf0aldniENn8iNqwerl7FQTzaL.IhgLFE8NxK3b.RWZFEf0e',1);
INSERT INTO `usuarios` (username, password, enabled) VALUES ('admin','$2a$10$7fi8Xu09Nv.oxQb0cJ5xPOUBa/fueyDyHRtG9QwDIl85m0OCYO3Mm',1);

INSERT INTO `authorities` (user_id, authority) VALUES (1,'ROLE_USER');
INSERT INTO `authorities` (user_id, authority) VALUES (2,'ROLE_ADMIN');
INSERT INTO `authorities` (user_id, authority) VALUES (2,'ROLE_USER');