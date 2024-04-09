package nl.top.spring6webclient.client;

import nl.top.spring6webclient.domain.BeerStyle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicBoolean;

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

    @Test
    @DisplayName("Test get list of all beers as a JSON")
    void getBeerJSON() {
        AtomicBoolean completed = new AtomicBoolean(false);
        beerClient.getBeerJSON().subscribe(response -> {
//            System.out.println(response.toPrettyString());
            assertThat(response.toPrettyString()).contains("beerName", "quantityOnHand");
            assertThat(response.size()).isGreaterThan(2);
            completed.set(true);
        });
        await().untilTrue(completed);
    }

    @Test
    @DisplayName("Test get list of all beers as a POJO/BeerDTO")
    void getBeerDTO() {
        AtomicBoolean completed = new AtomicBoolean(false);
        beerClient.getBeerDTO().subscribe(beerDTO -> {
            System.out.println(beerDTO.beerName());
            assertThat(beerDTO.beerName()).isNotNull();
            completed.set(true);
        });
        await().untilTrue(completed);
    }

    @Test
    @DisplayName("Test get a beer by ID")
    void getBeerById() {
        AtomicBoolean completed = new AtomicBoolean(false);
        beerClient.getBeerDTO()
                .flatMap(beerDTO -> {
                    System.out.println(beerDTO);
                    return beerClient.getBeerById(beerDTO.id());
                })
                .subscribe(foundBeer -> {
                    System.out.println(foundBeer.beerName());
                    assertThat(foundBeer.beerStyle()).isNotNull();
                    completed.set(true);
                });
        await().untilTrue(completed);
    }

    @Test
    @DisplayName("Test get all beers by beerStyle")
    void getBeerByBeerStyle() {
        AtomicBoolean completed = new AtomicBoolean(false);
        beerClient.getBeerByBeerStyle(BeerStyle.PALE_ALE)
                        .subscribe(foundBeer -> {
                            System.out.println(foundBeer);
                            assertThat(foundBeer).isNotNull();
                            assertThat(foundBeer.beerStyle()).isNotEqualByComparingTo(BeerStyle.IPA);
                            completed.set(true);
                        });
        await().untilTrue(completed);
    }
}