package com.minimarket.app.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ==========================================
    // 1. MANEJADOR PARA FALTA DE STOCK
    // ==========================================
    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<Map<String, String>> handleStockInsuficiente(StockInsuficienteException ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", ex.getMessage());
        // Devolvemos un status 400 (Bad Request) con el JSON: {"error": "mensaje..."}
        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
    }

    // ==========================================
    // 2. MANEJADOR GENÉRICO (Error 500)
    // ==========================================
    @ExceptionHandler(Exception.class)
    public Object handleAllExceptions(Exception ex, HttpServletRequest request) {
        
        // Si la petición viene de JS (Fetch/AJAX), devolvemos JSON para que no rompa la pantalla
        if ("application/json".equals(request.getHeader("Content-Type")) || 
           (request.getHeader("Accept") != null && request.getHeader("Accept").contains("application/json"))) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("error", "Error interno: " + ex.getMessage());
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Si es navegación web (URL normal), mostramos la pantalla estilizada HTML
        ModelAndView mav = new ModelAndView();
        mav.addObject("mensajeError", "Ha ocurrido un error inesperado en el sistema.");
        mav.addObject("detalle", ex.getMessage());
        mav.setViewName("error/generico"); 
        return mav;
    }

    // ==========================================
    // 3. MANEJADOR DE PÁGINA NO ENCONTRADA (Error 404)
    // ==========================================
    @ExceptionHandler(NoResourceFoundException.class)
    public ModelAndView handleNotFound(NoResourceFoundException ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("mensajeError", "La página o recurso que buscas no existe.");
        mav.setViewName("error/generico");
        return mav;
    }
}