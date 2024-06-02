package com.insightlab.sgpo.unittests.mocks;

import com.insightlab.sgpo.data.dtos.v1.supplier.SupplierRequestDTO;
import com.insightlab.sgpo.data.dtos.v1.supplier.SupplierResponseDTO;
import com.insightlab.sgpo.domain.supplier.Supplier;

import java.time.LocalDateTime;
import java.util.UUID;

public class SupplierMock {

    public Supplier mockEntity(UUID id){
        Supplier supplier = new Supplier();

        supplier.setId(id.toString());
        supplier.setName("Tech Solutions LLC");
        supplier.setTaxId("55-6677889");
        supplier.setPhone("+1 415 555-6543");
        supplier.setEmail("info@techsolutions.com");
        supplier.setCreatedAt(LocalDateTime.now());
        supplier.setStatus(true);

        return supplier;
    }

    public SupplierRequestDTO mockDTO(){
        return new SupplierRequestDTO(
                "Tech Solutions LLC",
                "55-6677889",
                "+1 415 555-6543",
                "info@techsolutions.com"
        );
    }

}
