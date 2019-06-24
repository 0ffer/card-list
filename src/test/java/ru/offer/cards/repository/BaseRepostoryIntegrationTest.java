package ru.offer.cards.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.crnk.client.CrnkClient;
import io.crnk.client.http.HttpAdapterListener;
import io.crnk.client.http.HttpAdapterRequest;
import io.crnk.client.http.HttpAdapterResponse;
import io.crnk.client.http.apache.HttpClientAdapter;
import io.crnk.gen.asciidoc.capture.AsciidocCaptureConfig;
import io.crnk.gen.asciidoc.capture.AsciidocCaptureModule;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.ClassRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.offer.cards.utils.PostgresContainer;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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

    @Autowired
    private ObjectMapper objectMapper;

    protected CrnkClient client;

    protected AsciidocCaptureModule docs;

    @Before
    public void setup() {
        docs = setupAsciidoc();

        client = new CrnkClient(getAppBaseUrl() + "/api");
        client.addModule(docs);
        client.findModules();

        // Fix default openJDK behaviour that restrict PATCH requests
        client.setHttpAdapter(HttpClientAdapter.newInstance());

        // Add mock for authentication.
        // Generate token when tests start and use it on each request.
        client.getHttpAdapter().addListener(oauthHttpListener());
    }

    private AsciidocCaptureModule setupAsciidoc() {
        File outputDir = new File("build/generated/source/asciidoc");
        AsciidocCaptureConfig asciidocConfig = new AsciidocCaptureConfig();
        asciidocConfig.setGenDir(outputDir);
        return new AsciidocCaptureModule(asciidocConfig);
    }

    /**
     * It is simple mock for aouth authentication.
     *
     * It creates single token, and use it for avery test.
     *
     * If tests take more than 5 minutes - token will be bad.
     */
    private HttpAdapterListener oauthHttpListener() {
        val token = getOauthToken();
        return new HttpAdapterListener() {
            @Override
            public void onRequest(HttpAdapterRequest request) {
                request.header("Authorization", token);
            }

            @Override
            public void onResponse(HttpAdapterRequest request, HttpAdapterResponse response) {

            }
        };
    }

    @SneakyThrows({UnsupportedEncodingException.class, IOException.class})
    private String getOauthToken () {

        val provider = new BasicCredentialsProvider();
        val credentials = new UsernamePasswordCredentials("ClientId", "secret");
        provider.setCredentials(AuthScope.ANY, credentials);

        val client = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(provider)
                .build();

        val httpPost = new HttpPost(getAppBaseUrl() + "/oauth/token?grant_type=password&username=admin&password=admin");

        val response = client.execute(httpPost);

        val requestInJosnFormat = objectMapper.readTree(response.getEntity().getContent());

        val token = requestInJosnFormat.findValue("access_token").textValue();

        return "Bearer " + token;
    }

    private String getAppBaseUrl() {
        return "http://localhost:" + port;
    }

}
