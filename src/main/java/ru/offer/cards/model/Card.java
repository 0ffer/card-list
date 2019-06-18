package ru.offer.cards.model;

import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Entity
@JsonApiResource(type = "card", resourcePath = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String title;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String shortLink;

    private String content;
}
