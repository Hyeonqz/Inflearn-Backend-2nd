package com.hkjin.practicaltesting.spring.domian.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findAllBySellingTypeIn(List<ProductSellingType> sellingTypes);
	List<Product> findAllByProductNumberIn(List<String> productNumbers);

	// Native Query -> JPQL
	@Query(value = "select p.product_number from product p order by id desc limit 1", nativeQuery = true)
	String findLastestProductNumber ();

}
