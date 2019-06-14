package ru.offer.cards.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.offer.cards.model.Card;

public interface CardRepo extends JpaRepository<Card, Long> {

}
