import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.xebialabs.restito.semantics.Action;
import com.xebialabs.restito.semantics.Condition;
import com.xebialabs.restito.server.StubServer;
import com.xebialabs.restito.support.junit.NeedsServer;
import helpers.BinaryResponseTest;
import nl.yurimeiburg.ondertekenen.dao.RESTEngine;
import nl.yurimeiburg.ondertekenen.objects.Transaction;
import nl.yurimeiburg.ondertekenen.objects.TransactionStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.builder.verify.VerifyHttp.verifyHttp;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by Yuri Meiburg on 28-1-2015.
 */
public class RESTEngineTest {
    private static final Logger LOGGER = LogManager.getLogger(RESTEngineTest.class);
    private static final int DEFAULT_TEST_PORT = 50080;

    @Test
    @NeedsServer
    public void testIfDefaultHeadersAreCopied() {
        StubServer server = new StubServer(DEFAULT_TEST_PORT).run();
        whenHttp(server).match(Condition.uri("/test")).then(Action.status(HttpStatus.OK_200), Action.stringContent("{ }"), Action.contentType(MediaType.APPLICATION_JSON));

        /* Create new Webresource and send a request. */
        Map<String, String> defaultHeaders = new HashMap<>();
        defaultHeaders.put("1", "2");
        defaultHeaders.put("3", "4");
        RESTEngine restEngine = new RESTEngine(defaultHeaders, Client.create());

        LOGGER.debug("Calling localhost server");
        WebResource.Builder webResourceBuilder= restEngine.getWebResourceBuilder("http://localhost:" + DEFAULT_TEST_PORT + "/test");
        webResourceBuilder.get(String.class);

        verifyHttp(server).once(
                Condition.method(Method.GET),
                Condition.withHeader("1", "2"),
                Condition.withHeader("3", "4")
        );

    }

    @Test
    public void testHandleBinaryResponse() throws Exception {
        ClientResponse clientResponseMock = mock(ClientResponse.class);
        when(clientResponseMock.getStatus())
                .thenReturn(ClientResponse.Status.OK.getStatusCode());
        when(clientResponseMock.getEntity(byte[].class))
                .thenReturn(new byte[]{
                        0x48, 0x61, 0x69, 0x6c, 0x20,
                        0x74, 0x6f, 0x20, 0x74, 0x68,
                        0x65, 0x20, 0x6b, 0x69, 0x6e,
                        0x67, 0x2c, 0x20, 0x62, 0x61,
                        0x62, 0x79, 0x21, 0x20, 0x2d,
                        0x2d, 0x20, 0x44, 0x75, 0x6b,
                        0x65, 0x20, 0x4e, 0x75, 0x6b,
                        0x65, 0x6d});
        RESTEngine restEngine = new RESTEngine(null, Client.create());
        BinaryResponseTest binaryResponseTest = restEngine.handleBinaryResponse(clientResponseMock, BinaryResponseTest.class);
        assertEquals("Hail to the king, baby! -- Duke Nukem", binaryResponseTest.getStringData());
    }

    @Test
    public void testHandleJSONResponse() throws Exception {
        ClientResponse clientResponseMock = mock(ClientResponse.class);
        when(clientResponseMock.getStatus())
                .thenReturn(ClientResponse.Status.OK.getStatusCode());
        when(clientResponseMock.getEntity(String.class))
                .thenReturn("{Id: 5e8cf1, Status: 20}");
        RESTEngine restEngine = new RESTEngine(null, Client.create());
        Transaction transaction = restEngine.handleJSONResponse(clientResponseMock, Transaction.class);
        assertEquals("5e8cf1", transaction.getId());
        assertEquals(TransactionStatus.IN_PROGRESS, transaction.getStatus());
    }

}
