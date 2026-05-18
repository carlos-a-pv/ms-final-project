package co.edu.uniquindio.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ONBOARDING_EXCHANGE = "onboarding.exchange";
    public static final String OFFBOARDING_EXCHANGE = "offboarding.exchange";

    public static final String NOTIFICATION_ONBOARDING_QUEUE = "notification.onboarding.queue";
    public static final String PROFILE_ONBOARDING_QUEUE = "profile.onboarding.queue";
    public static final String AUTH_ONBOARDING_QUEUE = "auth.onboarding.queue";

    @Bean
    public FanoutExchange onboardingExchange() {
        return new FanoutExchange(ONBOARDING_EXCHANGE);
    }

    @Bean
    public Queue notificationOnboardingQueue() {
        return new Queue(NOTIFICATION_ONBOARDING_QUEUE, true);
    }

    @Bean
    public Queue profileOnboardingQueue() {
        return new Queue(PROFILE_ONBOARDING_QUEUE, true);
    }

    @Bean
    public Queue authOnboardingQueue() {
        return new Queue(AUTH_ONBOARDING_QUEUE, true);
    }

    @Bean
    public Binding notificationOnboardingBinding(Queue notificationOnboardingQueue, FanoutExchange onboardingExchange) {
        return BindingBuilder.bind(notificationOnboardingQueue).to(onboardingExchange);
    }

    @Bean
    public Binding profileBindOnboarding(Queue profileOnboardingQueue, FanoutExchange onboardingExchange) {
        return BindingBuilder.bind(profileOnboardingQueue).to(onboardingExchange);
    }

    @Bean
    public Binding authBindOnboarding(Queue authOnboardingQueue, FanoutExchange onboardingExchange) {
        return BindingBuilder.bind(authOnboardingQueue).to(onboardingExchange);
    }

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
