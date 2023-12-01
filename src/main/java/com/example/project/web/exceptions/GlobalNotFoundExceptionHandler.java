package com.example.project.web.exceptions;

import com.example.project.model.exceptions.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class GlobalNotFoundExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public String handleNotFound(ObjectNotFoundException exception, Model model) {

       model.addAttribute("message",exception.getMessage());
       model.addAttribute("error_code","404");
        return "notfoud-global";
    }

}