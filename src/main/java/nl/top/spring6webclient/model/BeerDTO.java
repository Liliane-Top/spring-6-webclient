package nl.top.spring6webclient.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record BeerDTO(String id,
                      String beerName,
                      String beerStyle,
                      String upc,
                      Integer quantityOnHand,
                      BigDecimal price,
                      LocalDateTime createdDate,
                      LocalDateTime lastModifiedDate) {
}
