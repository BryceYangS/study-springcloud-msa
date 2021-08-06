package com.example.orderservice.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.orderservice.domain.OrderEntity;
import com.example.orderservice.domain.repository.OrderRepository;
import com.example.orderservice.dto.OrderDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final ModelMapper mapper;

	public OrderDto createOrder(OrderDto orderDetails) {
		orderDetails.setOrderId(UUID.randomUUID().toString());
		orderDetails.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());

		OrderEntity order = mapper.map(orderDetails, OrderEntity.class);
		orderRepository.save(order);

		return mapper.map(order, OrderDto.class);
	}

	public OrderDto getOrderByOrderId(String orderId) {
		OrderEntity order = orderRepository.findByOrderId(orderId);
		return mapper.map(order, OrderDto.class);
	}

	public Iterable<OrderEntity> getOrdersByUserId(String userId) {
		return orderRepository.findByUserId(userId);
	}
}
