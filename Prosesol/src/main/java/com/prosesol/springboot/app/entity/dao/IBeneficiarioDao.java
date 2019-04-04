package com.prosesol.springboot.app.entity.dao;

import java.util.List;

import org.jboss.logging.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.PathVariable;

import com.prosesol.springboot.app.entity.Beneficiario;

public interface IBeneficiarioDao extends CrudRepository<Beneficiario, Long> {

//	@Query(value = "select b.id, a.nombre, a.apellido_paterno, a.apellido_materno from afiliados a, rel_afiliados_beneficiarios b where a.id_afiliado = b.id_beneficiario and  a.is_beneficiario = true and a.id_afiliado = ?1", nativeQuery = true)
////	@Query("select b from Beneficiario b, Afiliado a where b.afiliado.id = ?1 and a.id = b.idBeneficiario")
//	public List<Beneficiario> getBeneficiarioByIdByIsBeneficiario(Long idAfiliado);

}
