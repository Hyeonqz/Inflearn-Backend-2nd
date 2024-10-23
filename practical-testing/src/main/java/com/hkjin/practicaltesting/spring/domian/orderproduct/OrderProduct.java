package com.hkjin.practicaltesting.spring.domian.orderproduct;

import com.hkjin.practicaltesting.spring.domian.order.Order;
import com.hkjin.practicaltesting.spring.domian.product.BaseEntity;
import com.hkjin.practicaltesting.spring.domian.product.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class OrderProduct extends BaseEntity {
	/*
	* 다대다 관계를 가지므로 위 관계를 1대다, 다대1 로 풀어내기 위한 중간 객체
	* */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

	public OrderProduct (Order order, Product product) {
		this.order = order;
		this.product = product;
	}

}
