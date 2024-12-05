package com.hkjin.practicaltesting.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkjin.practicaltesting.spring.api.order.OrderController;
import com.hkjin.practicaltesting.spring.api.product.ProductController;
import com.hkjin.practicaltesting.spring.service.product.ProductService;

@WebMvcTest(controllers = {
	OrderController.class, ProductController.class
})
public abstract class IntegrationControllerSupport {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;
	/*
	 * mockito 라이브러리의 어노테이션
	 * 컨테이너에 Mockito 로 만든 Bean 을 넣어주는 역할을 한다.
	 * */
	@MockBean
	protected ProductService productService;
}
