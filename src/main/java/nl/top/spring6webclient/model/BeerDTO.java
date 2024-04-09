package nl.top.spring6webclient.model;

import lombok.Builder;
import lombok.Data;
import nl.top.spring6webclient.domain.BeerStyle;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
public class BeerDTO {
    String id;
    String beerName;
    BeerStyle beerStyle;
    String upc;
    Integer quantityOnHand;
    BigDecimal price;
    LocalDateTime createdDate;
    LocalDateTime lastModifiedDate;
}
