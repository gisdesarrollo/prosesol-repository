package com.prosesol.springboot.app.view.excel;

import java.text.DateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.prosesol.springboot.app.entity.Afiliado;

@Component("afiliados/descargar.xlsx")
public class ReportesExcel extends AbstractXlsxView{
	
	private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT);

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setHeader("Content-Disposition", "attachment; filename=\"afiliados_view.xlsx\"");
		
		MessageSourceAccessor mensajes = getMessageSourceAccessor();
		
		@SuppressWarnings("unchecked")
		List<Afiliado> afiliados = (List<Afiliado>)model.get("afiliados");
		Sheet sheet = workbook.createSheet("Reporte Afiliados");
		
		Row encabezado = sheet.createRow(0);
		encabezado.createCell(0).setCellValue(mensajes.getMessage("text.afiliado.ver.nombre"));
		encabezado.createCell(1).setCellValue(mensajes.getMessage("text.afiliado.ver.tipoAfiliado"));
		encabezado.createCell(2).setCellValue(mensajes.getMessage("text.afiliado.ver.estatus"));
		encabezado.createCell(3).setCellValue(mensajes.getMessage("text.afiliado.ver.servicio"));
		encabezado.createCell(4).setCellValue(mensajes.getMessage("text.afiliado.ver.saldoAcumulado"));
		encabezado.createCell(5).setCellValue(mensajes.getMessage("text.afiliado.ver.saldoCorte"));
		encabezado.createCell(6).setCellValue(mensajes.getMessage("text.afiliado.ver.fechaInicioServicio"));
		encabezado.createCell(7).setCellValue(mensajes.getMessage("text.afiliado.ver.fechaCorte"));
		encabezado.createCell(8).setCellValue(mensajes.getMessage("text.afiliado.ver.fechaAfiliacion"));
		encabezado.createCell(9).setCellValue(mensajes.getMessage("text.afiliado.ver.periodo"));
		encabezado.createCell(10).setCellValue(mensajes.getMessage("text.afiliado.ver.email"));
		
		int rowNum = 1;
		for(Afiliado afiliado : afiliados) {
			Row courseRow = sheet.createRow(rowNum);
			courseRow.createCell(0).setCellValue(afiliado.getNombre() + " " + 
												 afiliado.getApellidoMaterno() + " "+ 
												 afiliado.getApellidoMaterno());
			courseRow.createCell(1).setCellValue(afiliado.getIsBeneficiario() ? "Beneficiario" : "Titular");
			
			if(afiliado.getEstatus() == 1) {
				courseRow.createCell(2).setCellValue("Activo");
			}else if(afiliado.getEstatus() == 2) {
				courseRow.createCell(2).setCellValue("Inactivo");
			}else if(afiliado.getEstatus() == 3) {
				courseRow.createCell(2).setCellValue("Candidato");
			}
			
			courseRow.createCell(3).setCellValue(afiliado.getServicio().getNombre());
			courseRow.createCell(4).setCellValue(afiliado.getSaldoAcumulado());
			courseRow.createCell(5).setCellValue(afiliado.getSaldoCorte());
			courseRow.createCell(8).setCellValue(DATE_FORMAT.format(afiliado.getFechaAfiliacion()));
			courseRow.createCell(7).setCellValue(DATE_FORMAT.format(afiliado.getFechaCorte()));			
			courseRow.createCell(9).setCellValue(afiliado.getPeriodicidad().getNombre());
			courseRow.createCell(10).setCellValue(afiliado.getEmail());
		}
		
	}

}
