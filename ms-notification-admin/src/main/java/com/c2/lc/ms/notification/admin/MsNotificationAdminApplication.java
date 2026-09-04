package com.c2.lc.ms.notification.admin;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories ("com.c2.lc.ms.notification.lib.repos")
@SpringBootApplication(scanBasePackages = {"com.c2.lc.lib", "com.c2.lc.ms.notification.lib","com.c2.lc.ms.notification.admin"})
@EntityScan("com.c2.lc.ms.notification.lib.entities")
public class MsNotificationAdminApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder(MsNotificationAdminApplication.class)
				.properties("spring.config.name:application,db,log,spy", "spring.config.location:classpath:/")
				.build()
				.run(args);
	}

}
