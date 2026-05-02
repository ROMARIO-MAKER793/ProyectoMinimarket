package com.minimarket.app.servicio;

import org.springframework.web.multipart.MultipartFile;

public interface ImagenService {
	
	String subirImagen(MultipartFile file);

}
