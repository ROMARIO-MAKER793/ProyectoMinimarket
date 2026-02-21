package com.minimarket.app.controlador;

import com.minimarket.app.entidad.Rol;
import com.minimarket.app.entidad.Usuario;
import com.minimarket.app.servicio.RolServicio;
import com.minimarket.app.servicio.UsuarioServicio;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("admin/usuarios")
public class UsuarioControlador {

	 @Autowired
	    private UsuarioServicio usuarioServicio;
	 
	 @Autowired
	 	private RolServicio rolServicio;
	 

	    // LISTAR
	 @GetMapping
	 public String listarUsuarios(Model model, HttpServletRequest request) {

	     String success = request.getParameter("success");

	     if (success != null) {
	         model.addAttribute("success", success);
	     }

	     model.addAttribute("usuarios", usuarioServicio.listarTodos());
	     model.addAttribute("roles", rolServicio.listarTodos());
	     model.addAttribute("content", "admin/users");

	     return "layaout/admin_layaout";
	 }

	   
	    //GUARDAR Y EDITAR

	    @PostMapping("/guardar")
	    public String guardarUsuario(
	            @RequestParam(required = false) Long id,
	            @RequestParam String usuario,
	            @RequestParam String contrasena,
	            @RequestParam Boolean habilitado,
	            @RequestParam("rolId") Long rolId, //este no es un error, solo advertencia
	            @RequestParam("confirmarContrasena") String confirmar) {

	        Usuario nuevo = new Usuario();

	        nuevo.setId(id);
	        nuevo.setUsuario(usuario);
	        nuevo.setHabilitado(habilitado);
	        nuevo.setContrasena(contrasena);

	        Rol rol = rolServicio.buscarPorId(rolId);
	        nuevo.setRoles(Set.of(rol));

	        usuarioServicio.guardar(nuevo);
	        
	        if(id == null) {
	        	
	        	return "redirect:/admin/usuarios?success=Usuario creado correctamente";
	        }else {

	        return "redirect:/admin/usuarios?success=Usuario editado correctamente";
	        }
	    }
	  

	    // ELIMINAR
	    @GetMapping("/eliminar/{id}")
	    public String eliminarUsuario(@PathVariable Long id) {
	        usuarioServicio.eliminar(id);
	        return "redirect:/admin/usuarios?success=Usuario eliminado correctamente";
	    }
}