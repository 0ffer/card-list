package ru.offer.cards.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.offer.cards.dto.CardDto;
import ru.offer.cards.model.Card;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardMapper {
    CardDto toDto(Card card);
    Card fromDto(CardDto cardDto);
    List<CardDto> toDtos(List<Card> cards);
}
