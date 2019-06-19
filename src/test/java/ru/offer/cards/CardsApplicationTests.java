package ru.offer.cards;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.offer.cards.utils.PostgresContainer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CardsApplicationTests {

	@ClassRule
	public static PostgreSQLContainer postgreSQLContainer = PostgresContainer.getInstance();

	@Test
	public void contextLoads() {
	}

}
