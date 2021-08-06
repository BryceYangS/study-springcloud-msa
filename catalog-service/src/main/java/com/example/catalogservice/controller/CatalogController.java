package com.example.catalogservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.catalogservice.domain.CatalogEntity;
import com.example.catalogservice.service.CatalogSerivce;
import com.example.catalogservice.vo.ResponseCatalog;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/catalog-service")
@RequiredArgsConstructor
public class CatalogController {

	private final Environment env;
	private final CatalogSerivce catalogSerivce;
	private final ModelMapper mapper;

	@GetMapping("/health_check")
	public String status() {
		return String.format("It's Working in Catalog Service on PORT %s", env.getProperty("local.server.port"));
	}

	@GetMapping("/catalogs")
	public ResponseEntity<List<ResponseCatalog>> getUsers() {
		Iterable<CatalogEntity> catalogs = catalogSerivce.getAllCatalogs();

		List<ResponseCatalog> result = new ArrayList<>();
		catalogs.forEach(v -> {
			result.add(mapper.map(v, ResponseCatalog.class));
		});
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

}
