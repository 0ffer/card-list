package ru.offer.cards.repository;

import lombok.val;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.offer.cards.model.Card;
import ru.offer.cards.model.CardList;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CardListRepositoryIntegrationTests extends BaseRepostoryIntegrationTest {

    @Test
    public void create() {
        val cardListRepository = client.<CardList, Long>getRepositoryForType(CardList.class);

        val cardListToCreate = new CardList();
        cardListToCreate.setTitle("card list title");

        val createdCardList = docs.capture("Create simple card list").call(() -> cardListRepository.create(cardListToCreate));

        assertThat(createdCardList.getTitle()).isEqualTo(cardListToCreate.getTitle());
    }

    @Test
    public void addCardToList() {
        val cardRepository = client.<Card, UUID>getRepositoryForType(Card.class);
        val cardListRepository = client.<CardList, Long>getRepositoryForType(CardList.class);
        val cardToCardListRepository = client.<Card, UUID, CardList, Long>getOneRepositoryForType(Card.class, CardList.class);

        val cardToCreate = new Card();
        cardToCreate.setTitle("new card title");
        cardToCreate.setContent("new card content");
        val createdCard = cardRepository.create(cardToCreate);

        val cardListToCreate = new CardList();
        cardListToCreate.setTitle("card list title");
        val createdCardList = cardListRepository.create(cardListToCreate);

        docs.capture("Add card to list").call((Runnable)() -> cardToCardListRepository.setRelation(createdCard, createdCardList.getId(), "cardList"));

        val queriedCardList = cardListRepository.findOne(createdCardList.getId(), null);

        assertThat(queriedCardList.getCards()).hasSize(1);
    }
}
