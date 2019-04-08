insert into afiliados values(1, 'Soriano', 'Morales', 52953, 'No cuenta con NSS', 0, 'MOSL891031HMCRRS09', 'Coahuila 4', 'dean.kikin@gmail.com', 'Estado de México', 'universidad (Trunca)', 'Soltero', true, 'Renta', '2019/03/11', '1989/10/#1', '2019/04/12', null, false, 'Tlalneapantla Edo. de Méx.', 'Atizapán de Zaragoza', 'Luis Enrique', 912939764839, 0, null, 'Desarrollador', 'MEX', 'Ninguno', 'MOSL891031CBA', 'M', 5558257904, 5532255338, 'Médico', 'Titular' );
insert into afiliados values(2, 'Pérez', 'Jiménez', 52953, 'No cuenta con NSS', 0, 'MOSL891031HMCRRS09', 'Coahuila 4', 'dean.kikin@gmail.com', 'Estado de México', 'universidad (Trunca)', 'Soltero', true, 'Renta', '2019/03/11', '1989/10/#1', '2019/04/12', null, true,'Tlalneapantla Edo. de Méx.', 'Atizapán de Zaragoza', 'Alberto', 912939764839, 0, null, 'Desarrollador', 'MEX', 'Ninguno', 'MOSL891031CBA', 'M', 5558257904, 5532255338, 'Médico', 'Titular' );
insert into afiliados values(3, 'Soriano', 'Morales', 52953, 'No cuenta con NSS', 0, 'MOSL891031HMCRRS09', 'Coahuila 4', 'dean.kikin@gmail.com', 'Estado de México', 'universidad (Trunca)', 'Soltero', true, 'Renta', '2019/03/11', '1989/10/#1', '2019/04/12', null, true,'Tlalneapantla Edo. de Méx.', 'Atizapán de Zaragoza', 'María Fernanda', 912939764839, 0, null, 'Desarrollador', 'MEX', 'Ninguno', 'MOSL891031CBA', 'M', 5558257904, 5532255338, 'Médico', 'Titular' );

insert into afiliados values(4, 'Fernández', 'Pérez', 52953, 'No cuenta con NSS', 0, 'MOSL891031HMCRRS09', 'Coahuila 4', 'dean.kikin@gmail.com', 'Estado de México', 'universidad (Trunca)', 'Soltero', true, 'Renta', '2019/03/11', '1989/10/#1', '2019/04/12', null, false, 'Tlalneapantla Edo. de Méx.', 'Atizapán de Zaragoza', 'Javier', 912939764839, 0, null, 'Desarrollador', 'MEX', 'Ninguno', 'MOSL891031CBA', 'M', 5558257904, 5532255338, 'Médico', 'Titular' );
insert into afiliados values(5, 'García', 'Sánchez', 52953, 'No cuenta con NSS', 0, 'MOSL891031HMCRRS09', 'Coahuila 4', 'dean.kikin@gmail.com', 'Estado de México', 'universidad (Trunca)', 'Soltero', true, 'Renta', '2019/03/11', '1989/10/#1', '2019/04/12', null, true,'Tlalneapantla Edo. de Méx.', 'Atizapán de Zaragoza', 'Juan Carlos', 912939764839, 0, null, 'Desarrollador', 'MEX', 'Ninguno', 'MOSL891031CBA', 'M', 5558257904, 5532255338, 'Médico', 'Titular' );

INSERT INTO rel_afiliados_beneficiarios (id_beneficiario, fecha_creacion, estatus, afiliado_id_afiliado) values(2, '2019/04/03', true, 1);
INSERT INTO rel_afiliados_beneficiarios (id_beneficiario, fecha_creacion, estatus, afiliado_id_afiliado) values(3, '2019/04/03', true, 1);
INSERT INTO rel_afiliados_beneficiarios (id_beneficiario, fecha_creacion, estatus, afiliado_id_afiliado) values(5, '2019/04/03', true, 4);

INSERT INTO `usuarios` (nombre, username, password, estatus) VALUES ('Luis Enrique Morales Soriano','luis','$2a$10$WkpRCpf0aldniENn8iNqwerl7FQTzaL.IhgLFE8NxK3b.RWZFEf0e',1);
INSERT INTO `usuarios` (nombre, username, password, estatus) VALUES ('Administrador','admin','$2a$10$7fi8Xu09Nv.oxQb0cJ5xPOUBa/fueyDyHRtG9QwDIl85m0OCYO3Mm',1);

INSERT INTO `roles` (user_id, role_name) VALUES (1,'ROLE_USER');
INSERT INTO `roles` (user_id, role_name) VALUES (2,'ROLE_ADMIN');
INSERT INTO `roles` (user_id, role_name) VALUES (2,'ROLE_USER');

INSERT INTO perfiles(nombre_perfil)VALUES('ROLE_USER'),('ROLE_ADMIN');

