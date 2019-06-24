package ru.offer.cards.repository;

import io.crnk.core.queryspec.FilterOperator;
import io.crnk.core.queryspec.FilterSpec;
import io.crnk.core.queryspec.PathSpec;
import io.crnk.core.queryspec.QuerySpec;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.offer.cards.model.Card;
import ru.offer.cards.model.CardList;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CardRepositryIntegrationTests extends BaseRepostoryIntegrationTest {

    @Test
    public void createCard() {
        val cardToCreate = new Card();
        cardToCreate.setTitle("new card title");
        cardToCreate.setContent("new card content");

        val repository = client.<Card, UUID>getRepositoryForType(Card.class);

        val createdCard = docs.capture("Create card").call(() -> repository.create(cardToCreate));

        assertThat(cardToCreate).isEqualToIgnoringGivenFields(createdCard, "id", "shortLink");
    }

    @Test
    public void createWithCardList() {
        val cardRepository = client.<Card, UUID>getRepositoryForType(Card.class);
        val cardListRepository = client.<CardList, Long>getRepositoryForType(CardList.class);

        val cardListToCreate = new CardList();
        cardListToCreate.setTitle("card list title");
        val createdCardList = docs.capture("Create simple card list").call(() -> cardListRepository.create(cardListToCreate));

        val cardToCreate = new Card();
        cardToCreate.setTitle("new card title");
        cardToCreate.setContent("new card content");
        cardToCreate.setCardList(createdCardList);

        val createdCard = docs.capture("Create card with CardList").call(() ->cardRepository.create(cardToCreate));

        assertThat(createdCard.getCardList()).isNotNull();
    }

    @Test
    public void createWithImage() throws Exception {
        val cardRepository = client.<Card, UUID>getRepositoryForType(Card.class);

        val cardToCreate = new Card();
        cardToCreate.setTitle("new card title");
        cardToCreate.setContent("new card content");

        val imageBytes = IOUtils.resourceToByteArray("card_image.png", this.getClass().getClassLoader());
        cardToCreate.setImage(imageBytes);

        val createdCard = docs.capture("Create card with image bytes").call(() -> cardRepository.create(cardToCreate));

        // Uncomment to see the image, Add -Djava.awt.headless=false VM option to avoid Headless exception.
        /*BufferedImage img = ImageIO.read(new ByteArrayInputStream(createdCard.getImage()));
        ImageIcon icon = new ImageIcon(img);
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(200, 300);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/

        assertThat(createdCard.getImage()).isNotNull();
    }

    @Test
    public void updateCard() {
        val cardToCreate = new Card();
        cardToCreate.setTitle("new card title");
        cardToCreate.setContent("new card content");

        val repository = client.<Card, UUID>getRepositoryForType(Card.class);

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

        val repository = client.<Card, UUID>getRepositoryForType(Card.class);

        val createdCard = repository.create(cardToCreate);

        val query = new QuerySpec(Card.class);
        val filter = new FilterSpec(PathSpec.of("shortLink"), FilterOperator.EQ, createdCard.getShortLink());
        query.addFilter(filter);

        val findedCards = docs.capture("Find card by short link").call(() -> repository.findAll(query));

        assertThat(findedCards).containsOnly(createdCard);
    }
}
