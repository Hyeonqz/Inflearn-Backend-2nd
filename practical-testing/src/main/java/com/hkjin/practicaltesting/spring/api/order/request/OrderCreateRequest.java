package com.hkjin.practicaltesting.spring.api.order.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderCreateRequest {

	@NotEmpty
	private List<String> productNumbers;

	@Builder
	public OrderCreateRequest (List<String> productNumbers) {
		this.productNumbers = productNumbers;
	}

}
