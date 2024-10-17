package com.hkjin.practicaltesting.unit.kiosk;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.hkjin.practicaltesting.unit.order.Order;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.hkjin.practicaltesting.unit.beverate.Americano;
import com.hkjin.practicaltesting.unit.beverate.Beverage;
import com.hkjin.practicaltesting.unit.beverate.Latte;

class CafeKioskTest {

	// TDD
	@Test
	void calculatorTotalPrice () {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();
		Latte latte = new Latte();

		cafeKiosk.add(americano);
		cafeKiosk.add(latte);

		int totalPrice = cafeKiosk.caculatorTotalPrice();
		assertThat(totalPrice).isEqualTo(9500);
	}

	// test
	@Test
	@DisplayName("수동 더하기 테스트")
	void noAutomatic_add () {
		// given
		CafeKiosk cafeKiosk = new CafeKiosk();

		// when
		cafeKiosk.add(new Americano());

		// then
		System.out.println(">>> 담긴 음료 수 : " + cafeKiosk.getBeverages().size());
		System.out.println(">>> 담긴 음료 : " + cafeKiosk.getBeverages().getLast().getName());
	}

	@Test
	@DisplayName("자동화 더하기 테스트")
	void automatic_Add () {
		CafeKiosk cafeKiosk = new CafeKiosk();

		cafeKiosk.add(new Americano());

		assertThat(cafeKiosk.getBeverages().size()).isEqualTo(1);
		assertThat(cafeKiosk.getBeverages()).hasSize(1);
		assertThat(cafeKiosk.getBeverages().getFirst().getName()).isEqualTo("아메리카노");
	}

	@Test
	@DisplayName("자동화 더하기 테스트")
	void addSeveralBeverages () {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();

		cafeKiosk.add(americano, 2);

		assertThat(cafeKiosk.getBeverages().getFirst()).isEqualTo(americano);
		assertThat(cafeKiosk.getBeverages().get(1)).isEqualTo(americano);
		assertThat(cafeKiosk.getBeverages().getFirst().getName()).isEqualTo("아메리카노");
	}

	@Test
	@DisplayName("더하기 예외 테스트")
	void addSeveralZeroBeverages () {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();

		cafeKiosk.add(americano, 2);

		// 예외시
		assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("음료는 1잔 이상 주문하실 수 있습니다.");
	}

	@Test
	@DisplayName("삭제 테스트")
	void remove () {
		// given
		CafeKiosk cafeKiosk = new CafeKiosk();
		Beverage beverage = new Latte();

		cafeKiosk.add(beverage);
		assertThat(cafeKiosk.getBeverages()).hasSize(1);

		cafeKiosk.delete(beverage);
		assertThat(cafeKiosk.getBeverages().isEmpty());
	}

	@Test
	@DisplayName("전체 삭제 테스트")
	void all_Clear () {
		// given
		CafeKiosk cafeKiosk = new CafeKiosk();
		Beverage beverage = new Latte();
		Beverage beverage1 = new Americano();
		cafeKiosk.add(beverage);
		cafeKiosk.add(beverage1);

		// when
		cafeKiosk.clear();

		// then
		assertThat(cafeKiosk.getBeverages()).isEmpty();
	}

	@Test
	@DisplayName("주문시간이 맞는지 확인하는 테스트")
	void createOrderWithCurrentTime () {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();

		cafeKiosk.add(americano);

		Order order = cafeKiosk.createOrder(LocalDateTime.of(2024, 10, 16, 16, 0));

		assertThat(order.getBeverages()).hasSize(1);
		assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
	}

	@Test
	@DisplayName("영업시간이 아닐 때 예외 발생 테스트")
	void createOrderOutsideOpenTime () {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();

		cafeKiosk.add(americano);

		assertThatThrownBy(() -> cafeKiosk.createOrder(LocalDateTime.of(2024, 10, 16, 22, 1)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요")
		;

	}

}