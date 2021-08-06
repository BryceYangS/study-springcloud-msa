package com.example.catalogservice.domain.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.catalogservice.domain.CatalogEntity;

public interface CatalogRepository extends CrudRepository<CatalogEntity, Long> {
	CatalogEntity findByProductId(String productId);
}
