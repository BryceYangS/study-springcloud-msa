package com.example.apigatewayservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
	public LoggingFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {

		GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			ServerHttpResponse response = exchange.getResponse();

			log.info("Logging PRE baseMessage: {}", config.getBaseMessage());

			if (config.isPreLogger()) {
				log.info("Logging Filter Start: request id -> {}", request.getId());
			}

			// Logging Post Filter
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				if (config.isPostLogger()) {
					log.info("Logging POST Filter: response code -> {}", response.getStatusCode());
				}
			}));
		}, Ordered.HIGHEST_PRECEDENCE); // 해당 필터가 가장 먼저 실행됨

		return filter;
	}

	@Data
	public static class Config {
		// Put the configuration properties
		private String baseMessage;
		private boolean preLogger;
		private boolean postLogger;
	}

	;

}
