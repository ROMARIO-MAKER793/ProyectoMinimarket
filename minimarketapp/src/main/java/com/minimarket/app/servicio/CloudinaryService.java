package com.minimarket.app.servicio;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService implements ImagenService {
	
	//Inyeccion por constructor
	
	private final Cloudinary cloudinary;
	
	
	public CloudinaryService(Cloudinary cloudinary) {
		super();
		this.cloudinary = cloudinary;
	}


	@Override
	public String subirImagen(MultipartFile file) {
		try {
		Map<?,?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "minimarket/productos"));
		return result.get("url").toString();
		}catch(IOException e) {
			throw new RuntimeException("Error al subir imagen a cloudinary");
		}
		
		
	} // fin del metodo subirImagen
	
	
	

}
