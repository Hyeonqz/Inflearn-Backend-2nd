package com.hkjin.practicaltesting.spring.api.order.request;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderCreateRequest {
	private List<String> productNumbers;

	@Builder
	public OrderCreateRequest (List<String> productNumbers) {
		this.productNumbers = productNumbers;
	}

}