package ru.offer.cards.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jasminb.jsonapi.DeserializationFeature;
import com.github.jasminb.jsonapi.ResourceConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import ru.offer.cards.dto.CardDto;
import ru.offer.cards.dto.CardListDto;

@Configuration
public class JsonApiConfig {

    public static final String JSON_API_CONTENT_TYPE = "application/vnd.api+json";
    public static final MediaType JSON_API_MEDIA_TYPE = MediaType.valueOf("application/vnd.api+json");

    //TODO сделать контролируемое имя сервера
    @Bean
    public ResourceConverter resourceConverter(final ObjectMapper objectMapper) {
        ResourceConverter resourceConverter = new ResourceConverter(objectMapper, "http://localhost:8080", CardDto.class, CardListDto.class);
//        resourceConverter.enableSerializationOption(SerializationFeature.INCLUDE_RELATIONSHIP_ATTRIBUTES);
        resourceConverter.disableDeserializationOption(DeserializationFeature.REQUIRE_RESOURCE_ID);
        return resourceConverter;
    }

}
