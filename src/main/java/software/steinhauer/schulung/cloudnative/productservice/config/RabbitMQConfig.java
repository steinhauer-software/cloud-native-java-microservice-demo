package software.steinhauer.schulung.cloudnative.productservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "productExchange";
    public static final String ROUTING_KEY = "product.created";
    public static final String QUEUE = "productCreatedQueue";

    @Bean
    public DirectExchange productExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue productQueue() {
        // auto-delete is here for demo purposes only!!!
        return new Queue(QUEUE, true, false, true);
    }

    @Bean
    public Binding binding(Queue productQueue, DirectExchange productExchange) {
        return BindingBuilder.bind(productQueue).to(productExchange).with(ROUTING_KEY);
    }

    // Config JSON for serialisation
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
