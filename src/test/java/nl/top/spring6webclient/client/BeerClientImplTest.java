package nl.top.spring6webclient.client;

import nl.top.spring6webclient.domain.BeerStyle;
import nl.top.spring6webclient.model.BeerDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BeerClientImplTest {

    @Autowired
    BeerClient beerClient;

    @Test
    @Order(1)
    @DisplayName("Test get list of all beers as a String")
    void listBeer() {
        AtomicBoolean completed = new AtomicBoolean(false);

        beerClient.getBeerString().subscribe(response -> {
            System.out.println(response);
            assertThat(response).contains("Crank");
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
            System.out.println(beerDTO.getBeerName());
            assertThat(beerDTO.getBeerName()).isNotNull();
            completed.set(true);
        });
        await().untilTrue(completed);
    }

    @Test
    @DisplayName("Test get a beer by ID")
    void getBeerById() {
        AtomicBoolean completed = new AtomicBoolean(false);
        beerClient.getBeerDTO()
                .flatMap(beerDTO -> beerClient.getBeerById(beerDTO.getId()))
                .subscribe(foundBeer -> {
                    assertThat(foundBeer.getBeerStyle()).isNotNull();
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
                    assertThat(foundBeer).isNotNull();
                    assertThat(foundBeer.getBeerStyle()).isNotEqualByComparingTo(BeerStyle.IPA);
                    completed.set(true);
                });
        await().untilTrue(completed);
    }

    @Test
    @DisplayName("Test create new beer and add to DB")
    void createNewBeer() {
        AtomicBoolean completed = new AtomicBoolean(false);

        BeerDTO newBeerDTO = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.GOSE)
                .quantityOnHand(500)
                .upc("12344")
                .build();

        beerClient.createBeer(newBeerDTO)
                .subscribe(beerDTO -> {
                    assertThat(beerDTO.getBeerName()).isEqualTo(newBeerDTO.getBeerName());
                    completed.set(true);
                });
        await().untilTrue(completed);
    }

    @Test
    @DisplayName("Test to update a beer by Id")
    void updateBeer() {
        final String NAME = "Blond biertje";

        AtomicBoolean completed = new AtomicBoolean(false);

        beerClient.getBeerDTO()
                .next()
                .doOnNext(beerDTO -> beerDTO.setBeerName(NAME))
                .flatMap(beerDTO -> beerClient.updateBeerById(beerDTO))
                .subscribe(updatedBeer -> {
                    assertThat(updatedBeer.getBeerName()).isEqualTo(NAME);
                    completed.set(true);
                });

        await().untilTrue(completed);
    }

    @Test
    @DisplayName("Test to patch a beer by Id")
    void patchBeer() {
        final BeerStyle BEERSTYLE = BeerStyle.LAGER;
        final String newBeerName = "Amstelbrier";

        AtomicBoolean completed = new AtomicBoolean(false);

        beerClient.getBeerDTO().next()
                .doOnNext(beerDTO -> {
                    beerDTO.setBeerStyle(BEERSTYLE);
                    beerDTO.setBeerName(newBeerName);
                })
                .flatMap(beerDTO -> beerClient.patchBeerById(beerDTO))
                .subscribe(patchedBeer -> {
                    assertThat(patchedBeer.getBeerStyle()).isEqualTo(BEERSTYLE);
                    assertThat(patchedBeer.getBeerName()).isEqualTo(newBeerName);
                    completed.set(true);
                });

        await().untilTrue(completed);
    }

    @Test
    @DisplayName("Test delete beer by Id")
    void deleteBeerById() {
        BeerDTO beerToDelete = beerClient.getBeerDTO().blockFirst();
        assertThat(beerToDelete).isNotNull();
        String beerToDeleteId = beerToDelete.getId();
        beerClient.deleteBeerById(beerToDeleteId).block();
        assertThatThrownBy(() -> {
                    beerClient.getBeerById(beerToDeleteId).block();

                }).isInstanceOf(WebClientResponseException.class);
    }

    @Test
    @DisplayName("Test delete beer by Id alternative way")
    void deleteBeerByIdAlternative() {
        AtomicBoolean completed = new AtomicBoolean(false);

        beerClient.getBeerDTO()
                .next()
                .flatMap(beerDTO -> beerClient.deleteBeerById(beerDTO.getId()))
                .doOnSuccess(succes -> completed.set(true))
                .subscribe();

        await().untilTrue(completed);

    }
}