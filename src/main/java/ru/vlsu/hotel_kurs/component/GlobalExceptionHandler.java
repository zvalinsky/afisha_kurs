package ru.vlsu.hotel_kurs.component;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBindException(BindException ex, Model model) {

        for (FieldError fieldError : ex.getFieldErrors()) {
            System.out.println("Field: " + fieldError.getField());
            System.out.println("Message: " + fieldError.getDefaultMessage());
        }

        return "redirect:/batches";
    }
}
