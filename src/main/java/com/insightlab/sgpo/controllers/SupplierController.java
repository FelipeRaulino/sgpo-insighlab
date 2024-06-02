package com.insightlab.sgpo.controllers;

import com.insightlab.sgpo.data.dtos.v1.supplier.SupplierRequestDTO;
import com.insightlab.sgpo.data.dtos.v1.supplier.SupplierResponseDTO;
import com.insightlab.sgpo.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/suppliers")
public class SupplierController {

    @Autowired
    SupplierService supplierService;

    @PostMapping
    public ResponseEntity<SupplierResponseDTO> createSupplier(@RequestBody SupplierRequestDTO supplierRequestDTO){

        SupplierResponseDTO supplierResponseDTO = this.supplierService.createSupplier(supplierRequestDTO);

        return ResponseEntity.ok().body(supplierResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponseDTO>> getAllSuppliers(){
        List<SupplierResponseDTO> suppliers = this.supplierService.getAllSuppliers();

        return ResponseEntity.ok().body(suppliers);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<SupplierResponseDTO> getSupplierById(
            @PathVariable String id
    ){
        SupplierResponseDTO supplier = this.supplierService.findSupplierById(id);

        return ResponseEntity.ok().body(supplier);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<SupplierResponseDTO> updateSupplier(
            @PathVariable String id,
            @RequestBody SupplierRequestDTO supplierRequestDTO
    ){
        SupplierResponseDTO supplierUpdated = this.supplierService.updateSupplier(id, supplierRequestDTO);

        return ResponseEntity.ok().body(supplierUpdated);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<SupplierResponseDTO> deleteSupplier(
            @PathVariable String id
    ){
        this.supplierService.deleteSupplier(id);

        return ResponseEntity.noContent().build();
    }

}
