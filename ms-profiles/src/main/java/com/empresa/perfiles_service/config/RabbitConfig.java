package com.empresa.perfiles_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String ONBOARDING_EXCHANGE = "onboarding.exchange";

    public static final String OFFBOARDING_EXCHANGE = "offboarding.exchange";
    public static final String PROFILE_ONBOARDING_QUEUE = "profile.onboarding.queue";

    @Bean
    public FanoutExchange exchange() {
        return new FanoutExchange(ONBOARDING_EXCHANGE);
    }

    @Bean
    public Queue perfilesQueue() {
        return new Queue(PROFILE_ONBOARDING_QUEUE, true);
    }

    @Bean
    public Binding onboardingBinding(Queue perfilesQueue, FanoutExchange onboardingExchange) {
        return BindingBuilder.bind(perfilesQueue).to(onboardingExchange);
    }

    @Bean
    public Binding offboardingBinding(Queue perfilesQueue, FanoutExchange offboardingExchange) {
        return BindingBuilder.bind(perfilesQueue).to(offboardingExchange);
    }

    @Bean
    public MessageConverter messageConverter() {
        return  new Jackson2JsonMessageConverter();

    }
    /*@Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }*/

}