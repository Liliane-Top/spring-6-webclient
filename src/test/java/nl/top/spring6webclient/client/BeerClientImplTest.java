package nl.top.spring6webclient.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClient beerClient;

    @Test
    void listBeer() {
        AtomicBoolean completed = new AtomicBoolean(false);

        beerClient.listBeer().subscribe(response -> {
            System.out.println(response);
            assertThat(response.contains("Crank"));
            completed.set(true);
        });

        await().untilTrue(completed);
    }
}