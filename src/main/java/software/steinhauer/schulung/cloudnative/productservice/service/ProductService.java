package software.steinhauer.schulung.cloudnative.productservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.steinhauer.schulung.cloudnative.productservice.model.Product;
import software.steinhauer.schulung.cloudnative.productservice.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);

    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
