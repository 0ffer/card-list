package ru.offer.cards.repository;

import io.crnk.client.CrnkClient;
import io.crnk.client.http.apache.HttpClientAdapter;
import io.crnk.gen.asciidoc.capture.AsciidocCaptureConfig;
import io.crnk.gen.asciidoc.capture.AsciidocCaptureModule;
import org.junit.Before;
import org.junit.ClassRule;
import org.springframework.beans.factory.annotation.Value;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.offer.cards.utils.PostgresContainer;

import java.io.File;

/**
 * Base class with database initialization and crnk client preparing.
 *
 * @author Stas Melnichuk
 */
public class BaseRepostoryIntegrationTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresContainer.getInstance();

    @Value("${local.server.port}")
    protected int port;

    protected CrnkClient client;

    protected AsciidocCaptureModule docs;

    @Before
    public void setup() {
        docs = setupAsciidoc();

        client = new CrnkClient("http://localhost:" + port + "/api");
        client.addModule(docs);
        client.findModules();

        // Fix default openJDK behaviour that restrict PATCH requests
        client.setHttpAdapter(HttpClientAdapter.newInstance());
    }

    private AsciidocCaptureModule setupAsciidoc() {
        File outputDir = new File("build/generated/source/asciidoc");
        AsciidocCaptureConfig asciidocConfig = new AsciidocCaptureConfig();
        asciidocConfig.setGenDir(outputDir);
        return new AsciidocCaptureModule(asciidocConfig);
    }

}
