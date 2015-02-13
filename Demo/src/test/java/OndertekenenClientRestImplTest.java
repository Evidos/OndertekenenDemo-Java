import com.xebialabs.restito.semantics.Action;
import com.xebialabs.restito.semantics.Condition;
import com.xebialabs.restito.server.StubServer;
import com.xebialabs.restito.support.junit.NeedsServer;
import nl.evidos.ondertekenen.dao.OndertekenenClient;
import nl.evidos.ondertekenen.demo.Helper;
import nl.evidos.ondertekenen.objects.Transaction;
import nl.evidos.ondertekenen.objects.TransactionStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.builder.verify.VerifyHttp.verifyHttp;
import static junit.framework.TestCase.assertEquals;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

/**
 * Created by Yuri Meiburg on 12-2-2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring_unittests.xml")
@ComponentScan
public class OndertekenenClientRestImplTest {
    private static final int DEFAULT_TEST_PORT = 50080;
    private static final Logger LOGGER = LogManager.getLogger(OndertekenenClientRestImplTest.class);

    @Resource(name = "ondertekenenClient")
    OndertekenenClient ondertekenenClient;

    @Test
    @NeedsServer
    public void testIfCreateTransactionWorks(){
        StubServer server = new StubServer(DEFAULT_TEST_PORT).run();
        whenHttp(server)
                .match(Condition.uri("/api/transaction/"),
                        Condition.method(Method.POST))
                .then(Action.status(HttpStatus.OK_200),
                        Action.stringContent("{ Id: 12345 }"),
                        Action.contentType(MediaType.APPLICATION_JSON)
                );
            Transaction t = ondertekenenClient.createTransaction(Helper.createDemoTransaction(Helper.createDemoFileInfo(), Helper.createDemoSigners()));
            assertEquals("12345", t.getId());
        server.stop();
    }

    @Test
    @NeedsServer
    public void testIfGetTransactionWorks(){
        StubServer server = new StubServer(DEFAULT_TEST_PORT).run();
        whenHttp(server)
                .match(Condition.uri("/api/transaction/12345"),
                        Condition.method(Method.GET))
                .then(Action.status(HttpStatus.OK_200),
                        Action.stringContent("{ Id: 12345, Status: 20 }"),
                        Action.contentType(MediaType.APPLICATION_JSON)
                );
        Transaction t = ondertekenenClient.getTransaction("12345");
        assertEquals("12345", t.getId());
        assertEquals(TransactionStatus.IN_PROGRESS, t.getStatus());
        server.stop();
    }

}
