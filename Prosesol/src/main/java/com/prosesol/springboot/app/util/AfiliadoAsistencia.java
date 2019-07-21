package com.prosesol.springboot.app.util;

public enum AfiliadoAsistencia {
	
	NOMBRE{
		@Override
		public String addQuery(String campo, boolean where, Long idCcUsuario) {
			
			String whereClause = "where";
			
			if(campo != "") {
				if(where) {
					whereClause += " s.id = a.servicio.id and "
								   + "cc.id = s.centroContacto.id and "
								   + "cc.id = " + idCcUsuario + " and "
								   + "a.nombre like '" + campo + "'";
								   
				}
			}else {
				return null;
			}
			
			return whereClause;
		}
	},
	
	APELLIDO_PATERNO{

		@Override
		public String addQuery(String campo, boolean where, Long idCcUsuario) {

			String whereClause = "where";
			String andClause = " and";
			
			if(campo != "") {
				if(where) {
					whereClause += " s.id = a.servicio.id and "
								   + "cc.id = s.centroContacto.id and "
								   + "cc.id = " + idCcUsuario + " and "								
								   + " apellido_paterno like '" + campo + "'";
					
					return whereClause;
				}else {
					andClause += " s.id = a.servicio.id and "
							   + "cc.id = s.centroContacto.id and "
							   + "cc.id = " + idCcUsuario + " and "
							   + " apellido_paterno like '" + campo + "'";
					
					return andClause;
				}
			}else {
				return null;
			}		
		}		
	},
	
	APELLIDO_MATERNO{

		@Override
		public String addQuery(String campo, boolean where, Long idCcUsuario) {
			
			String whereClause = "where";
			String andClause = " and";
			
			if(campo != "") {
				if(where) {
					whereClause += " s.id = a.servicio.id and "
								   + "cc.id = s.centroContacto.id and "
								   + "cc.id = " + idCcUsuario + " and " 
								   + " apellido_materno like '" + campo + "'";
					
					return whereClause;
				}else {
					andClause += " s.id = a.servicio.id and "
								 + "cc.id = s.centroContacto.id and "
								 + "cc.id = " + idCcUsuario + " and "
								 + " apellido_materno like '" + campo + "'";
					
					return andClause;
				}
			}else {
				return null;
			}
		}		
	},
	
	RFC{

		@Override
		public String addQuery(String campo, boolean where, Long idCcUsuario) {

			String whereClause = "where";
			String andClause = " and";
			
			if(campo != "") {
				if(where) {
					whereClause += " s.id = a.servicio.id and "
							 	   + "cc.id = s.centroContacto.id and "
							 	   + "cc.id = " + idCcUsuario + " and " 
							 	   + " rfc like '" + campo + "'";
					
					return whereClause;
				}else {
					andClause += " s.id = a.servicio.id and "
						 	   	 + "cc.id = s.centroContacto.id and "
						 	   	 + "cc.id = " + idCcUsuario + " and " 
								 + " rfc like '" + campo + "'";
					
					return andClause;
				}
			}else {
				return null;
			}
			
		}		
	},
	
	TELEFONO{

		@Override
		public String addQuery(String campo, boolean where, Long idCcUsuario) {
			
			String whereClause = "where";
			String andClause = " and";
			String orClause = " or";
			
			if(campo != "") {
				if(where) {
					whereClause += " s.id = a.servicio.id and "
							 	   	 + "cc.id = s.centroContacto.id and "							 	   	 
									 + "telefono_fijo = " + campo + orClause + " telefono_movil = " + campo + " and "
									 + "cc.id = " + idCcUsuario;
					
					return whereClause;
				}else {
					andClause += " s.id = a.servicio.id and "
						 	   	 + "cc.id = s.centroContacto.id and "							
						 	   	 + "telefono_fijo = " + campo + orClause + " telefono_movil = " + campo + " and "
						 	   	 + "cc.id = " + idCcUsuario;
					
					return andClause;
				}
			}else {
				return null;
			}
		}		
	},
	
	CLAVE{

		@Override
		public String addQuery(String campo, boolean where, Long idCcUsuario) {
			
			String whereClause = "where";
			String andClause = " and";
			
			if(campo != "") {
				if(where) {
					whereClause += " s.id = a.servicio.id and "
						 	   	 + "cc.id = s.centroContacto.id and "
						 	   	 + "cc.id = " + idCcUsuario + " and "
								 + " clave like '" + campo + "'";
					
					return whereClause;
				}else {
					andClause += " s.id = a.servicio.id and "
						 	   	 + "cc.id = s.centroContacto.id and "
						 	   	 + "cc.id = " + idCcUsuario + " and " 
								 + " clave like '" + campo + "'";
					
					return andClause;
				}
			}else {
				return null;
			}
		}		
	};

	public abstract String addQuery(String campo, boolean where, Long idCcUsuario) ;
	
}
