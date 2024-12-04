package com.hkjin.practicaltesting.spring.domian.product;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class ProductTypeTest {

	@Test
	@DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
	void containsStockType() {
	    // given
		ProductType givenType = ProductType.HANDMADE;

		// when
		boolean result = ProductType.containsStockType(givenType);

		// then
		Assertions.assertThat(result).isFalse();
	}

	@CsvSource({"HANDMADE, false","BOTTLE,true","BAKERY,true"})
	@ParameterizedTest
	void ProductTypeParameterized(ProductType productType, boolean expected) {
		// when
		boolean result = ProductType.containsStockType(productType);

		// then
		Assertions.assertThat(result).isEqualTo(expected);
	}

	private static Stream<Arguments> ProductTypeParameterized() {
		return Stream.of(
			Arguments.of(ProductType.HANDMADE, false),
			Arguments.of(ProductType.BOTTLE, true),
			Arguments.of(ProductType.BAKERY, true)
		);
	}


	@MethodSource("ProductTypeParameterized")
	@ParameterizedTest
	void ProductTypeTest(ProductType productType, boolean expected) {
	    // when
		boolean result = ProductType.containsStockType(productType);

	    // then
		Assertions.assertThat(result).isEqualTo(expected);
	}

}