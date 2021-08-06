package com.example.catalogservice.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCatalog {
	private String productId;
	private Integer productName;
	private Integer unitPrice;
	private Integer stock;
	private Date createdAt;
}
