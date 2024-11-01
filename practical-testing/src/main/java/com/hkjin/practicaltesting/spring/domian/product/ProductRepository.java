package com.hkjin.practicaltesting.spring.domian.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findAllBySellingTypeIn(List<ProductSellingType> sellingTypes);
	List<Product> findAllByProductNumberIn(List<String> productNumbers);
}
