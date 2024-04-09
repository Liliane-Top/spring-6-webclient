package nl.top.spring6webclient.client;

import com.fasterxml.jackson.databind.JsonNode;
import nl.top.spring6webclient.domain.BeerStyle;
import nl.top.spring6webclient.model.BeerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface BeerClient {

    Flux<String> getBeerString();

    Flux<Map> getBeerMap();

    Flux<JsonNode> getBeerJSON();


    Flux<BeerDTO> getBeerDTO();

    Mono<BeerDTO> getBeerById(String id);

    Flux<BeerDTO> getBeerByBeerStyle(BeerStyle beerStyle);

    Mono<BeerDTO> createBeer(BeerDTO newBeerDTO);

    Mono<BeerDTO> updateBeerById(BeerDTO beerDTO);
}
