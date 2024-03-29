package ru.offer.cards.model;

import io.crnk.core.resource.annotations.JsonApiField;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Card list representation.
 *
 * @author Stas Melnichuk
 */
@Data
@Entity
@JsonApiResource(type = "list", resourcePath = "lists")
public class CardList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @OneToMany(mappedBy = "cardList")
    @JsonApiField(postable = false)
    private List<Card> cards = new ArrayList<>();

}
