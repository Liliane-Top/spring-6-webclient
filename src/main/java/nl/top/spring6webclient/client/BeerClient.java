package nl.top.spring6webclient.client;

import com.fasterxml.jackson.databind.JsonNode;
import nl.top.spring6webclient.model.BeerDTO;
import reactor.core.CorePublisher;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.concurrent.Flow;

public interface BeerClient {

    Flux<String> getBeerString();

    Flux<Map> getBeerMap();

    Flux<JsonNode> getBeerJSON();


    Flux<BeerDTO> getBeerDTO();
}
