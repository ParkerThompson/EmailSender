package com.brightwheel.application;

import com.brightwheel.services.EmailService;
import com.brightwheel.services.SendGridEmailService;
import com.brightwheel.services.SnailgunEmailService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
@SpringBootConfiguration
public class SpringBootConfig {

    /**
     * Bean for template for sending REST requests
     * @return template for sending REST requests
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Bean for service to send emails through sendgrid
     * @return service to send emails through sendgrid
     */
    @Profile("sendgrid")
    @Qualifier("emailService")
    @Bean
    public EmailService sendGridEmailService() {
       return new SendGridEmailService();
    }

    /**
     * Bean for service to send emails through snailgun
     * @return service to send emails through snailgun
     */
    @Profile("snailgun")
    @Qualifier("emailService")
    @Bean
    public EmailService snailgunEmailService() {
        return new SnailgunEmailService();
    }
}
