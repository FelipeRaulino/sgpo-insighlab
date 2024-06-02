package com.insightlab.sgpo.repositories;

import com.insightlab.sgpo.domain.supplier.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, String> {

    Optional<Supplier> findSupplierByTaxId(String taxId);

}
