package com.hkjin.practicaltesting.unit.beverate;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AmericanoTest {

	private Americano americano;

	@BeforeEach
	public void setUp() {
		americano = new Americano();
	}

	@AfterEach
	public void afterUp() {
		americano = null;
	}

	@Test
	@DisplayName("이름에 따른 가격 Equals 테스트")
	void getPrice() {
		Assertions.assertEquals(americano.getName(), "아메리카노");
	}
	
	@Test
	@DisplayName("가격에 따른 이름 Equals 테스트")
	void getName() {
		assertThat(americano.getPrice()).isEqualTo(4500);
	}

}