package ru.offer.cards.model;

import lombok.Data;
import org.hibernate.annotations.Generated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    private String content;
}
