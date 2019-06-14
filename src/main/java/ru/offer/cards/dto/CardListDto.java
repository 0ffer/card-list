package ru.offer.cards.dto;

import com.github.jasminb.jsonapi.LongIdHandler;
import com.github.jasminb.jsonapi.annotations.Id;
import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;
import lombok.Data;

import java.util.List;

@Data
@Type(value = "lists", path = "lists/{id}")
public class CardListDto {

    @Id(LongIdHandler.class)
    private Long id;

    private String title;

    @Relationship(value = "cards", path = "relationships/cards", relatedPath = "cards")
    private List<CardDto> cards;

//    @RelationshipLinks("cards")
//    private Links cardsLinks;
}
