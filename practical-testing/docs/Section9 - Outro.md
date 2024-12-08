# Section9 - Outro
## 학습테스트
- 잘 모르는 기능, 라이브러리, 프레임워크를 학습하기 위해 작성하는 테스트
- 여러 테스트 케이스를 스스로 정의하고 검증하는 과정을 통해 보다 구체적인 동작과 기능을 학습할 수 있다
- 관련 문서만 읽는 것보다 훨씬 재미있게 학습할 수 있다.

위 오픈소스를 사용해보자
> implementation("com.google.guava:guava:33.3.1-jre")

```java
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

```

팀에 대한 Guava 를 알려주고 싶다면 위에 대한 테스트 내용을 담겨두면 좋을 것이다 <br>

## 테스트를 작성하는 마음가짐
### [1. 테스트는 왜 필요할까?]
- 테스트를 아예 작성하지 않는 경우 문제
  - 빠른 변화가 필요한 소프트웨어 품질을 일정 수준 이상으로 가져가기 힘들다.
  - 사람이 다 테스트를 할 수가 없다.
- 테스트가 병목이 되는 경우

#### [단위 테스트]
- 작은 코드 단위를 독립적으로 테스트 한다.
- 테스트가 용이한 부분을 확대해 나가야 한다.
- 테스트 하기 어려운 영역?
  - 매번 바뀌는 값? ex) 현재시간, 랜덤 값

#### [TDD]
- 프로덕션보다 테스트 코드를 먼저 작성하여 테스트가 구현과정을 주도하도록 하는 것이다
- 레드,그린 리팩토링을 통하여 하게 됨.

#### [테스트는 문서다]
- 남들이 봤을 때 내 테스트 코드가 말하는 요구사항을 알 수 있어야 한다.
- 언어가 사고를 제한한다

#### [Spring & JPA]
- Layered 아키텍쳐에 맞는 테스트가 필요하다.
  - Repository, Service 는 통합 테스트 느낌으로 테스트를 하는게 좋다.

타협하지 않는 마음이 제일 중요하다 <br>
가까이 보면 느리지만, 멀리 보면 가장 빠르다 <br>

테스트가 귀찮은 마음과 시간적인 압박이 있더라도, 내가 지금 시간을 더 투자하여 테스트를 작성하는게 <br>
미래의 수많은 시간을 아낄 수 있을 것이다.