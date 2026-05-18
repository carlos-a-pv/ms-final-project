package com.example.jwt_security.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String ONBOARDING_EXCHANGE = "onboarding.exchange";
    public static final String AUTH_ONBOARDING_QUEUE = "auth.onboarding.queue";

    @Bean
    public FanoutExchange exchange() {
        return new FanoutExchange(ONBOARDING_EXCHANGE);
    }

    @Bean
    public Queue authQueue() {
        return new Queue(AUTH_ONBOARDING_QUEUE, true);
    }

    @Bean
    public Binding onboardingBinding(Queue authQueue, FanoutExchange onboardingExchange) {
        return BindingBuilder.bind(authQueue).to(onboardingExchange);
    }

    @Bean
    public MessageConverter messageConverter() {
        return  new JacksonJsonMessageConverter();
    }
}
