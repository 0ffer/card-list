package ru.offer.cards.repository;

import io.crnk.data.jpa.JpaEntityRepositoryBase;
import org.springframework.stereotype.Service;
import ru.offer.cards.model.Card;

import java.util.UUID;

@Service
public class CardRepo extends JpaEntityRepositoryBase<Card, UUID> {

    public CardRepo() {
        super(Card.class);
    }

}