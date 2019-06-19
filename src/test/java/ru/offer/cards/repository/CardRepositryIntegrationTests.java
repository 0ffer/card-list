package ru.offer.cards.repository;

import io.crnk.client.CrnkClient;
import io.crnk.client.http.apache.HttpClientAdapter;
import io.crnk.core.queryspec.FilterOperator;
import io.crnk.core.queryspec.FilterSpec;
import io.crnk.core.queryspec.PathSpec;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepository;
import io.crnk.gen.asciidoc.capture.AsciidocCaptureConfig;
import io.crnk.gen.asciidoc.capture.AsciidocCaptureModule;
import lombok.val;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.offer.cards.model.Card;
import ru.offer.cards.utils.PostgresContainer;

import java.io.File;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CardRepositryIntegrationTests {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresContainer.getInstance();

    @Value("${local.server.port}")
    protected int port;

    private CrnkClient client;

    private AsciidocCaptureModule docs;

    @Before
    public void setup() {
        docs = setupAsciidoc();

        client = new CrnkClient("http://localhost:" + port + "/");
        client.addModule(docs);
        client.findModules();

        // Fix default openJDK that restrict PATCH requests
        client.setHttpAdapter(HttpClientAdapter.newInstance());
    }

    private AsciidocCaptureModule setupAsciidoc() {
        File outputDir = new File("build/generated/source/asciidoc");
        AsciidocCaptureConfig asciidocConfig = new AsciidocCaptureConfig();
        asciidocConfig.setGenDir(outputDir);
        return new AsciidocCaptureModule(asciidocConfig);
    }

    @Test
    public void createCard() {
        val cardToCreate = new Card();
        cardToCreate.setTitle("new card title");
        cardToCreate.setContent("new card content");

        ResourceRepository<Card, UUID> repository = client.getRepositoryForType(Card.class);

        val createdCard = docs.capture("Create card").call(() -> repository.create(cardToCreate));

        assertThat(cardToCreate).isEqualToIgnoringGivenFields(createdCard, "id", "shortLink");
    }

    @Test
    public void updateCard() {
        val cardToCreate = new Card();
        cardToCreate.setTitle("new card title");
        cardToCreate.setContent("new card content");

        ResourceRepository<Card, UUID> repository = client.getRepositoryForType(Card.class);

        val createdCard = repository.create(cardToCreate);

        createdCard.setId(createdCard.getId());
        createdCard.setTitle("Updated card title");

        val updatedCard = docs.capture("Update card").call(() -> repository.save(createdCard));

        System.out.println(updatedCard);

        assertThat(updatedCard).isEqualTo(createdCard);
    }

    @Test
    public void findCardByShortLink() {
        val cardToCreate = new Card();
        cardToCreate.setTitle("new card title");
        cardToCreate.setContent("new card content");

        ResourceRepository<Card, UUID> repository = client.getRepositoryForType(Card.class);

        val createdCard = repository.create(cardToCreate);

        val query = new QuerySpec(Card.class);
        val filter = new FilterSpec(PathSpec.of("shortLink"), FilterOperator.EQ, createdCard.getShortLink());
        query.addFilter(filter);

        val findedCards = docs.capture("Find card by short link").call(() -> repository.findAll(query));

        System.out.println(findedCards);

        assertThat(findedCards).containsOnly(createdCard);
    }
}
