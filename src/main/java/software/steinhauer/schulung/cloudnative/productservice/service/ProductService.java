package software.steinhauer.schulung.cloudnative.productservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import software.steinhauer.schulung.cloudnative.productservice.config.RabbitMQConfig;
import software.steinhauer.schulung.cloudnative.productservice.event.ProductCreatedEvent;
import software.steinhauer.schulung.cloudnative.productservice.model.Product;
import software.steinhauer.schulung.cloudnative.productservice.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final RabbitTemplate rabbitTemplate;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {

        var createdProduct = productRepository.save(product);


        var event = new ProductCreatedEvent(
                createdProduct.getId(),
                createdProduct.getName()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                event
        );

        return createdProduct;
    }


    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
