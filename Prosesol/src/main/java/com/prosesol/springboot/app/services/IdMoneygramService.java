package com.prosesol.springboot.app.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.Empresa;
import com.prosesol.springboot.app.entity.Parametro;
import com.prosesol.springboot.app.entity.Promotor;
import com.prosesol.springboot.app.service.IEmpresaService;
import com.prosesol.springboot.app.service.IParametroService;

@Service
public class IdMoneygramService {

	protected static final Log LOG = LogFactory.getLog(IdMoneygramService.class);
	
	@Autowired
	private IEmpresaService empresaService;
	
	@Autowired
	private IParametroService parametroService;
	
	protected final long ID_MONEYGRAM = 1L;
	protected final int PADDING_SIZE = 8;
	
	public String generaIdMoneygram(Afiliado afiliado,Promotor promotor) {
		
		String idMoneygram= null;
		Empresa empresa = null;
		String clavePromotor = null;
		if(promotor == null) {
			empresa = empresaService.findById(afiliado.getPromotor().getEmpresa().getId());
			clavePromotor = afiliado.getPromotor().getClave();
		}else {
			empresa = empresaService.findById(promotor.getEmpresa().getId());
			clavePromotor = promotor.getClave();
		}
		Parametro parametro = parametroService.findById(ID_MONEYGRAM);
        
        if(empresa == null || parametro == null){
            LOG.error("error: El id de la empresa no se ha encontrado");
            return idMoneygram;
        }
        String valor = parametro.getValor();
        String clave = empresa.getClave();
        Long consecutivoEmpresa = empresa.getConsecutivo();
        
     // Verificar si la empresa trae un consecutivo
        	if(consecutivoEmpresa == null){
        		consecutivoEmpresa = 1L;
        	}else{
        		consecutivoEmpresa = consecutivoEmpresa + 1;
        	}

        empresa.setConsecutivo(consecutivoEmpresa);
        empresaService.save(empresa);
        String valueValidation="0";
        	String consecutivo = String.format("%0" + PADDING_SIZE + "d", consecutivoEmpresa);
        	 idMoneygram = valor + clave + clavePromotor + valueValidation + consecutivo;
        	
		return idMoneygram;
	}
}
