package com.example.orderservice.domain.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.orderservice.domain.OrderEntity;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
	OrderEntity findByOrderId(String orderId);
	Iterable<OrderEntity> findByUserId(String userId);
}
