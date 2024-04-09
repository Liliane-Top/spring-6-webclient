package nl.top.spring6webclient.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
@Service
public class BeerClientImpl implements BeerClient {

    private final WebClient webClient;

    public BeerClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = WebClient.builder().baseUrl("http://localhost:8080").build();
    }

    @Override
    public Flux<String> listBeer() {
        return webClient.get().uri("/api/v3/beer", String.class)
                .retrieve().bodyToFlux(String.class);
    }
}
