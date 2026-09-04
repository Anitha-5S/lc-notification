package com.c2.lc.ms.notification.c.push;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;

@SpringBootApplication(scanBasePackages = {"com.c2.lc.lib", "com.c2.lc.ms.notification.lib","com.c2.lc.ms.notification.p","com.c2.lc.ms.notification.c.push"})
@EntityScan("com.c2.lc.ms.notification.lib.entities")
@EnableJpaRepositories("com.c2.lc.ms.notification.lib.repos")
public class MsNotificationCPushApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MsNotificationCPushApplication.class)
                .properties("spring.config.name:application,db,log,spy", "spring.config.location:classpath:/")
                .build()
                .run(args);
    }

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource("serviceAccountKey.json").getInputStream());
        FirebaseOptions firebaseOptions = FirebaseOptions
                .builder()
                .setCredentials(googleCredentials)
                .build();
        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "c2notification");
        return FirebaseMessaging.getInstance(app);
    }


}
