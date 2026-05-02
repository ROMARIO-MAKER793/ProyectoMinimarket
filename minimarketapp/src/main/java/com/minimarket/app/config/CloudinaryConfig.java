package com.minimarket.app.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

	@Bean
	public Cloudinary cloudinary() {
		Map<String,String> config = new HashMap<>();
		
		config.put("cloud_name", "dt61zf586");
		config.put("api_key", "292826847819869");
		config.put("api_secret", "C_ytJEoHhkTDTL1rxh4E_7Sd2GM");
		
		return new Cloudinary(config);
	}
}
