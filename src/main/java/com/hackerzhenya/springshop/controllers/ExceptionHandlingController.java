package com.hackerzhenya.springshop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlingController {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody ExceptionResult
    handleError(HttpServletRequest request, Exception exception) {
        var message = exception.getMessage();

        if (message == null) {
            message = exception.getClass().getSimpleName();
        }

        exception.printStackTrace();
        return new ExceptionResult(message);
    }

    public static class ExceptionResult {
        private final String error;

        public ExceptionResult(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}
