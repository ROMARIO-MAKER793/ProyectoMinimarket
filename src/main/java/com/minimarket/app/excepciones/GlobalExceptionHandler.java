package com.minimarket.app.excepciones;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Manejador genérico para cualquier error interno del servidor (Error 500)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(Exception ex, HttpServletRequest request) {
        
        // Si el error proviene de nuestra API fetch (el POS), dejamos que devuelva el JSON original
        if ("application/json".equals(request.getHeader("Content-Type"))) {
            return null; 
        }

        // Si es navegación web, mostramos una pantalla estilizada
        ModelAndView mav = new ModelAndView();
        mav.addObject("mensajeError", "Ha ocurrido un error inesperado en el sistema.");
        mav.addObject("detalle", ex.getMessage());
        mav.setViewName("error/generico"); // Apunta a una vista que crearemos
        return mav;
    }

    // Manejador específico para Páginas No Encontradas (Error 404)
    @ExceptionHandler(NoResourceFoundException.class)
    public ModelAndView handleNotFound(NoResourceFoundException ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("mensajeError", "La página o recurso que buscas no existe.");
        mav.setViewName("error/generico");
        return mav;
    }
}