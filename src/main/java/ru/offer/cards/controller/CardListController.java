package ru.offer.cards.controller;

import com.github.jasminb.jsonapi.JSONAPIDocument;
import com.github.jasminb.jsonapi.ResourceConverter;
import com.github.jasminb.jsonapi.SerializationSettings;
import com.github.jasminb.jsonapi.models.errors.Error;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.offer.cards.dto.CardListDto;
import ru.offer.cards.dto.mapper.CardListMapper;
import ru.offer.cards.exception.ApiException;
import ru.offer.cards.model.Card;
import ru.offer.cards.model.CardList;
import ru.offer.cards.service.CardListService;
import ru.offer.cards.service.CardService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static ru.offer.cards.config.JsonApiConfig.JSON_API_CONTENT_TYPE;

@RestController
@RequestMapping("/lists")
@RequiredArgsConstructor
public class CardListController {

    private static final Set<String> allowedRelationsToInclude;

    static {
        allowedRelationsToInclude = new HashSet<>();
        allowedRelationsToInclude.add("cards");
    }

    private final CardListService cardListService;
    private final CardListMapper cardListMapper;
    private final ResourceConverter resourceConverter;

    private final CardService cardService;

    @PostConstruct
    public void postConstruct() {

        if(cardListService.count() >= 4) {
            return;
        }

        val emptyList = new CardList();
        emptyList.setTitle("emptyList");
        cardListService.save(emptyList);

        var singleList = new CardList();
//        singleList = cardListService.save(singleList);
        singleList.setTitle("not_single");
        val singleCard = new Card();
//        singleCard.setCardList(singleList);
        singleCard.setTitle("tro-lo-lo");
        singleCard.setContent("contenT_T");
        cardService.save(singleCard);
        singleList.getCards().add(singleCard);
        cardListService.save(singleList);

        var notSingleList = new CardList();
//        notSingleList = cardListService.save(notSingleList);
        notSingleList.setTitle("not_single");
        val secondCard = new Card();
        secondCard.setTitle("tro-lo-lo222");
        secondCard.setContent("contenT_T222");
//        secondCard.setCardList(notSingleList);
        cardService.save(secondCard);
        val thirdCard = new Card();
        thirdCard.setTitle("tro-lo-lo333");
        thirdCard.setContent("contenT_T333");
//        thirdCard.setCardList(notSingleList);
        cardService.save(thirdCard);

        notSingleList.getCards().add(thirdCard);
        notSingleList.getCards().add(secondCard);
        cardListService.save(notSingleList);
    }

    @GetMapping(path = "/{id}", produces = JSON_API_CONTENT_TYPE)
    public ResponseEntity getById(@PathVariable final long id,
                                  @RequestParam(value = "include", required = false) final Set<String> includeRelations) throws Exception {
        val cardList = cardListService.findById(id);

        if(cardList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        val cardDto = cardListMapper.toDto(cardList.orElse(null));

        val result = new JSONAPIDocument<CardListDto>(cardDto);
        return ResponseEntity.ok().body(resourceConverter.writeDocument(result));
    }

    @GetMapping(path = "", produces = JSON_API_CONTENT_TYPE)
    public ResponseEntity getAll(@RequestParam(value = "include", required = false) final Optional<Set<String>> includeRelations) throws Exception {
        if (includeRelations.isPresent() && ! allowedRelationsToInclude.containsAll(includeRelations.get())) {
            val error = new Error();
            error.setTitle("Bad relation names");
            throw new ApiException(HttpStatus.BAD_REQUEST, error);
        }

        val cardLists = cardListService.findAll();
        val dtos = cardListMapper.toDtos(cardLists);

        SerializationSettings.Builder serializationSettingsBuilder = new SerializationSettings.Builder();
        includeRelations.ifPresent(strings -> strings.forEach(serializationSettingsBuilder::includeRelationship));
        SerializationSettings serializeSettings = serializationSettingsBuilder.build();

        val document = new JSONAPIDocument<>(dtos);
        return ResponseEntity.ok().body(resourceConverter.writeDocumentCollection(document, serializeSettings));
    }

    @GetMapping(path = {"/{id}/relationships/cards", "/{id}/cards"},produces = JSON_API_CONTENT_TYPE)
    public ResponseEntity getRelationCards(@PathVariable final long id) throws Exception {
        Optional<CardList> cardList = cardListService.findById(id);
        if (cardList.isEmpty()) {
            val error = new Error();
            error.setTitle("Resource not found");
            throw new ApiException(HttpStatus.NOT_FOUND, error);
        } else {
            val cardDtos = cardListMapper.toDto(cardList.get()).getCards();
            return ResponseEntity.ok(resourceConverter.writeDocumentCollection(new JSONAPIDocument<>(cardDtos)));
        }
    }

}
