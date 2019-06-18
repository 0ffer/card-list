package ru.offer.cards.repository;

import io.crnk.client.CrnkClient;
import io.crnk.client.http.okhttp.OkHttpAdapter;
import io.crnk.core.repository.ResourceRepository;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.offer.cards.model.Card;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CardRepositryIntegrationTests {

    @Value("${local.server.port}")
    protected int port;

    private CrnkClient client;

    @Before
    public void setup() {
        client = new CrnkClient("http://localhost:" + port + "/");
        client.findModules();

        // Fix default openJDK that restrict PATCH requests
        client.setHttpAdapter(OkHttpAdapter.newInstance());
    }

    @Test
    public void createCard() {
        val cardToCreate = new Card();
        cardToCreate.setTitle("new card title");
        cardToCreate.setContent("new card content");

        ResourceRepository<Card, UUID> repository = client.getRepositoryForType(Card.class);

        val createdCard = repository.create(cardToCreate);

        assertThat(cardToCreate).isEqualToIgnoringGivenFields(createdCard, "id");
    }

    @Test
    public void updateCard() {
        val cardToCreate = new Card();
        cardToCreate.setTitle("new card title");
        cardToCreate.setContent("new card content");

        ResourceRepository<Card, UUID> repository = client.getRepositoryForType(Card.class);

        val createdCard = repository.create(cardToCreate);

        createdCard.setTitle("updated card title");
        createdCard.setContent("updated card content");

        val updatedCard = repository.save(createdCard);

        assertThat(updatedCard).isEqualTo(createdCard);
    }
}
