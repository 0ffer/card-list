package ru.offer.cards.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.offer.cards.model.Card;
import ru.offer.cards.repository.CardRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepo cardRepo;

    public List<Card> findAll() {
        return cardRepo.findAll();
    }

    public Optional<Card> findById(final long id) {
        return cardRepo.findById(id);
    }

    public Card save(final Card card) {
        return cardRepo.saveAndFlush(card);
    }

    public void delete(final long id) {
        cardRepo.deleteById(id);
    }

    public boolean exists(final long id) {
        return cardRepo.existsById(id);
    }

    public long count() {
        return cardRepo.count();
    }

}
