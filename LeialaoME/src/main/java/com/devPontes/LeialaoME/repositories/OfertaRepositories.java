package com.devPontes.LeialaoME.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devPontes.LeialaoME.model.entities.Oferta;

public interface OfertaRepositories extends JpaRepository<Oferta, Long> {

	@Query("SELECT o FROM Oferta o WHERE o.comprador.cpf = :cpfComprador "
			+ "AND o.valorOferta >= :valorMinimo ORDER BY o.valorOferta DESC")
	Set<Oferta> findOfertasMaisCarasDeComprador(@Param("cpfComprador") String cpfComprador, @Param("valorMinimo") Double valorMinimo);

	@Query("SELECT o FROM Oferta o WHERE o.vendedor.cnpj = :cpfComprador "
			+ "AND o.valorOferta >= :valorMinimo ORDER BY o.valorOferta DESC")
	Set<Oferta> findOfertasMaisCarasDeVendedor(@Param("cnpjComprador") String cnpjComprador, @Param("valorMinimo") Double valorMinimo);

	
}