package ru.offer.cards.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.offer.cards.model.Card;
import ru.offer.cards.model.CardList;
import ru.offer.cards.repository.CardListRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardListService {

    private final CardListRepo cardListRepo;

    public List<CardList> findAll() {
        return cardListRepo.findAll();
    }

    public Optional<CardList> findById(final long id) {
        return cardListRepo.findById(id);
    }

    public CardList save(final CardList cardList) {
        return cardListRepo.saveAndFlush(cardList);
    }

    public void delete(final long id) {
        cardListRepo.deleteById(id);
    }

    public boolean exists(final long id) {
        return cardListRepo.existsById(id);
    }

    public long count() {
        return cardListRepo.count();
    }
}
