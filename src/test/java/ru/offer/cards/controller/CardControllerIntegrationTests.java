package ru.offer.cards.controller;

import com.github.jasminb.jsonapi.JSONAPIDocument;
import com.github.jasminb.jsonapi.ResourceConverter;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.offer.cards.CardsApplication;
import ru.offer.cards.dto.CardDto;
import ru.offer.cards.dto.mapper.CardMapper;
import ru.offer.cards.service.CardService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CardControllerIntegrationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CardService cardService;

    @Autowired
    CardMapper cardMapper;

    @Autowired
    ResourceConverter resourceConverter;

    @Test
    public void create() throws Exception {

        val cardDto = new CardDto();
        cardDto.setContent("content");
        cardDto.setTitle("main title");

        MockHttpServletRequestBuilder requestMock = MockMvcRequestBuilders
                .post("/cards")
                .content(resourceConverter.writeDocument(new JSONAPIDocument<>(cardDto)));
        MvcResult mvcResult = mockMvc.perform(requestMock).andReturn();

        CardDto cardDtoResult = resourceConverter.readDocument(mvcResult.getResponse().getContentAsByteArray(), CardDto.class).get();

        assertThat(cardDtoResult).isEqualToIgnoringGivenFields(cardDto, "id");
    }

    @Test
    public void update() throws Exception {

        val cardDto = new CardDto();
        cardDto.setContent("content");
        cardDto.setTitle("main title");

        cardService.save(cardMapper.fromDto(cardDto));

        cardDto.setTitle("main title NOT");
        cardDto.setContent(null);
        cardDto.setId(4L);

        MockHttpServletRequestBuilder requestMock = MockMvcRequestBuilders
                .patch("/cards/4")
                .content(resourceConverter.writeDocument(new JSONAPIDocument<>(cardDto)));
        MvcResult mvcResult = mockMvc.perform(requestMock).andReturn();

        CardDto cardDtoResult = resourceConverter.readDocument(mvcResult.getResponse().getContentAsByteArray(), CardDto.class).get();

        System.out.println(cardDtoResult);

        assertThat(cardDtoResult).isEqualToIgnoringGivenFields(cardDto, "id");
    }


}
