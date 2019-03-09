package com.sha.fileuploader;

import com.sha.fileuploader.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class FileUploaderApplication {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*").allowedMethods("OPTIONS", "PUT", "DELETE", "GET",
						"POST").allowedHeaders("X-requested-with",
						"Content-Type", "Origin","Content-Range","Range",
						"authorization",
						"x-upload-content-length",
						"x-upload-content-type",
						"location");
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(FileUploaderApplication.class, args);
	}

}
