package com.example.catalogservice.service;

import org.springframework.stereotype.Service;

import com.example.catalogservice.domain.CatalogEntity;
import com.example.catalogservice.domain.repository.CatalogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatalogSerivce {
	private final CatalogRepository catalogRepository;

	public Iterable<CatalogEntity> getAllCatalogs() {
		return catalogRepository.findAll();
	}

}
