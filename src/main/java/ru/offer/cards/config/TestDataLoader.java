package ru.offer.cards.config;

import io.crnk.core.engine.transaction.TransactionRunner;
import lombok.extern.java.Log;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.offer.cards.model.Card;
import ru.offer.cards.model.CardList;
import ru.offer.cards.repository.CardListRepo;
import ru.offer.cards.repository.CardRepo;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.Arrays;

@Configuration
@Profile("init-test-data")
@Log
public class TestDataLoader {

    @Autowired
    private EntityManager em;

    @Autowired
    private TransactionRunner transactionRunner;

    @Autowired
    CardRepo cardRepo;

    @Autowired
    CardListRepo cardListRepo;

    @PostConstruct
    public void setup() {
        transactionRunner.doInTransaction(() ->
            {

                val cardsCount = (Long) em.createQuery("SELECT COUNT(c) FROM Card c").getSingleResult();
                val cardListsCount = (Long) em.createQuery("SELECT COUNT(c) FROM CardList c").getSingleResult();
                if (cardsCount!=0 || cardListsCount!=0) {
                    log.info("There is yet exist some data. Interrupt generation.");
                    return null;
                }

                val firstCard = createCard("first", "first card content");
                val secondCard = createCard("second", "second card content");
                val thirdCard = createCard("third", "third card content");
                val fourthCard = createCard("fourth", "fourth card content");

                createCardList("first");
                createCardList("second", firstCard);
                createCardList("third", secondCard, thirdCard, fourthCard);

                return null;
            });
    }

    private CardList createCardList(final String title, final Card... cards) {
        val result = new CardList();
        result.setTitle(title);
        result.setCards(Arrays.asList(cards));
        em.persist(result);
        return result;
    }

    private Card createCard(final String title, final String content) {
        val result = new Card();
        result.setTitle(title);
        result.setContent(content);
        em.persist(result);
        return result;
    }

}
