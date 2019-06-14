package ru.offer.cards.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.offer.cards.model.CardList;

public interface CardListRepo extends JpaRepository<CardList, Long> {
}
