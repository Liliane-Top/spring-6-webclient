package nl.top.spring6webclient.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClient beerClient;

    @Test
    @DisplayName("Test get list of all beers as a String")
    void listBeer() {
        AtomicBoolean completed = new AtomicBoolean(false);

        beerClient.getBeerString().subscribe(response -> {
            System.out.println(response);
            assertThat(response.contains("Crank"));
            completed.set(true);
        });

        await().untilTrue(completed);
    }

    @Test
    @DisplayName("Test get list of all beers as a Map")
    void getBeerMap() {
        AtomicBoolean completed = new AtomicBoolean(false);
        beerClient.getBeerMap().subscribe(response -> {
            System.out.println(response);
            System.out.println(response.keySet());
            assertThat(response.keySet().size()).isEqualTo(8);
            completed.set(true);
        });
        await().untilTrue(completed);
    }
}