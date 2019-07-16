package com.prosesol.springboot.app.util;

public enum AfiliadoAsistencia {
	
	NOMBRE{
		@Override
		public String addQuery(String campo, boolean where) {
			
			String whereClause = "where";
			
			if(campo != "") {
				if(where) {
					whereClause += " nombre like '" + campo + "'";
				}
			}else {
				return null;
			}
			
			return whereClause;
		}
	},
	
	APELLIDO_PATERNO{

		@Override
		public String addQuery(String campo, boolean where) {

			String whereClause = "where";
			String andClause = " and";
			
			if(campo != "") {
				if(where) {
					whereClause += " apellidoPaterno like '" + campo + "'";
					
					return whereClause;
				}else {
					andClause += " apellidoPaterno like '" + campo + "'";
					
					return andClause;
				}
			}else {
				return null;
			}		
		}		
	},
	
	APELLIDO_MATERNO{

		@Override
		public String addQuery(String campo, boolean where) {
			
			String whereClause = "where";
			String andClause = " and";
			
			if(campo != "") {
				if(where) {
					whereClause += " apellidoMaterno like '" + campo + "'";
					
					return whereClause;
				}else {
					andClause += " apellidoMaterno like '" + campo + "'";
					
					return andClause;
				}
			}else {
				return null;
			}
		}		
	},
	
	RFC{

		@Override
		public String addQuery(String campo, boolean where) {

			String whereClause = "where";
			String andClause = " and";
			
			if(campo != "") {
				if(where) {
					whereClause += " rfc like '" + campo + "'";
					
					return whereClause;
				}else {
					andClause += " rfc like '" + campo + "'";
					
					return andClause;
				}
			}else {
				return null;
			}
			
		}		
	},
	
	TELEFONO{

		@Override
		public String addQuery(String campo, boolean where) {
			
			String whereClause = "where";
			String andClause = " and";
			String orClause = " or";
			
			if(campo != "") {
				if(where) {
					whereClause += " telefonoFijo = " + campo + orClause + " telefonoMovil = " + campo;
					
					return whereClause;
				}else {
					andClause += " telefonoFijo = " + campo + orClause + " telefonoMovil = " + campo;
					
					return andClause;
				}
			}else {
				return null;
			}
		}		
	},
	
	CLAVE{

		@Override
		public String addQuery(String campo, boolean where) {
			
			String whereClause = "where";
			String andClause = " and";
			
			if(campo != "") {
				if(where) {
					whereClause += " clave like '" + campo + "'";
					
					return whereClause;
				}else {
					andClause += " clave like '" + campo + "'";
					
					return andClause;
				}
			}else {
				return null;
			}
		}		
	};

	public abstract String addQuery(String campo, boolean where) ;
	
}
