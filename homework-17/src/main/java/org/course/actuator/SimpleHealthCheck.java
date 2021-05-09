package org.course.actuator;

import lombok.RequiredArgsConstructor;
import org.course.domain.Book;
import org.course.repository.BookRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SimpleHealthCheck implements HealthIndicator {

    private final BookRepository bookRepository;

    @Override
    public Health health() {
        Optional<Book> optionalBook = bookRepository.findAll().stream().filter(b -> b.getQuantity() < 0).findFirst();
        return optionalBook.map(b -> Health.down().withDetail("Book have negative quantity:", b.getId()).build())
                .orElse(Health.up().build());
    }

}
