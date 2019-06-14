package ru.offer.cards.dto;

import com.github.jasminb.jsonapi.LongIdHandler;
import com.github.jasminb.jsonapi.annotations.Id;
import com.github.jasminb.jsonapi.annotations.Links;
import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;
import lombok.Data;

@Data
@Type(value = "cards", path = "cards/{id}")
public class CardDto {

    @Id(LongIdHandler.class)
    private Long id;

    private String title;

    private String content;

//    @Relationship(value = "list", path = "relationships/list", relatedPath = "list")
//    private CardListDto cardList;
}
