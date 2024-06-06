package com.insightlab.sgpo.controllers;

import com.insightlab.sgpo.data.dtos.v1.supplier.SupplierRequestDTO;
import com.insightlab.sgpo.data.dtos.v1.supplier.SupplierResponseDTO;
import com.insightlab.sgpo.services.SupplierService;
import com.insightlab.sgpo.utils.swagger.SupplierControllerSwaggerOptions;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/suppliers")
@Tag(name = "Supplier", description = "Endpoints for managing suppliers")
public class SupplierController {

    @Autowired
    SupplierService supplierService;

    @PostMapping
    @SupplierControllerSwaggerOptions.CreateSupplierOptions
    public ResponseEntity<SupplierResponseDTO> createSupplier(@RequestBody SupplierRequestDTO supplierRequestDTO){

        SupplierResponseDTO supplierResponseDTO = this.supplierService.createSupplier(supplierRequestDTO);

        return ResponseEntity.ok().body(supplierResponseDTO);
    }

    @GetMapping
    @SupplierControllerSwaggerOptions.GetAllSuppliersOptions
    public ResponseEntity<List<SupplierResponseDTO>> getAllSuppliers(){
        List<SupplierResponseDTO> suppliers = this.supplierService.getAllSuppliers();

        return ResponseEntity.ok().body(suppliers);
    }

    @GetMapping(value = "/{id}")
    @SupplierControllerSwaggerOptions.GetSupplierOptions
    public ResponseEntity<SupplierResponseDTO> getSupplierById(
            @PathVariable String id
    ){
        SupplierResponseDTO supplier = this.supplierService.findSupplierById(id);

        return ResponseEntity.ok().body(supplier);
    }

    @PutMapping(value = "/{id}")
    @SupplierControllerSwaggerOptions.UpdateSupplierOptions
    public ResponseEntity<SupplierResponseDTO> updateSupplier(
            @PathVariable String id,
            @RequestBody SupplierRequestDTO supplierRequestDTO
    ){
        SupplierResponseDTO supplierUpdated = this.supplierService.updateSupplier(id, supplierRequestDTO);

        return ResponseEntity.ok().body(supplierUpdated);
    }

    @DeleteMapping(value = "/{id}")
    @SupplierControllerSwaggerOptions.DeleteSupplierOptions
    public ResponseEntity<SupplierResponseDTO> deleteSupplier(
            @PathVariable String id
    ){
        this.supplierService.deleteSupplier(id);

        return ResponseEntity.noContent().build();
    }

}
