import com.xebialabs.restito.semantics.Condition;
import com.xebialabs.restito.server.StubServer;
import com.xebialabs.restito.support.junit.NeedsServer;
import helpers.Helper;
import nl.yurimeiburg.ondertekenen.dao.OndertekenenClient;
import nl.yurimeiburg.ondertekenen.objects.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.misc.Regexp;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.net.URISyntaxException;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.builder.verify.VerifyHttp.verifyHttp;
import static com.xebialabs.restito.semantics.Action.*;
import static com.xebialabs.restito.semantics.Condition.*;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by Yuri Meiburg on 12-2-2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring_unittests.xml")
@ComponentScan
public class OndertekenenClientRestImplTest {
    private static final int DEFAULT_TEST_PORT = 50080;
    private final String TEST_STRING = "Hail to the king, baby! -- Duke Nukem";
    private static final Logger LOGGER = LogManager.getLogger(OndertekenenClientRestImplTest.class);
    private StubServer server = null;

    @Resource(name = "ondertekenenClient")
    OndertekenenClient ondertekenenClient;

    @Before
    public void init() {
        /* Create server object */
        server = new StubServer(DEFAULT_TEST_PORT).run();
        whenHttp(server)
                .match(post("/api/transaction/"))
                .then(status(HttpStatus.OK_200), stringContent("{ Id: 12345 }")
                );
        whenHttp(server)
                .match(startsWithUri("/api/transaction/"), method(Method.GET))
                .then(status(HttpStatus.OK_200), stringContent("{ Id: 12345, Status: 20 }")
                );
        whenHttp(server)
                .match(delete("/api/transaction/12345"))
                .then(status(HttpStatus.OK_200), stringContent("{ Id: 12345, Status: 20 }")
                );
        whenHttp(server)
                .match(startsWithUri("/api/receipt"), method(Method.GET))
                .then(status(HttpStatus.OK_200),
                        stringContent(TEST_STRING),
                        contentType(MediaType.APPLICATION_OCTET_STREAM)
                );
        whenHttp(server)
                .match(startsWithUri("/api/transaction/454545/file/123123"),
                        method(Method.GET))
                .then(status(HttpStatus.OK_200),
                        stringContent(TEST_STRING),
                        contentType(MediaType.APPLICATION_OCTET_STREAM)
                );
        whenHttp(server)
                .match(startsWithUri("/api/file/"),
                        method(Method.PUT))
                .then(status(HttpStatus.OK_200),
                        stringContent(TEST_STRING),
                        contentType(MediaType.APPLICATION_OCTET_STREAM)
                );

        whenHttp(server)
                .match(matchesUri(new Regexp(".*INVALID-ID.*")))
                .then(status(HttpStatus.NOT_FOUND_404), stringContent("{\"Message\":\"INVALID ID\"}")
                );
    }

    @After
    public void breakDown() {
        server.stop();
    }

    /**
     * Test if create transaction returns a new transaction, based on the response from the REST call
     */
    @Test
    @NeedsServer
    public void testIfCreateTransactionWorks() {

        Transaction t = ondertekenenClient.createTransaction(Helper.createDemoTransaction(Helper.createDemoFileInfo(), Helper.createDemoSigners()));
        /* Verify result object */
        assertEquals("12345", t.getId());

        /* Verify REST call */
        verifyHttp(server)
                .once(uri("/api/transaction/")
                        , method(Method.POST)
                        , Condition.withHeader("Application", "12345")
                        , Condition.withHeader("Authorization", "54321"));
    }

    /**
     * Test if we can retrieve a transaction
     */
    @Test
    @NeedsServer
    public void testIfGetTransactionWorks() {
        Transaction t = ondertekenenClient.getTransaction("12345");

        /* Verify result object */
        assertEquals("12345", t.getId());
        assertEquals(TransactionStatus.IN_PROGRESS, t.getStatus());

        /* Verify REST call */
        verifyHttp(server)
                .once(uri("/api/transaction/12345")
                        , method(Method.GET)
                        , Condition.withHeader("Application", "12345")
                        , Condition.withHeader("Authorization", "54321"));

    }

    /**
     * Test if a transaction can be deleted, and is returned as a result
     */
    @Test
    @NeedsServer
    public void testIfDeleteTransactionWorks() {

        Transaction t = ondertekenenClient.deleteTransaction("12345", false, null);

        /* Verify result object */
        assertEquals("12345", t.getId());
        assertEquals(TransactionStatus.IN_PROGRESS, t.getStatus());

        /* Verify REST call */
        verifyHttp(server)
                .once(uri("/api/transaction/12345")
                        , method(Method.DELETE)
                        , Condition.withHeader("Application", "12345")
                        , Condition.withHeader("Authorization", "54321"));
    }

    /**
     * Test if a receipt can be retrieved
     */
    @Test
    @NeedsServer
    public void testIfGetReceiptWorks() {
        Receipt receipt = ondertekenenClient.getReceipt("112233", false);

        /* Verify result object */
        assertEquals(TEST_STRING, new String(receipt.getData()));

        /* Verify REST call */
        verifyHttp(server)
                .once(uri("/api/receipt/112233")
                        , method(Method.GET)
                        , Condition.withHeader("Application", "12345")
                        , Condition.withHeader("Authorization", "54321"));
    }

    /**
     * Test if a signed document can be retrieved
     */
    @Test
    @NeedsServer
    public void testIfGetSignedDocumentWorks() {

        Document signedDocument = ondertekenenClient.getSignedDocument("454545", "123123", false);

        /* Verify result object */
        assertEquals(TEST_STRING, new String(signedDocument.getData()));

        /* Verify REST call */
        verifyHttp(server)
                .once(uri("/api/transaction/454545/file/123123")
                        , method(Method.GET)
                        , Condition.withHeader("Application", "12345")
                        , Condition.withHeader("Authorization", "54321"));
    }

    /**
     * Test if a pdf can be uploaded
     */
    @Test
    @NeedsServer
    public void testIfUploadFileWorks() throws URISyntaxException {

        ondertekenenClient.uploadFile(
                Transaction.builder().id("123321").build(),
                new File(this.getClass().getResource("sample.txt").toURI()));

        /* Verify REST call */
        verifyHttp(server)
                .once(uri("/api/transaction/123321/file/sample.txt")
                        , method(Method.PUT)
                        , Condition.withHeader("Application", "12345")
                        , Condition.withHeader("Authorization", "54321")
                        , Condition.withPostBodyContaining("\"Hail to the king, baby!\" -- Duke Nukem"));
    }

    @Test
    @NeedsServer
    public void testErrorScenario() {
        /* Test invalid transaction */
        Transaction transaction = ondertekenenClient.getTransaction("INVALID-ID");
        assertEquals(false, transaction.isOk());
        assertEquals("INVALID ID", transaction.getErrorMessage().getMessage());

        /* Test invalid receipt */
        Receipt receipt = ondertekenenClient.getReceipt("INVALID-ID", false);
        assertEquals(false, receipt.isOk());
        assertEquals("INVALID ID", receipt.getErrorMessage().getMessage());

        /* Test invalid document */
        Document document = ondertekenenClient.getSignedDocument("INVALID-ID", "123", false);
        assertEquals(false, document.isOk());
        assertEquals("INVALID ID", document.getErrorMessage().getMessage());
    }
}
