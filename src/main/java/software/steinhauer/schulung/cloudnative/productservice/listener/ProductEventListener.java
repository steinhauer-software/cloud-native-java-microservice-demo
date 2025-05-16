package software.steinhauer.schulung.cloudnative.productservice.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import software.steinhauer.schulung.cloudnative.productservice.config.RabbitMQConfig;
import software.steinhauer.schulung.cloudnative.productservice.event.ProductCreatedEvent;

@Component
public class ProductEventListener {

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleProductCreated(ProductCreatedEvent event) {
        System.out.printf("📦 Neues Produkt erstellt: ID=%d, Name=%s%n",
                event.getProductId(), event.getName());
    }
}
