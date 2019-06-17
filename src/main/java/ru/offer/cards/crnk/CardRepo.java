package ru.offer.cards.crnk;

import io.crnk.data.jpa.JpaEntityRepositoryBase;
import org.springframework.stereotype.Service;
import ru.offer.cards.model.Card;

@Service
public class CardRepo extends JpaEntityRepositoryBase<Card, Long> {

    public CardRepo() {
        super(Card.class);
    }

}