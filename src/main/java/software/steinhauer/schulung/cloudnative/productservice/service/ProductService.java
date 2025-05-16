package software.steinhauer.schulung.cloudnative.productservice.service;

import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import software.steinhauer.schulung.cloudnative.productservice.config.RabbitMQConfig;
import software.steinhauer.schulung.cloudnative.productservice.event.ProductCreatedEvent;
import software.steinhauer.schulung.cloudnative.productservice.model.Product;
import software.steinhauer.schulung.cloudnative.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final RabbitTemplate rabbitTemplate;
    private final Counter productCreatedCounter;
    private final Counter productDeletedCounter;

    public List<Product> getAllProducts() {
        log.debug("Fetching all products");
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        log.debug("Fetching product with ID: {}", id);
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        log.info("Creating new product: {}", product.getName());

        var createdProduct = productRepository.save(product);

        log.debug("Product saved with ID: {}", createdProduct.getId());
        productCreatedCounter.increment();

        var event = new ProductCreatedEvent(
                createdProduct.getId(),
                createdProduct.getName()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                event
        );

        log.debug("Product creation event sent to message queue");

        return createdProduct;
    }

    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        productRepository.deleteById(id);
        productDeletedCounter.increment();
        log.debug("Product deletion completed, ID: {}", id);
    }
}
