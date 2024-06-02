package com.insightlab.sgpo.unittests.services;

import com.insightlab.sgpo.data.dtos.v1.supplier.SupplierRequestDTO;
import com.insightlab.sgpo.data.dtos.v1.supplier.SupplierResponseDTO;
import com.insightlab.sgpo.domain.supplier.Supplier;
import com.insightlab.sgpo.repositories.SupplierRepository;
import com.insightlab.sgpo.services.SupplierService;
import com.insightlab.sgpo.unittests.mocks.SupplierMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import java.util.Optional;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class SupplierServiceTest {

    SupplierMock supplierMock;

    @InjectMocks
    private SupplierService supplierService;

    @Mock
    private SupplierRepository supplierRepository;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        supplierMock = new SupplierMock();
    }

    @Test
    void shouldBeAbleToCreateANewSupplier(){
        UUID id = UUID.randomUUID();

        Supplier supplier = supplierMock.mockEntity(id);
        SupplierRequestDTO supplierRequestDTO = supplierMock.mockDTO();

        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        SupplierResponseDTO result = supplierService.createSupplier(supplierRequestDTO);

        assertNotNull(result);
        assertEquals("Tech Solutions LLC", result.name());
        assertEquals("55-6677889", result.taxId());
        assertEquals("+1 415 555-6543", result.phone());
        assertEquals("info@techsolutions.com", result.email());

        ArgumentCaptor<Supplier> supplierCaptor = ArgumentCaptor.forClass(Supplier.class);
        verify(supplierRepository).save(supplierCaptor.capture());
        Supplier capturedSupplier = supplierCaptor.getValue();

        assertNotNull(capturedSupplier);
        assertEquals("Tech Solutions LLC", capturedSupplier.getName());
        assertEquals("55-6677889", capturedSupplier.getTaxId());
        assertEquals("+1 415 555-6543", capturedSupplier.getPhone());
        assertEquals("info@techsolutions.com", capturedSupplier.getEmail());
    }

    @Test
    public void shouldBeAbleToFindASupplierById(){
        UUID id = UUID.randomUUID();

        Supplier supplier = supplierMock.mockEntity(id);

        when(supplierRepository.findById(id.toString())).thenReturn(Optional.of(supplier));

        SupplierResponseDTO result = supplierService.findSupplierById(id.toString());

        assertNotNull(result);
        assertEquals("Tech Solutions LLC", result.name());
        assertEquals("Tech Solutions LLC", result.name());
        assertEquals("55-6677889", result.taxId());
        assertEquals("+1 415 555-6543", result.phone());
        assertEquals("info@techsolutions.com", result.email());
    }

    @Test
    public void shouldBeAbleToUpdateASupplier(){
        UUID id = UUID.randomUUID();

        Supplier supplier = supplierMock.mockEntity(id);
        SupplierRequestDTO supplierRequestDTO = supplierMock.mockDTO();

        when(supplierRepository.findById(id.toString())).thenReturn(Optional.of(supplier));
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        SupplierResponseDTO result = supplierService.updateSupplier(id.toString(), supplierRequestDTO);

        assertNotNull(result);
        assertEquals("Tech Solutions LLC", result.name());
        assertEquals("Tech Solutions LLC", result.name());
        assertEquals("55-6677889", result.taxId());
        assertEquals("+1 415 555-6543", result.phone());
        assertEquals("info@techsolutions.com", result.email());
    }

    @Test
    public void shouldBeAbleToDeleteASupplier(){
        UUID id = UUID.randomUUID();

        Supplier supplier = supplierMock.mockEntity(id);

        when(supplierRepository.findById(id.toString())).thenReturn(Optional.of(supplier));

        this.supplierService.deleteSupplier(id.toString());
    }

}
