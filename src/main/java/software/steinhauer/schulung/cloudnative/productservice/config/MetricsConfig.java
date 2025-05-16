package software.steinhauer.schulung.cloudnative.productservice.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter productCreatedCounter(MeterRegistry registry) {
        return Counter.builder("product.created.count")
                .description("Anzahl der erstellten Produkte")
                .register(registry);
    }

    @Bean
    public Counter productDeletedCounter(MeterRegistry registry) {
        return Counter.builder("product.deleted.count")
                .description("Anzahl der gelöschten Produkte")
                .register(registry);
    }
}
