import com.xebialabs.restito.semantics.Action;
import com.xebialabs.restito.semantics.Condition;
import com.xebialabs.restito.server.StubServer;
import com.xebialabs.restito.support.junit.NeedsServer;
import nl.evidos.ondertekenen.dao.OndertekenenClient;
import nl.evidos.ondertekenen.demo.Helper;
import nl.evidos.ondertekenen.objects.*;
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

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.builder.verify.VerifyHttp.verifyHttp;
import static com.xebialabs.restito.semantics.Action.contentType;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static com.xebialabs.restito.semantics.Condition.*;
import static junit.framework.TestCase.assertEquals;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.net.URISyntaxException;

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
    public void init(){
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
                .match(startsWithUri("/api/file/document/"),
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
    }

    @After
    public void breakDown(){
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

        Document signedDocument = ondertekenenClient.getSignedDocument("454545", false);

        /* Verify result object */
        assertEquals(TEST_STRING, new String(signedDocument.getData()));

        /* Verify REST call */
        verifyHttp(server)
                .once(uri("/api/file/document/454545")
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
        ondertekenenClient.uploadFile(new Transaction(new FileInfo("sample.txt").withId("123321"),null),
                new File(this.getClass().getResource("sample.txt").toURI()));

        /* Verify REST call */
        verifyHttp(server)
                .once(uri("/api/file/123321")
                        , method(Method.PUT)
                        , Condition.withHeader("Application", "12345")
                        , Condition.withHeader("Authorization", "54321"));
    }
}
