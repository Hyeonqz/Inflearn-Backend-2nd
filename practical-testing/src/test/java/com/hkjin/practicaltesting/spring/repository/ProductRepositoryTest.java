package com.hkjin.practicaltesting.spring.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.hkjin.practicaltesting.spring.domian.product.Product;
import com.hkjin.practicaltesting.spring.domian.product.ProductRepository;
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
		assertThat(allBySellingTypeIn).hasSize(2)
			.extracting("productNumber","name","sellingType")// 원하는 컬럼만 추출
			.containsExactlyInAnyOrder(
				Tuple.tuple("001","아메리카노",ProductSellingType.SELLING),
				Tuple.tuple("002","크룽지",ProductSellingType.HOLD)
			);
	}

	@Test
	@DisplayName("상품번호 리스트로 상품들을 조회한다.")
	void findAllByProductNumberIn() {
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
		List<Product> productNumberIn = productRepository.findAllByProductNumberIn(List.of("001","002"));

		// then
		assertThat(productNumberIn).hasSize(2)
			.extracting("productNumber","name","sellingType")
			.containsExactlyInAnyOrder(
				tuple("001","아메리카노",ProductSellingType.SELLING),
				tuple("002","크룽지",ProductSellingType.HOLD)
			);
	}

	@Test
	@DisplayName("가장 마지막으로 저장한 상품의 상품번호를 가져온다")
	void findLastestProductNumber() {
	    // given
		String targetNumber = "003";
		Product product = createProduct("001",ProductType.HANDMADE,ProductSellingType.SELLING,4000,"아메리카노");
		Product product1 = createProduct("002",ProductType.HANDMADE,ProductSellingType.HOLD,5000,"카페라떼");
		Product product2 = createProduct(targetNumber,ProductType.HANDMADE,ProductSellingType.STOP_SELLING,6000,"오곡라떼");
		productRepository.saveAll(List.of(product,product1,product2));

		// when
		String lastestProductNumber = productRepository.findLastestProductNumber();

		// then
		assertThat(lastestProductNumber).isEqualTo(targetNumber);
	}

	@Test
	@DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어올 때, 상품이 하나도 없는 경우 null 반환")
	void findLastestProductNumberNull() {
	    // given

	    // when
		String lastestProductNumber = productRepository.findLastestProductNumber();

		// then
		assertThat(lastestProductNumber).isNull();
	}

	private Product createProduct(String productNumber, ProductType type, ProductSellingType sellingType, int price, String name) {
		return Product.builder()
			.productNumber(productNumber)
			.type(type)
			.sellingType(sellingType)
			.price(price)
			.name(name)
			.build();
	}

}