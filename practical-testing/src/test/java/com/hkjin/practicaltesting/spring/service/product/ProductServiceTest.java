package com.hkjin.practicaltesting.spring.service.product;

import static com.hkjin.practicaltesting.spring.domian.product.ProductSellingType.*;
import static com.hkjin.practicaltesting.spring.domian.product.ProductType.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.hkjin.practicaltesting.spring.api.product.dtos.request.ProductCreateRequest;
import com.hkjin.practicaltesting.spring.api.product.dtos.response.ProductResponse;
import com.hkjin.practicaltesting.spring.domian.product.Product;
import com.hkjin.practicaltesting.spring.domian.product.ProductRepository;
import com.hkjin.practicaltesting.spring.domian.product.ProductSellingType;
import com.hkjin.practicaltesting.spring.domian.product.ProductType;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@AfterEach
	void tearDown () {
		productRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("신규상품을 등록한다, 상품 번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
	void createProduct () {
		// given
		Product product = createProduct("001", HANDMADE, SELLING, 4000, "아메리카노");
		ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
			.type(HANDMADE)
			.sellingType(SELLING)
			.name("카페라떼")
			.price(5000)
			.build();

		// when
		ProductResponse productResponse = productService.createProduct(productCreateRequest);

		// then
		List<Product> products = productRepository.findAll();
		Assertions.assertThat(productResponse)
			.extracting("productNumber", "type", "sellingType", "name", "price")
			.containsExactlyInAnyOrder(
				Tuple.tuple("001", HANDMADE, SELLING, "아메리카노", 4000),
				Tuple.tuple("002",HANDMADE,SELLING,"카페라떼",5000)
			);

	}

	@Test
	@DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 001이다.")
	void ProductServiceTest () {
		// given
		ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
			.type(HANDMADE)
			.sellingType(SELLING)
			.name("카푸치노")
			.price(5000)
			.build();

		// when
		ProductResponse productResponse = productService.createProduct(productCreateRequest);

		// then
		Assertions.assertThat(productResponse.getProductNumber()).isEqualTo("001");
	}

	private Product createProduct (String productNumber, ProductType type, ProductSellingType sellingType, int price,
		String name) {
		return Product.builder()
			.productNumber(productNumber)
			.type(type)
			.sellingType(sellingType)
			.price(price)
			.name(name)
			.build();
	}

}