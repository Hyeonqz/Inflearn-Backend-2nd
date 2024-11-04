package com.hkjin.practicaltesting.spring.api.product;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hkjin.practicaltesting.spring.api.product.dtos.request.ProductCreateRequest;
import com.hkjin.practicaltesting.spring.api.product.dtos.response.ProductResponse;
import com.hkjin.practicaltesting.spring.service.product.ProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ProductController {
	private final ProductService productService;

	@GetMapping("/api/v1/products/selling")
	public List<ProductResponse> getSellingProducts() {
		return productService.getSellingProducts();
	}

	@PostMapping("/api/v1/products/new")
	public void createProduct(@RequestBody ProductCreateRequest request) {
		productService.createProduct(request);

	}


}
