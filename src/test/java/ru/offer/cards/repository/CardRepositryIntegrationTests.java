package ru.offer.cards.repository;

import io.crnk.core.queryspec.FilterOperator;
import io.crnk.core.queryspec.FilterSpec;
import io.crnk.core.queryspec.PathSpec;
import io.crnk.core.queryspec.QuerySpec;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.offer.cards.model.Card;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CardRepositryIntegrationTests extends BaseRepostoryIntegrationTest {

    @Test
    public void createCard() {
        val cardToCreate = new Card();
        cardToCreate.setTitle("new card title");
        cardToCreate.setContent("new card content");

        val repository = client.<Card, UUID>getRepositoryForType(Card.class);

        val createdCard = docs.capture("Create card").call(() -> repository.create(cardToCreate));

        assertThat(cardToCreate).isEqualToIgnoringGivenFields(createdCard, "id", "shortLink");
    }

    @Test
    public void updateCard() {
        val cardToCreate = new Card();
        cardToCreate.setTitle("new card title");
        cardToCreate.setContent("new card content");

        val repository = client.<Card, UUID>getRepositoryForType(Card.class);

        val createdCard = repository.create(cardToCreate);

        createdCard.setId(createdCard.getId());
        createdCard.setTitle("Updated card title");

        val updatedCard = docs.capture("Update card").call(() -> repository.save(createdCard));

        System.out.println(updatedCard);

        assertThat(updatedCard).isEqualTo(createdCard);
    }

    @Test
    public void findCardByShortLink() {
        val cardToCreate = new Card();
        cardToCreate.setTitle("new card title");
        cardToCreate.setContent("new card content");

        val repository = client.<Card, UUID>getRepositoryForType(Card.class);

        val createdCard = repository.create(cardToCreate);

        val query = new QuerySpec(Card.class);
        val filter = new FilterSpec(PathSpec.of("shortLink"), FilterOperator.EQ, createdCard.getShortLink());
        query.addFilter(filter);

        val findedCards = docs.capture("Find card by short link").call(() -> repository.findAll(query));

        assertThat(findedCards).containsOnly(createdCard);
    }
}
