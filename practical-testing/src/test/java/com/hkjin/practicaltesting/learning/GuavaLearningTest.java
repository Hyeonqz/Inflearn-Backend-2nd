package com.hkjin.practicaltesting.learning;

import static org.assertj.core.api.Assertions.*;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

class GuavaLearningTest {

	@Test
	@DisplayName("Guava 를 사용한 List 를 파티셔닝 한다.")
	void partitionLearningTest() {
		// given
		List<Integer> integers = List.of(1, 2, 3, 4, 5, 6);

		// when
		List<List<Integer>> partition = Lists.partition(integers, 3);

		// then
		assertThat(partition).hasSize(2)
			.isEqualTo(List.of(
				List.of(1,2,3), List.of(4,5,6)
			));
	}

	@Test
	@DisplayName("Guava 를 사용한 List 를 파티셔닝 한다.")
	void partitionLearningTest2() {
		// given
		List<Integer> integers = List.of(1, 2, 3, 4, 5, 6);

		// when
		List<List<Integer>> partition = Lists.partition(integers, 4);

		// then
		assertThat(partition).hasSize(2)
			.isEqualTo(List.of(
				List.of(1,2,3,4), List.of(5,6)
			));
	}

	@Test
	@DisplayName("Guava MultiMap 사용")
	void multiMapLearningTest() {
	    // given
		// 한개의 key 에 여러개의 value 를 넣을 수 있다.
		Multimap<String,String> multiMap = ArrayListMultimap.create();
		multiMap.put("커피","아메리카노");
		multiMap.put("커피","카페라떼");
		multiMap.put("커피","카푸치노");
		multiMap.put("베이커리","소보로빵");
		multiMap.put("베이커리","크룽지");

	    // when
		Collection<String> collection = multiMap.get("커피");

		// then
		assertThat(collection).hasSize(3)
			.isEqualTo(List.of("아메리카노","카페라떼","카푸치노"));
	}

	@TestFactory
	@DisplayName("멀티맵 기능 확인")
	Collection<DynamicTest> GuavaLearningTest() {
	    // given
		Multimap<String,String> multiMap = ArrayListMultimap.create();
		multiMap.put("커피","아메리카노");
		multiMap.put("커피","카페라떼");
		multiMap.put("커피","카푸치노");
		multiMap.put("베이커리","소보로빵");
		multiMap.put("베이커리","크룽지");

		return List.of(
			DynamicTest.dynamicTest("1개 value 삭제", () -> {
				// when
				multiMap.remove("커피","카푸치노");

				// then
				Collection<String> collection = multiMap.get("커피");

				assertThat(collection).hasSize(2)
					.isEqualTo(List.of("아메리카노","카페라떼"));
			}),
			DynamicTest.dynamicTest("key 삭제", () -> {
				// when
				multiMap.removeAll("커피");

				// then
				Collection<String> collection = multiMap.get("커피");

				assertThat(collection).isEmpty();
			})
		);
	}
}
