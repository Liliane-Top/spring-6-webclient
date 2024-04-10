package nl.top.spring6webclient.client;

import com.fasterxml.jackson.databind.JsonNode;
import nl.top.spring6webclient.domain.BeerStyle;
import nl.top.spring6webclient.model.BeerDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

@Service
public class BeerClientImpl implements BeerClient {

    private final WebClient webClient;
    public static final String BEER_PATH = "/api/v3/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";


    public BeerClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Flux<String> getBeerString() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(String.class);
    }

    @Override
    public Flux<Map> getBeerMap() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(Map.class);
    }

    @Override
    public Flux<JsonNode> getBeerJSON() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(JsonNode.class);
    }

    @Override
    public Flux<BeerDTO> getBeerDTO() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(BeerDTO.class);
    }

    @Override
    public Mono<BeerDTO> getBeerById(String id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(BEER_PATH_ID).build(id))
                .retrieve()
                .bodyToMono(BeerDTO.class);
    }

    @Override
    public Flux<BeerDTO> getBeerByBeerStyle(BeerStyle beerStyle) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BEER_PATH)
                        .queryParam("beerStyle", beerStyle)
                        .build())
                .retrieve()
                .bodyToFlux(BeerDTO.class);
    }

    @Override
    public Mono<BeerDTO> createBeer(BeerDTO newBeerDTO) {
        return webClient.post()
                .uri(BEER_PATH)
                .body(Mono.just(newBeerDTO), BeerDTO.class)
                .retrieve()
                .toBodilessEntity()
                .flatMap(voidResponseEntity -> Mono.just(Objects.requireNonNull(voidResponseEntity
                        .getHeaders()
                        .get("Location")).get(0)))
                .map(path -> path.split("/")[path.split("/").length - 1])
                .flatMap(this::getBeerById);
    }

    @Override
    public Mono<BeerDTO> updateBeerById(BeerDTO beerDTO) {
        return webClient.put()
                .uri(uriBuilder -> uriBuilder.path(BEER_PATH_ID).build(beerDTO.getId()))
                .body(Mono.just(beerDTO), BeerDTO.class)
                .retrieve()
                .toBodilessEntity()
                .flatMap(voidResponseEntity -> getBeerById(beerDTO.getId()));
    }

    @Override
    public Mono<BeerDTO> patchBeerById(BeerDTO beerDTO) {
        return webClient.patch()
                .uri(uriBuilder -> uriBuilder.path(BEER_PATH_ID).build(beerDTO.getId()))
                .body(Mono.just(beerDTO), BeerDTO.class)
                .retrieve()
                .toBodilessEntity()
                .flatMap(voidResponseEntity -> getBeerById(beerDTO.getId()));
    }

    @Override
    public Mono<Void> deleteBeerById(String id) {
        return webClient.delete()
                .uri(uriBuilder -> uriBuilder.path(BEER_PATH_ID).build(id))
                .retrieve()
                .toBodilessEntity()
                .then();
    }
}
