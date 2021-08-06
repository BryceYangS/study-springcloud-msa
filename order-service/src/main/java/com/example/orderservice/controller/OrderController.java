package com.example.orderservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.orderservice.domain.OrderEntity;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
public class OrderController {

	private final OrderService orderService;
	private final Environment env;
	private final ModelMapper mapper;

	@GetMapping("/health_check")
	public String status() {
		return String.format("It's Working in Order Service on PORT %s", env.getProperty("local.server.port"));
	}

	@PostMapping("/{userId}/orders")
	public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,@RequestBody RequestOrder order) {

		OrderDto orderDto = mapper.map(order, OrderDto.class);
		orderDto.setUserId(userId);
		OrderDto createOrder = orderService.createOrder(orderDto);

		ResponseOrder responseOrder = mapper.map(createOrder, ResponseOrder.class);

		return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
	}

	@GetMapping("/{userId}/orders")
	public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId) {
		Iterable<OrderEntity> ordersByUserId = orderService.getOrdersByUserId(userId);

		List<ResponseOrder> responseOrders = new ArrayList<>();
		ordersByUserId.forEach(e -> {
			responseOrders.add(mapper.map(e, ResponseOrder.class));
		});

		return ResponseEntity.status(HttpStatus.OK).body(responseOrders);
	}
}
