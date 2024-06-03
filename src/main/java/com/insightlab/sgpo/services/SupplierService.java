package com.insightlab.sgpo.services;

import com.insightlab.sgpo.data.dtos.v1.supplier.SupplierRequestDTO;
import com.insightlab.sgpo.data.dtos.v1.supplier.SupplierResponseDTO;
import com.insightlab.sgpo.domain.supplier.Supplier;
import com.insightlab.sgpo.domain.supplier.exceptions.SupplierAlreadyExistsException;
import com.insightlab.sgpo.domain.supplier.exceptions.SupplierNotFoundException;
import com.insightlab.sgpo.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    @Autowired
    SupplierRepository repository;

    public SupplierResponseDTO findSupplierById(String id){
        Supplier supplierDB = this.repository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier with id: " + id + " was not found!"));

        return new SupplierResponseDTO(
                supplierDB.getId(),
                supplierDB.getName(),
                supplierDB.getTaxId(),
                supplierDB.getPhone(),
                supplierDB.getEmail(),
                supplierDB.getStatus(),
                supplierDB.getCreatedAt()
        );
    }

    public SupplierResponseDTO createSupplier(SupplierRequestDTO supplierRequest){

        if (checkIfExistsSupplierByTaxId(supplierRequest.taxId())){
            throw new SupplierAlreadyExistsException("Supplier with taxId: " + supplierRequest.taxId() + " already exists.");
        }

        Supplier supplier = new Supplier();

        supplier.setName(supplierRequest.name());
        supplier.setTaxId(supplierRequest.taxId());
        supplier.setPhone(supplierRequest.phone());
        supplier.setEmail(supplierRequest.email());
        supplier.setStatus(supplierRequest.status());
        supplier.setCreatedAt(LocalDateTime.now());

        this.repository.save(supplier);

        return new SupplierResponseDTO(
                supplier.getId(),
                supplier.getName(),
                supplier.getTaxId(),
                supplier.getPhone(),
                supplier.getEmail(),
                supplier.getStatus(),
                supplier.getCreatedAt()
        );
    }

    public List<SupplierResponseDTO> getAllSuppliers(){
        List<Supplier> suppliers = this.repository.findAll();

        return suppliers.stream().map(
                supplier -> new SupplierResponseDTO(
                        supplier.getId(),
                        supplier.getName(),
                        supplier.getTaxId(),
                        supplier.getPhone(),
                        supplier.getEmail(),
                        supplier.getStatus(),
                        supplier.getCreatedAt())
        ).toList();
    }

    public SupplierResponseDTO updateSupplier(String id, SupplierRequestDTO supplierRequest){
        Supplier supplierDB = this.repository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier with id: " + id + " was not found!"));

        supplierDB.setName(supplierRequest.name());
        supplierDB.setTaxId(supplierRequest.taxId());
        supplierDB.setPhone(supplierRequest.phone());
        supplierDB.setEmail(supplierRequest.email());
        supplierDB.setStatus(supplierRequest.status());

        this.repository.save(supplierDB);

        return new SupplierResponseDTO(
                supplierDB.getId(),
                supplierDB.getName(),
                supplierDB.getTaxId(),
                supplierDB.getPhone(),
                supplierDB.getEmail(),
                supplierDB.getStatus(),
                supplierDB.getCreatedAt()
        );
    }

    public void deleteSupplier(String id){
        Supplier supplierDB = this.repository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier with id: " + id + " was not found!"));

        this.repository.delete(supplierDB);
    }

    private Boolean checkIfExistsSupplierByTaxId(String taxId){
        Optional<Supplier> supplier = this.repository.findSupplierByTaxId(taxId);

        return supplier.isPresent();
    }


}
