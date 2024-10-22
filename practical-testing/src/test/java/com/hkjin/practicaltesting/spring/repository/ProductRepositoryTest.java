package com.hkjin.practicaltesting.spring.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.hkjin.practicaltesting.spring.domian.product.Product;
import com.hkjin.practicaltesting.spring.domian.product.ProductSellingType;
import com.hkjin.practicaltesting.spring.domian.product.ProductType;

@ActiveProfiles("test")
@DataJpaTest
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@Test
	@DisplayName("원하는 판매상태를 가진 상품들을 조회한디.")
	void ProductRepositoryTest() {
	    // given
		Product product = Product.builder()
			.productNumber("001")
			.type(ProductType.HANDMADE)
			.sellingType(ProductSellingType.SELLING)
			.price(4000)
			.name("아메리카노")
			.build();

		Product product1 = Product.builder()
			.productNumber("002")
			.type(ProductType.HANDMADE)
			.sellingType(ProductSellingType.HOLD)
			.price(4500)
			.name("크룽지")
			.build();

		Product product2 = Product.builder()
			.productNumber("003")
			.type(ProductType.HANDMADE)
			.sellingType(ProductSellingType.STOP_SELLING)
			.price(5000)
			.name("카페라떼")
			.build();
		productRepository.saveAll(List.of(product,product1,product2));

		// when
		List<Product> allBySellingTypeIn = productRepository.findAllBySellingTypeIn(
			List.of(ProductSellingType.SELLING, ProductSellingType.STOP_SELLING.HOLD));

		// then
		Assertions.assertThat(allBySellingTypeIn).hasSize(2)
			.extracting("productNumber","name","sellingType")// 원하는 컬럼만 추출
			.containsExactlyInAnyOrder(
				Tuple.tuple("001","아메리카노",ProductSellingType.SELLING),
				Tuple.tuple("002","크룽지",ProductSellingType.HOLD)
			);
	}
}