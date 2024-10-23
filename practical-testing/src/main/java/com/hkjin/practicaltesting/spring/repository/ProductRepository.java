package com.hkjin.practicaltesting.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hkjin.practicaltesting.spring.domian.product.Product;
import com.hkjin.practicaltesting.spring.domian.product.ProductSellingType;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findAllBySellingTypeIn(List<ProductSellingType> sellingTypes);
	List<Product> findAllByProductNumberIn(List<String> productNumbers);
}
