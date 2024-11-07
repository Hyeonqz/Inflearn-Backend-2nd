package com.hkjin.practicaltesting.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // Entity 변경시 시간 측정
@Configuration
public class JpaAuditingConfig {
	// test 시 auditing 빈 찾지 못하는 오류 해결을 위한 Config


}
