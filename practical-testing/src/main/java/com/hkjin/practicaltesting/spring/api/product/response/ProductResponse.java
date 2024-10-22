package com.hkjin.practicaltesting.spring.api.product.response;

import com.hkjin.practicaltesting.spring.domian.product.Product;
import com.hkjin.practicaltesting.spring.domian.product.ProductSellingType;
import com.hkjin.practicaltesting.spring.domian.product.ProductType;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponse {
	private Long id;
	private String productNumber;
	private ProductType type;
	private ProductSellingType sellingType;
	private String name;
	private Integer price;

	@Builder
	private ProductResponse (Long id, String productNumber, ProductType type, ProductSellingType sellingType,
		String name,
		Integer price) {
		this.id = id;
		this.productNumber = productNumber;
		this.type = type;
		this.sellingType = sellingType;
		this.name = name;
		this.price = price;
	}

	public static ProductResponse of (Product product) {
		return ProductResponse.builder()
			.id(product.getId())
			.productNumber(product.getProductNumber())
			.type(product.getType())
			.sellingType(product.getSellingType())
			.name(product.getName())
			.price(product.getPrice())
			.build();
	}

}
