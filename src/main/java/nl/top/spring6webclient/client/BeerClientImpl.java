package nl.top.spring6webclient.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Map;

@Service
public class BeerClientImpl implements BeerClient {

    private final WebClient webClient;
    public static final String BEER_PATH = "/api/v3/beer";

    public BeerClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = WebClient.builder().baseUrl("http://localhost:8080").build();
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
}
