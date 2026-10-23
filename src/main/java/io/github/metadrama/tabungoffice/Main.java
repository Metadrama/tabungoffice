package io.github.metadrama.tabungoffice;

import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @SpringBootApplication also counts as @Configuration, so this class can do
// the web wiring too instead of needing a separate WebConfig class
@SpringBootApplication
public class Main implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	// the page files live in frontend/ at the project root, not inside
	// src/main/resources - Spring just hands them to the browser here
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String dir = Paths.get("frontend").toAbsolutePath().toUri().toString();
		registry.addResourceHandler("/**").addResourceLocations(dir,"classpath:/static/");
	}

	// so localhost:8080 opens the page instead of a 404
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/index.html");
	}

	// needed for the case where index.html is opened straight from disk as a
	// file, because a file page counts as a different origin
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("http://localhost:8080", "null")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
	}
}