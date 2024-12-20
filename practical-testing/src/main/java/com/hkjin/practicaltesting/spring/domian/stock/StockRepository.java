package com.hkjin.practicaltesting.spring.domian.stock;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
	List<Stock> findByProductNumberIn(List<String> productNumbers);
}

