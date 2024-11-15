package com.hkjin.practicaltesting.spring.domian.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hkjin.practicaltesting.spring.domian.orderproduct.OrderProduct;
import com.hkjin.practicaltesting.spring.domian.product.BaseEntity;
import com.hkjin.practicaltesting.spring.domian.product.Product;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name="orders")
@Entity
public class Order extends BaseEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	private int totalPrice;

	private LocalDateTime registeredDateTime;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderProduct> orderProducts = new ArrayList<>();

	@Builder
	public Order (List<Product> products, OrderStatus orderStatus, LocalDateTime registeredDateTime) {
		this.orderStatus = orderStatus;
		this.totalPrice = calculatorTotalPrice(products);
		this.registeredDateTime = registeredDateTime;
		this.orderProducts = orderProducts(products);
	}

	public static Order create (List<Product> productNumberIn, LocalDateTime registeredDateTime) {
		return Order.builder()
			.orderStatus(OrderStatus.INIT)
			.products(productNumberIn)
			.registeredDateTime(registeredDateTime)
			.build();
	}

	private int calculatorTotalPrice(List<Product> products) {
		return products.stream().mapToInt(Product::getPrice).sum();
	}

	private List<OrderProduct> orderProducts(List<Product> products) {
		return products.stream()
			.map(product -> new OrderProduct(this, product))
			.toList();
	}

}
