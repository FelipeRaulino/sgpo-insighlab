package com.insightlab.sgpo.domain.supplier.exceptions;

public class SupplierAlreadyExistsException extends RuntimeException {

    public SupplierAlreadyExistsException(String message){
        super(message);
    }

}
