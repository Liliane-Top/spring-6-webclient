package nl.top.spring6webclient.client;

import reactor.core.publisher.Flux;

import java.util.Map;

public interface BeerClient {

    Flux<String> getBeerString();

    Flux<Map> getBeerMap();
}
