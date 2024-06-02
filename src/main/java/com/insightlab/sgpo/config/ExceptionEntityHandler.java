package com.insightlab.sgpo.config;

import com.insightlab.sgpo.data.dtos.v1.general.ErrorMessageDTO;
import com.insightlab.sgpo.domain.supplier.exceptions.SupplierAlreadyExistsException;
import com.insightlab.sgpo.domain.supplier.exceptions.SupplierNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionEntityHandler {

    @ExceptionHandler(SupplierAlreadyExistsException.class)
    public ResponseEntity<?> handleOnSupplierAlreadyExists(SupplierAlreadyExistsException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessageDTO(exception.getMessage()));
    }

    @ExceptionHandler(SupplierNotFoundException.class)
    public ResponseEntity<?> handleOnSupplierNotFound(SupplierNotFoundException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessageDTO(exception.getMessage()));
    }

}
