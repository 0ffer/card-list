package ru.offer.cards.dto.mapper;

import org.mapstruct.Mapper;
import ru.offer.cards.dto.CardListDto;
import ru.offer.cards.model.CardList;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardListMapper {
    CardListDto toDto(CardList cardList);
    CardList fromDto(CardListDto cardDto);
    List<CardListDto> toDtos(List<CardList> cards);
}
