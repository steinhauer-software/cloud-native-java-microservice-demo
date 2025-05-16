package software.steinhauer.schulung.cloudnative.productservice.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.steinhauer.schulung.cloudnative.productservice.model.Product;
import software.steinhauer.schulung.cloudnative.productservice.service.ProductService;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class DemoDataGenerator {

    private final ProductService productService;
    private final Random random = new Random();
    private final AtomicInteger counter = new AtomicInteger(0);
    private static final List<String> CATEGORIES = List.of("Elektronik", "Haushalt", "Mode", "Sport", "Garten");

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.info("Initialisiere Demo-Daten");

        // Erstelle 20 initiale Produkte
        IntStream.range(0, 20).forEach(i -> createRandomProduct());

        log.info("Demo-Daten initialisiert: {} Produkte erstellt", 20);
    }

    @Scheduled(fixedRate = 5000) // Alle 5 Sekunden
    public void generateRandomActivity() {
        int choice = random.nextInt(10);

        if (choice < 7) {
            // 70% Chance: Erstelle ein neues Produkt
            createRandomProduct();
        } else if (choice < 9) {
            // 20% Chance: Lösche ein vorhandenes Produkt
            deleteRandomProduct();
        } else {
            // 10% Chance: Generiere einen Fehler für Demo-Zwecke
            try {
                simulateError();
            } catch (Exception e) {
                log.error("Demo-Fehler aufgetreten", e);
            }
        }
    }

    private void createRandomProduct() {
        int id = counter.incrementAndGet();
        String category = CATEGORIES.get(random.nextInt(CATEGORIES.size()));
        double price = 5.0 + (random.nextDouble() * 95.0); // Preis zwischen 5 und 100

        Product product = new Product();
        product.setName("Demo-Produkt " + id);
        product.setDescription("Ein automatisch generiertes " + category + "-Produkt");
        product.setPrice(Math.round(price * 100) / 100.0); // Auf 2 Nachkommastellen runden

        productService.createProduct(product);
        log.debug("Demo-Produkt erstellt: {}", product.getName());
    }

    private void deleteRandomProduct() {
        List<Product> products = productService.getAllProducts();
        if (!products.isEmpty()) {
            Product randomProduct = products.get(random.nextInt(products.size()));
            productService.deleteProduct(randomProduct.getId());
            log.debug("Demo-Produkt gelöscht: {}", randomProduct.getName());
        }
    }

    private void simulateError() {
        // Simuliere einen Fehler für Demo-Zwecke
        if (random.nextBoolean()) {
            // Null Pointer Exception
            log.warn("Simuliere einen NullPointerException-Fehler");
            String nullString = null;
            nullString.length(); // NPE
        } else {
            // Arithmetischer Fehler
            log.warn("Simuliere einen ArithmeticException-Fehler");
            int division = 1 / 0; // ArithmeticException
        }
    }
}
