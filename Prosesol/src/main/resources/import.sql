insert into afiliados values(1, 'Soriano', 'Morales', 52953, 'No cuenta con NSS', 0, 'MOSL891031HMCRRS09', 'Coahuila 4', 'dean.kikin@gmail.com', 'Estado de México', 'universidad (Trunca)', 'Soltero', null, 'Renta', '2019/03/11', '1989/10/#1', '2019/04/12', null, 'Tlalneapantla Edo. de Méx.', 'Atizapán de Zaragoza', 'Luis Enrique', 912939764839, 0, null, 'Desarrollador', 'MEX', 'Ninguno', 'MOSL891031CBA', 'M', 5558257904, 5532255338, 'Médico', 'Titular' );
insert into afiliados values(2, 'Pérez', 'Jiménez', 52953, 'No cuenta con NSS', 0, 'MOSL891031HMCRRS09', 'Coahuila 4', 'dean.kikin@gmail.com', 'Estado de México', 'universidad (Trunca)', 'Soltero', null, 'Renta', '2019/03/11', '1989/10/#1', '2019/04/12', null, 'Tlalneapantla Edo. de Méx.', 'Atizapán de Zaragoza', 'Alberto', 912939764839, 0, null, 'Desarrollador', 'MEX', 'Ninguno', 'MOSL891031CBA', 'M', 5558257904, 5532255338, 'Médico', 'Titular' );

--insert into beneficiarios(nombre, apellido_paterno, apellido_materno, fecha_nacimiento, telefono_fijo, telefono_movil, email, direccion, municipio, codigo_postal, entidad_federativa, nss, rfc, curp) values('María Trinidad', 'Soriano', 'Fuentes', '1964/04/07', 5558257904, 5549135156, 'marysorianofuentes@gmail.com', 'Av. de Las Colonias 8', 'Atizapán de Zaragoza', 52953, 'Estado de México', '156271829283', 'MASF640407GTA', 'MASF640407SUTIOE08');
--insert into beneficiarios(nombre, apellido_paterno, apellido_materno, fecha_nacimiento, telefono_fijo, telefono_movil, email, direccion, municipio, codigo_postal, entidad_federativa, nss, rfc, curp) values('María Fernanda', 'Morales', 'Soriano', '1995/08/26', 5558257904, 5579066520, 'mariafernandamoralessoriano@gmail.com', 'Av. de Las Colonias 8', 'Atizapán de Zaragoza', 52953, 'Estado de México', '156737183729', 'FEMS950826UDI', 'FEMS950826THSUEID78');
--
--INSERT INTO rel_afiliado_beneficiario values(1, 1);
--INSERT INTO rel_afiliado_beneficiario values(1, 2);

INSERT INTO `usuarios` (nombre, username, password, estatus) VALUES ('Luis Enrique Morales Soriano','luis','$2a$10$WkpRCpf0aldniENn8iNqwerl7FQTzaL.IhgLFE8NxK3b.RWZFEf0e',1);
INSERT INTO `usuarios` (nombre, username, password, estatus) VALUES ('Administrador','admin','$2a$10$7fi8Xu09Nv.oxQb0cJ5xPOUBa/fueyDyHRtG9QwDIl85m0OCYO3Mm',1);

INSERT INTO `roles` (user_id, role_name) VALUES (1,'ROLE_USER');
INSERT INTO `roles` (user_id, role_name) VALUES (2,'ROLE_ADMIN');
INSERT INTO `roles` (user_id, role_name) VALUES (2,'ROLE_USER');

INSERT INTO perfiles(nombre_perfil)VALUES('ROLE_USER'),('ROLE_ADMIN');

