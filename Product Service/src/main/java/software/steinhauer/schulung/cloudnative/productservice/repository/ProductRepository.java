package software.steinhauer.schulung.cloudnative.productservice.repository;

import software.steinhauer.schulung.cloudnative.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
