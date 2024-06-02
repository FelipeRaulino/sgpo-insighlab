package com.insightlab.sgpo.data.dtos.v1.supplier;

import java.time.LocalDateTime;

public record SupplierResponseDTO(String id, String name, String taxId, String phone, String email, Boolean status, LocalDateTime createdAt) {
}
