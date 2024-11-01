package com.hkjin.practicaltesting.spring.service.order;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hkjin.practicaltesting.spring.api.order.request.OrderCreateRequest;
import com.hkjin.practicaltesting.spring.api.order.response.OrderResponse;
import com.hkjin.practicaltesting.spring.domian.order.Order;
import com.hkjin.practicaltesting.spring.domian.order.OrderRepository;
import com.hkjin.practicaltesting.spring.domian.product.Product;
import com.hkjin.practicaltesting.spring.domian.product.ProductRepository;
import com.hkjin.practicaltesting.spring.domian.product.ProductType;
import com.hkjin.practicaltesting.spring.domian.stock.Stock;
import com.hkjin.practicaltesting.spring.domian.stock.StockRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final StockRepository stockRepository;

	/*
	* 재고 감소 -> 동시성 고민
	* 낙관적 락, 비괸적 락 사용하여 데이터에 대하여 락을 잡고 순차적으로 처리하게 끔 한다.
	* */
	@Transactional
	public OrderResponse createOrder (OrderCreateRequest orderCreateRequest, LocalDateTime registeredDateTime) {
		List<String> productNumbers = orderCreateRequest.getProductNumbers();
		List<Product> products = findProductsBy(productNumbers);

		deductStockQuantities(products);

		Order saved = orderRepository.save(Order.create(products, registeredDateTime));

		return OrderResponse.of(saved);
	}

	private void deductStockQuantities (List<Product> products) {
		// 재고 차감 체크가 필요한 상품들 Filter
		List<String> stockProductNumbers = extractedStockProductNumbers(products);

		// 재고 엔티티 조회
		Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);
		// 상품별 counting
		Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);

		// 재고 차감 시도
		for(String stockProductNumber : new HashSet<>(stockProductNumbers)) {
			Stock stock = stockMap.get(stockProductNumber);
			int quantity = productCountingMap.get(stockProductNumber).intValue();

			if(stock.isQuantityLessThan(quantity))
				throw new IllegalArgumentException("재고가 부족한 상품이 있습니다"); // 디버그 찍을시 throw 에 찍는게 유리함

			stock.deductQuantity(quantity);
		}
	}

	private List<String> extractedStockProductNumbers (List<Product> products) {
		return products.stream()
			.filter(product -> ProductType.containsStockType(product.getType()))
			.map(Product::getProductNumber)
			.toList();
	}

	private Map<String, Stock> createStockMapBy (List<String> stockProductNumbers) {
		List<Stock> stocks = stockRepository.findByProductNumberIn(stockProductNumbers);
		Map<String, Stock> stockMap = stocks.stream()
			.collect(Collectors.toMap(Stock::getProductNumber, stock -> stock));
		return stockMap;
	}

	private Map<String, Long> createCountingMapBy (List<String> stockProductNumbers) {
		Map<String, Long> productCountingMap = stockProductNumbers.stream()
			.collect(Collectors.groupingBy(productNumber -> productNumber, Collectors.counting()));
		return productCountingMap;
	}

	private List<Product> findProductsBy (List<String> productNumbers) {
		List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

		Map<String, Product> productMap = products.stream()
			.collect(Collectors.toMap(Product::getProductNumber, p -> p));

		return productNumbers.stream()
			.map(productMap::get)
			.toList();
	}

}
