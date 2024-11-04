package com.hkjin.practicaltesting.spring.api.product.dtos.request;

import com.hkjin.practicaltesting.spring.domian.product.Product;
import com.hkjin.practicaltesting.spring.domian.product.ProductSellingType;
import com.hkjin.practicaltesting.spring.domian.product.ProductType;

import lombok.Builder;

@Builder
public record ProductCreateRequest(
	String productNumber,
	ProductType type,
	ProductSellingType sellingType,
	String name,
	int price
) {

	public Product toEntity (String nextProductNumber) {
		return Product.builder()
			.productNumber(nextProductNumber)
			.name(name)
			.type(type)
			.sellingType(sellingType)
			.price(price)
			.build();
	}

}
