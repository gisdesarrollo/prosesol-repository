INSERT INTO `centros_contactos` (`id_centro_contacto`, `descripcion`, `estatus`, `mail_responsable`, `nombre`, `nombre_responsable`, `telefono1`, `telefono2`, `telefono_responsable`) VALUES ('1', 'Centro de Prueba', TRUE, 'mail@mail.com', 'Centro de Prueba 1', 'Juan Gomez', '123456789', '23456789', '234567890');

INSERT INTO `servicios` (`id_servicio`, `costo_beneficiario`, `costo_titular`, `estatus`, `inscripcion_beneficiario`, `inscripcion_titular`, `nombre`, `tipo_privacidad`, `id_centro_contacto`) VALUES ('1', '100', '100', TRUE, '100', '100', 'Prosesol Total', FALSE, '1');

INSERT INTO periodicidades(nombre, periodo)values('MENSUAL', 'Mensual'),('ANUAL', 'anual'),('SEMESTRAL', 'Semestral');

insert into promotores(nombre, apellido_paterno, apellido_materno, fecha_alta, edad, email, estatus) values('Cuenta Corporativa', 'N/A', 'N/A', '2019/05/08', null, 'N/A', true),('José Martín', 'Hernández', 'Sánchez', '2019/05/07', 25, 'martin@gmail.com', true),('Aldo', 'Contreras', 'Muñoz', '2019/05/07', 33, 'aldo@gmail.com', true),('Javier', 'Cisneros', 'Aguilar', '2019/05/07', 24, 'javier@gmail.com', true),('Estela', 'Reyes', 'Ocampo', '2019/05/07', 28, 'estela@gmail.com', true);

insert into cuentas_comerciales(razon_social, rfc, direccion, codigo_postal, pais, email, fecha_alta) values('SOCIEDAD S.A. de C.V.', 'SOFR891031CBA', 'Coahuila No. 4', '55438', 'México', 'sociedad@gmail.com', '2019/05/07'),('EMPRESA S.A. de C.V.', 'EMFR891031CBA', 'Coahuila No. 18', '53456', 'México', 'empresa@gmail.com', '2019/05/07'),('Walmart S.A. de C.V.', 'WTFR891031CBA', 'Adolfo López Mateos No. 18', '53456', 'México', 'walmart@walmart.com.mx', '2019/05/07'),('UnderArmor S.A. de C.V.', 'UAFR891031CBA', 'Vicente Guerrero # 85', '54368', 'México', 'ua@underarmor.com', '2019/05/07'),('Unitec S.A. de C.V.', 'UTFR891031CBA', 'Coyoacán 37 Atizapán de Zaragoza', '52785', 'México', 'contacto@unitec.com', '2019/05/07');

insert into rel_promotores_cuentas(id_promotor, id_cta_comercial)values(2,3),(1,1),(1,2),(4,5);

INSERT INTO `afiliados` (`id_afiliado`,`apellido_materno`,`apellido_paterno`,`clave`,`codigo_postal`,`comentarios`,`curp`,`direccion`,`email`,`entidad_federativa`,`estado_civil`,`estatus`,`fecha_afiliacion`,`fecha_alta`,`fecha_corte`,`fecha_nacimiento`,`infonavit`,`inscripcion`,`is_beneficiario`,`lugar_nacimiento`,`municipio`,`nombre`,`nss`,`dependientes`,`numero_infonavit`,`ocupacion`,`pais`,`rfc`,`saldo_acumulado`,`saldo_corte`,`sexo`,`telefono_fijo`,`telefono_movil`,`id_cta_comercial`,`id_periodicidad`,`id_promotor`,`id_servicio`) VALUES (1,'Soto','Ayala','PR-1610324600',53126,'','AASR850601HDFYTL09','Sabadoñas 114','e.ayalasoto@gmail.com','AGU','Soltero',3,NULL,'2019-07-29','2019-08-01','1985-06-01',NULL,NULL,'0','','Naucalpan de Juarez','Eduardo',NULL,NULL,NULL,'','MEX','AASE850601GR3',NULL,NULL,'Masculino',5536604985,NULL,NULL,1,NULL,1);
INSERT INTO `afiliados` (`id_afiliado`,`apellido_materno`,`apellido_paterno`,`clave`,`codigo_postal`,`comentarios`,`curp`,`direccion`,`email`,`entidad_federativa`,`estado_civil`,`estatus`,`fecha_afiliacion`,`fecha_alta`,`fecha_corte`,`fecha_nacimiento`,`infonavit`,`inscripcion`,`is_beneficiario`,`lugar_nacimiento`,`municipio`,`nombre`,`nss`,`dependientes`,`numero_infonavit`,`ocupacion`,`pais`,`rfc`,`saldo_acumulado`,`saldo_corte`,`sexo`,`telefono_fijo`,`telefono_movil`,`id_cta_comercial`,`id_periodicidad`,`id_promotor`,`id_servicio`) VALUES (2,'Mendez','Ayala','PR-1610324689',53126,'','AASR850601HDFYTL08','Sabadoñas 114','e.ayalasoto@gmail.com','AGU','Soltero',3,NULL,'2019-07-29','2019-08-01','1985-06-01',NULL,NULL,TRUE,'','Naucalpan de Juarez','Jose',NULL,NULL,NULL,'','MEX','AASA850601GR3',NULL,NULL,'Masculino',5536604985,NULL,NULL,TRUE,NULL,TRUE);

INSERT INTO `usuarios` (nombre, username, password, estatus, id_centro_contacto) VALUES ('Luis Enrique Morales Soriano','luis','$2a$10$WkpRCpf0aldniENn8iNqwerl7FQTzaL.IhgLFE8NxK3b.RWZFEf0e',1,NULL);
INSERT INTO `usuarios` (nombre, username, password, estatus, id_centro_contacto) VALUES ('Administrador','admin','$2a$10$7fi8Xu09Nv.oxQb0cJ5xPOUBa/fueyDyHRtG9QwDIl85m0OCYO3Mm',1,NULL);
INSERT INTO `usuarios` (nombre, username, password, estatus, id_centro_contacto) VALUES ('Operador de Centro de Asistencia','asistencia','$2a$10$7fi8Xu09Nv.oxQb0cJ5xPOUBa/fueyDyHRtG9QwDIl85m0OCYO3Mm',1,1);

INSERT INTO perfiles (nombre, descripcion, estatus) VALUES('admin', 'Administrador del sistema', 1),('asistencia', 'Operador de Centro de Asistencia', 1),('user', 'Usuario Corporativo', 1);

INSERT INTO rel_usuarios_perfiles(id_usuario, id_perfil) values(1,2),(2,1),(3,3);

INSERT INTO `roles` (id_perfil, nombre) VALUES (1,'ROLE_ADMINISTRADOR');
INSERT INTO `roles` (id_perfil, nombre) VALUES (2,'ROLE_USUARIO');
INSERT INTO `roles` (id_perfil, nombre) VALUES (3,'ROLE_ASISTENCIA');



