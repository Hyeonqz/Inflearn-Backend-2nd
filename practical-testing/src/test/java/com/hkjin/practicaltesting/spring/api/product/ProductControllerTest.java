package com.hkjin.practicaltesting.spring.api.product;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkjin.practicaltesting.spring.IntegrationControllerSupport;
import com.hkjin.practicaltesting.spring.api.product.dtos.request.ProductCreateRequest;
import com.hkjin.practicaltesting.spring.api.product.dtos.response.ProductResponse;import com.hkjin.practicaltesting.spring.domian.product.ProductSellingType;
import com.hkjin.practicaltesting.spring.domian.product.ProductType;
import com.hkjin.practicaltesting.spring.service.product.ProductService;import com.jayway.jsonpath.JsonPath;

class ProductControllerTest extends IntegrationControllerSupport {

	@Test
	@DisplayName("판매 상품을 조회한다")
	void getSellingProducts() throws Exception {
		// given
		List<ProductResponse> result = List.of();
		Mockito.when(productService.getSellingProducts()).thenReturn(result);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/selling")
		)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("200"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
		;

	}

	@Test
	@DisplayName("신규 상품 등록시 상품 타입은 필수값이다.")
	void createProductWithoutType() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.sellingType(ProductSellingType.SELLING)
			.name("아메리카노")
			.price(4000)
			.build();

		// when & then
		// http body 에 값을 넣다보면 직렬화, 역직렬화 과정을 거치게 된다.
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new") // perform -> api 수행
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(MockMvcResultHandlers.print()) // 요청 log 출력
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("상품 타입은 필수입니다."))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
		;
	}

	@Test
	@DisplayName("신규 상품 등록시 상품 가격은 필수값이다.")
	void createProductWithZeroType() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(ProductType.HANDMADE)
			.sellingType(ProductSellingType.SELLING)
			.name("아메리카노")
			.price(-4000)
			.build();

		// when & then
		// http body 에 값을 넣다보면 직렬화, 역직렬화 과정을 거치게 된다.
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new") // perform -> api 수행
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(MockMvcResultHandlers.print()) // 요청 log 출력
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("상품 가격은 양수여야 합니다."))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
		;
	}

	@Test
	@DisplayName("신규 상품 등록시 상품 판매상태는 필수값이다.")
	void createProductWithoutSellingType() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(ProductType.HANDMADE)
			.name("아메리카노")
			.price(4000)
			.build();

		// when & then
		// http body 에 값을 넣다보면 직렬화, 역직렬화 과정을 거치게 된다.
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new") // perform -> api 수행
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(MockMvcResultHandlers.print()) // 요청 log 출력
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("상품 판매상태는 필수입니다."))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
		;
	}

	@Test
	@DisplayName("신규 상품을 등록한다")
	void createProduct() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(ProductType.HANDMADE)
			.sellingType(ProductSellingType.SELLING)
			.name("아메리카노")
			.price(4000)
			.build();

		// when & then
		// http body 에 값을 넣다보면 직렬화, 역직렬화 과정을 거치게 된다.
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new") // perform -> api 수행
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(MockMvcResultHandlers.print()) // 요청 log 출력
			.andExpect(MockMvcResultMatchers.status().isOk());
	}

}