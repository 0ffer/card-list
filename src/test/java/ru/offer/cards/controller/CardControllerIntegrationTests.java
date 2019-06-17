package ru.offer.cards.controller;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
public class CardControllerIntegrationTests {

//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    CardService cardService;
//
//    @Autowired
//    CardMapper cardMapper;
//
//    @Autowired
//    ResourceConverter resourceConverter;
//
//    @Test
//    public void create() throws Exception {
//
//        val cardDto = new CardDto();
//        cardDto.setContent("content");
//        cardDto.setTitle("main title");
//
//        MockHttpServletRequestBuilder requestMock = MockMvcRequestBuilders
//                .post("/cards")
//                .content(resourceConverter.writeDocument(new JSONAPIDocument<>(cardDto)));
//        MvcResult mvcResult = mockMvc.perform(requestMock).andReturn();
//
//        CardDto cardDtoResult = resourceConverter.readDocument(mvcResult.getResponse().getContentAsByteArray(), CardDto.class).get();
//
//        assertThat(cardDtoResult).isEqualToIgnoringGivenFields(cardDto, "id");
//    }
//
//    @Test
//    public void update() throws Exception {
//
//        val cardDto = new CardDto();
//        cardDto.setContent("content");
//        cardDto.setTitle("main title");
//
//        cardService.save(cardMapper.fromDto(cardDto));
//
//        cardDto.setTitle("main title NOT");
//        cardDto.setContent(null);
//        cardDto.setId(4L);
//
//        MockHttpServletRequestBuilder requestMock = MockMvcRequestBuilders
//                .patch("/cards/4")
//                .content(resourceConverter.writeDocument(new JSONAPIDocument<>(cardDto)));
//        MvcResult mvcResult = mockMvc.perform(requestMock).andReturn();
//
//        CardDto cardDtoResult = resourceConverter.readDocument(mvcResult.getResponse().getContentAsByteArray(), CardDto.class).get();
//
//        System.out.println(cardDtoResult);
//
//        assertThat(cardDtoResult).isEqualToIgnoringGivenFields(cardDto, "id");
//    }


}
