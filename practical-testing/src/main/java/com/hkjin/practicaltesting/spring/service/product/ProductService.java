package com.hkjin.practicaltesting.spring.service.product;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hkjin.practicaltesting.spring.api.product.dtos.request.ProductCreateRequest;
import com.hkjin.practicaltesting.spring.api.product.dtos.response.ProductResponse;
import com.hkjin.practicaltesting.spring.domian.product.Product;
import com.hkjin.practicaltesting.spring.domian.product.ProductSellingType;
import com.hkjin.practicaltesting.spring.domian.product.ProductRepository;
import com.hkjin.practicaltesting.spring.domian.product.ProductType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductService {
	private final ProductRepository productRepository;

	/* 동시성 이슈가 발생할 수 있음 */
	// count 가 아닌 UUID 로 하면 동시성 이슈에 벗어날 수 있음
	@Transactional
	public ProductResponse createProduct (ProductCreateRequest request) {
		String nextProductNumber = createNextProductNumber();

		Product product = request.toEntity(nextProductNumber);
		Product savedProduct = productRepository.save(product);

		productRepository.save(savedProduct);

		return ProductResponse.of(savedProduct);
	}

	private String createNextProductNumber () {
		String lastestProductNumber = productRepository.findLastestProductNumber();
		if(lastestProductNumber == null)
			return "001";

		int latestProductNumberOriginal = Integer.parseInt(lastestProductNumber);
		int nextProductNumber = latestProductNumberOriginal+1;

		// 9 -> 009, 10 -> 010 세자리수로 만든다.
		return String.format("%03d", nextProductNumber);
	}

	public List<ProductResponse> getSellingProducts() {
		List<Product> products = productRepository.findAllBySellingTypeIn(ProductSellingType.forDisplay());

		return products.stream()
			.map(ProductResponse::of)
			.toList();
	}

}
