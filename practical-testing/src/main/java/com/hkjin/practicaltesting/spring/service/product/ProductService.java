package com.hkjin.practicaltesting.spring.service.product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hkjin.practicaltesting.spring.api.product.response.ProductResponse;
import com.hkjin.practicaltesting.spring.domian.product.Product;
import com.hkjin.practicaltesting.spring.domian.product.ProductSellingType;
import com.hkjin.practicaltesting.spring.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {
	private final ProductRepository productRepository;

	public List<ProductResponse> getSellingProducts() {
		List<Product> products = productRepository.findAllBySellingTypeIn(ProductSellingType.forDisplay());

		return products.stream()
			.map(ProductResponse::of)
			.toList();
	}
}
