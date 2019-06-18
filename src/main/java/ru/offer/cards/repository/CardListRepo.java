package ru.offer.cards.repository;

import io.crnk.data.jpa.JpaEntityRepositoryBase;
import org.springframework.stereotype.Service;
import ru.offer.cards.model.CardList;

@Service
public class CardListRepo extends JpaEntityRepositoryBase<CardList, Long> {

    public CardListRepo() {
        super(CardList.class);
    }

}
