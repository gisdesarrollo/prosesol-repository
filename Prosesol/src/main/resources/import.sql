insert into afiliados values(1, 'Soriano', 'Morales', '4000-0001', 52953, 'No cuenta con NSS','MOSL891031HMCRRS09', 'Coahuila 4', 'dean.kikin@gmail.com', 'MEX', 'Soltero', true, '2019/04/12', '2019/05/11', '2019/04/23', '1989/10/31',  null, 200.00, false, 'Tlalneapantla Edo. de Méx.', 'Atizapán de Zaragoza', 'Luis Enrique', 912939764839, 0, null, 'Desarrollador', 'MEX', 'MOSL891031CBA', 1100.00, 2000.00,'M', 5558257904, 5532255338, null, 2, 3, 1);
insert into afiliados values(2, 'Pérez', 'Jiménez', '4000-0002', 52953, 'No cuenta con NSS', 'MOSL891031HMCRRS09', 'Coahuila 4', 'dean.kikin@gmail.com', 'MEX', 'Soltero', true, '2019/04/12', '2019/05/11', '2019/04/23', '1989/10/31', null, 200.00, true,'Tlalneapantla Edo. de Méx.', 'Atizapán de Zaragoza', 'Alberto', 912939764839, 0, null, 'Desarrollador', 'MEX', 'MOSL891031CBA', 450.00, 450.00, 'M', 5558257904, 553225533, null, 3, 3, 2);
insert into afiliados values(3, 'Soriano', 'Morales', '4000-0003', 52953, 'No cuenta con NSS', 'MOSL891031HMCRRS09', 'Coahuila 4', 'dean.kikin@gmail.com', 'MEX', 'Soltero', true, '2019/04/12', '2019/05/11', '2019/04/23', '1989/10/31', null, 200.00, true,'Tlalneapantla Edo. de Méx.', 'Atizapán de Zaragoza', 'María Fernanda', 912939764839, 0, null, 'Desarrollador', 'MEX', 'MOSL891031CBA', 450.00, 450.00, 'M', 5558257904, 5532255338, null, 3, 3, 2);

insert into afiliados values(4, 'Fernández', 'Pérez', '4000-0004', 52953, 'No cuenta con NSS', 'MOSL891031HMCRRS09', 'Coahuila 4', 'luisenrique.moralessoriano@gmail.com', 'MEX', 'Soltero', true, '2019/04/12', '2019/05/07', '2019/04/23', '1989/10/31', null, 150.00, false, 'Tlalneapantla Edo. de Méx.', 'Atizapán de Zaragoza', 'Javier', 912939764839, 0, null, 'Desarrollador', 'MEX', 'MOSL891031CBA', 2500.00, 4000.00, 'M', 5558257904, 5532255338, 4, 1, 5, 3);
insert into afiliados values(5, 'García', 'Sánchez', '4000-0005', 52953, 'No cuenta con NSS', 'MOSL891031HMCRRS09', 'Coahuila 4', 'dean.kikin@gmail.com', 'MEX', 'Soltero', true, '2019/04/12', '2019/05/11', '2019/04/23', '1989/10/31', null, 150.00, true,'Tlalneapantla Edo. de Méx.', 'Atizapán de Zaragoza', 'Juan Carlos', 912939764839, 0, null, 'Desarrollador', 'MEX', 'MOSL891031CBA', 1000.00, 1000.00, 'M', 5558257904, 5532255338, 4, 2, 5, 2);
insert into afiliados values(6, 'García', 'Sánchez', '4000-0005', 52953, 'No cuenta con NSS', 'MOSL891031HMCRRS09', 'Coahuila 4', 'dean.kikin@gmail.com', 'MEX', 'Soltero', true, '2019/04/12', '2019/05/11', '2019/04/23', '1989/10/31', null, 150.00, true,'Tlalneapantla Edo. de Méx.', 'Atizapán de Zaragoza', 'Juan Carlos', 912939764839, 0, null, 'Desarrollador', 'MEX', 'MOSL891031CBA', 3000.00, 1000.00, 'M', 5558257904, 5532255338, 4, 2, 5, 2);

insert into promotores(nombre, apellido_paterno, apellido_materno, fecha_alta, edad, email, estatus) values('Cuenta Corporativa', 'N/A', 'N/A', '2019/05/08', null, 'N/A', true),('José Martín', 'Hernández', 'Sánchez', '2019/05/07', 25, 'martin@gmail.com', true),('Aldo', 'Contreras', 'Muñoz', '2019/05/07', 33, 'aldo@gmail.com', true),('Javier', 'Cisneros', 'Aguilar', '2019/05/07', 24, 'javier@gmail.com', true),('Estela', 'Reyes', 'Ocampo', '2019/05/07', 28, 'estela@gmail.com', true);

insert into cuentas_comerciales(razon_social, rfc, direccion, codigo_postal, pais, email, fecha_alta) values('SOCIEDAD S.A. de C.V.', 'SOFR891031CBA', 'Coahuila No. 4', '55438', 'México', 'sociedad@gmail.com', '2019/05/07'),('EMPRESA S.A. de C.V.', 'EMFR891031CBA', 'Coahuila No. 18', '53456', 'México', 'empresa@gmail.com', '2019/05/07'),('Walmart S.A. de C.V.', 'WTFR891031CBA', 'Adolfo López Mateos No. 18', '53456', 'México', 'walmart@walmart.com.mx', '2019/05/07'),('UnderArmor S.A. de C.V.', 'UAFR891031CBA', 'Vicente Guerrero # 85', '54368', 'México', 'ua@underarmor.com', '2019/05/07'),('Unitec S.A. de C.V.', 'UTFR891031CBA', 'Coyoacán 37 Atizapán de Zaragoza', '52785', 'México', 'contacto@unitec.com', '2019/05/07');

insert into rel_promotores_cuentas(id_promotor, id_cta_comercial)values(2,3),(1,1),(1,2),(4,5);

INSERT INTO rel_afiliados_beneficiarios (id_beneficiario, fecha_creacion, estatus, afiliado_id_afiliado) values(2, '2019/04/03', true, 1);
INSERT INTO rel_afiliados_beneficiarios (id_beneficiario, fecha_creacion, estatus, afiliado_id_afiliado) values(3, '2019/04/03', true, 1);
INSERT INTO rel_afiliados_beneficiarios (id_beneficiario, fecha_creacion, estatus, afiliado_id_afiliado) values(5, '2019/04/03', true, 4);
INSERT INTO rel_afiliados_beneficiarios (id_beneficiario, fecha_creacion, estatus, afiliado_id_afiliado) values(6, '2019/04/03', true, 4);

INSERT INTO `usuarios` (nombre, username, password, estatus) VALUES ('Luis Enrique Morales Soriano','luis','$2a$10$WkpRCpf0aldniENn8iNqwerl7FQTzaL.IhgLFE8NxK3b.RWZFEf0e',1);
INSERT INTO `usuarios` (nombre, username, password, estatus) VALUES ('Administrador','admin','$2a$10$7fi8Xu09Nv.oxQb0cJ5xPOUBa/fueyDyHRtG9QwDIl85m0OCYO3Mm',1);

INSERT INTO rel_usuarios_perfiles values(1,2),(2,1);

INSERT INTO perfiles (nombre, descripcion, estatus) VALUES('admin', 'Administrador del sistema', 1),('user', 'Usuario Corporativo', 1);

INSERT INTO `roles` (id_perfil, nombre) VALUES (2,'ROLE_USUARIO');
INSERT INTO `roles` (id_perfil, nombre) VALUES (1,'ROLE_ADMINISTRADOR');

INSERT INTO servicios(nombre, notas, costo, estatus)values('Ajustes Especiales', 'Ajustes especiales', 9.99, 1),('Prosesol total', 'Prosesol total', 19.99, 1),('Médica Prosesol', 'Médica Prosesol', 29.99, 1)

INSERT INTO periodicidades(nombre, periodo)values('MENSUAL', 'Mensual'),('ANUAL', 'anual'),('SEMESTRAL', 'Semestral');
