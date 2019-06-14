package ru.offer.cards.controller;

import com.github.jasminb.jsonapi.*;
import com.github.jasminb.jsonapi.exceptions.DocumentSerializationException;
import com.github.jasminb.jsonapi.models.errors.Error;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.offer.cards.dto.CardDto;
import ru.offer.cards.dto.mapper.CardMapper;
import ru.offer.cards.exception.ApiException;
import ru.offer.cards.model.Card;
import ru.offer.cards.service.CardService;

import javax.validation.constraints.NotNull;
import java.util.List;

import static ru.offer.cards.config.JsonApiConfig.JSON_API_CONTENT_TYPE;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final CardMapper cardMapper;
    private final ResourceConverter resourceConverter;

    @PostMapping(produces = JSON_API_CONTENT_TYPE)
    public ResponseEntity create(@RequestBody @NotNull final byte[] cardBytes) throws Exception {

        val inputDto = resourceConverter.readDocument(cardBytes, CardDto.class).get();

        val created = cardService.save(cardMapper.fromDto(inputDto));

        return ResponseEntity.ok(serialize(created));
    }

    @PatchMapping(value = "/{id}", produces = JSON_API_CONTENT_TYPE)
    public ResponseEntity update(@PathVariable @NotNull final Long id,
                                 @RequestBody @NotNull final byte[] cardBytes) throws Exception {

        if ( ! cardService.exists(id)) {
            val error = new Error();
            error.setTitle("Resourse not found");
            throw new ApiException(HttpStatus.NOT_FOUND, error);
        }

        val inputApiDoc = resourceConverter.readDocument(cardBytes, CardDto.class).get();

        val updated = cardService.save(cardMapper.fromDto(inputApiDoc));

        return ResponseEntity.ok(serialize(updated));
    }

    @DeleteMapping(value = "/{id}", produces = JSON_API_CONTENT_TYPE)
    public ResponseEntity delete(@PathVariable @NotNull final Long id) {
        if ( ! cardService.exists(id)) {
            return ResponseEntity.notFound().build();
        } else {
            cardService.delete(id);
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/{id}", produces = JSON_API_CONTENT_TYPE)
    public ResponseEntity getById(@PathVariable @NotNull final Long id) throws Exception {

        val card = cardService.findById(id);
        if(card.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(serialize(card.get()));
    }

    @RequestMapping(method = RequestMethod.GET, value = "", produces = JSON_API_CONTENT_TYPE)
    public ResponseEntity getAll() throws Exception {
        val cards = cardService.findAll();

        return ResponseEntity.ok(serializeCollection(cards));
    }

    private byte[] serialize(final Card card) throws DocumentSerializationException {
        val dto = cardMapper.toDto(card);
        val apiDocument = new JSONAPIDocument<CardDto>(dto);
        return resourceConverter.writeDocument(apiDocument);
    }

    private byte[] serializeCollection(final List<Card> cards) throws DocumentSerializationException {
        val dtos = cardMapper.toDtos(cards);
        val apiDocument = new JSONAPIDocument<List<CardDto>>(dtos);
        return resourceConverter.writeDocumentCollection(apiDocument);
    }
}


