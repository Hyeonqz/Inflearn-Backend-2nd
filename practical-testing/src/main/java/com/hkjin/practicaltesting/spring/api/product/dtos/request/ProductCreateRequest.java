package com.hkjin.practicaltesting.spring.api.product.dtos.request;

import com.hkjin.practicaltesting.spring.domian.product.Product;
import com.hkjin.practicaltesting.spring.domian.product.ProductSellingType;
import com.hkjin.practicaltesting.spring.domian.product.ProductType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record ProductCreateRequest(
	String productNumber,

	@NotNull(message = "상품 타입은 필수입니다.") // client 에 보낼 메시지를 적어준다.
	ProductType type,

	@NotNull(message = "상품 판매상태는 필수입니다.") // enum 같은 객체 또는 type 일 경우
	ProductSellingType sellingType,

	@NotBlank(message = "상품 이름은 필수입니다.") // String 일 경우
	String name,

	@Positive(message = "상품 가격은 양수여야 합니다.") // 숫자 일 경우
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
