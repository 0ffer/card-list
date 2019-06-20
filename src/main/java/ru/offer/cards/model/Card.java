package ru.offer.cards.model;

import io.crnk.core.resource.annotations.JsonApiField;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Simple card presentation.
 *
 * It has auto generated short link unique property.
 *
 * @author Stas Melnichuk
 */
@Data
@Entity
@JsonApiResource(type = "card", resourcePath = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String title;

    @Column(unique=true, nullable = false, insertable = false, updatable = false)
    @Generated(GenerationTime.INSERT)
    @JsonApiField(deletable = false, patchable = false, postable = false)
    private String shortLink;

    private String content;
}
