package nl.evidos.ondertekenen.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import nl.evidos.ondertekenen.objects.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.net.ssl.SSLContext;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


/**
 * {@inheritDoc}
 *
 * REST implementation of {@see OndertekenenClient}
 */
public class OndertekenenClientRestImpl implements OndertekenenClient {

    /* Urls for the REST Api */
    public static final String GET_SIGNED_DOCUMENT = "https://api.signhost.com/api/document/";
    public static final String GET_RECEIPT = "https://api.signhost.com/api/receipt/";
    public static final String TRANSACTION_URL = "https://api.signhost.com/api/transaction/";

    private static final Logger LOGGER = LogManager.getLogger(OndertekenenClientRestImpl.class);
    private Map<String, String> defaultHeaders = new HashMap<>();
    private RESTEngine restEngine = null;
    private Gson gson = new GsonBuilder().create();

    /**
     * Create a DAO object for Ondertekenen using REST implementation.
     * @param appName The application name, used in the HTTP Headers (authentication)
     * @param appKey The application key, used in the HTTP Headers (authentication)
     * @param apiKey The api key, used in the HTTP Headers (authentication)
     */
    public OndertekenenClientRestImpl(String appName, String appKey, String apiKey){
        defaultHeaders.put("Application", "APPKey " + appName + " " + appKey);
        defaultHeaders.put("Authorization", "APIKey " + apiKey);
        restEngine = new RESTEngine(defaultHeaders);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response<Document> getSignedDocument(String documentID, boolean sendSignedRequest) {
        return restEngine.get(GET_SIGNED_DOCUMENT + documentID, Document.class, RESTEngine.JSON);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response<Receipt> getReceipt(String documentID, boolean sendSignedRequest){
        return restEngine.get(GET_RECEIPT + documentID, Receipt.class, RESTEngine.JSON);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response<Transaction> getTransaction(String transactionId) {
        return restEngine.get(TRANSACTION_URL + transactionId, Transaction.class, RESTEngine.JSON);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response<Transaction> deleteTransaction(Transaction transaction, boolean sendNotification, String reason){
        if(transaction == null){
            return null;
        }
        return deleteTransaction(transaction.getId(), sendNotification, reason);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Response<Transaction> deleteTransaction(String transactionId, boolean sendNotification, String reason) {
        LOGGER.info("Deleting transaction: " + transactionId);
        CancelTransaction cancelTransaction = new CancelTransaction(sendNotification, reason);
        return restEngine.delete(TRANSACTION_URL + transactionId, Transaction.class, gson.toJson(cancelTransaction), RESTEngine.JSON, RESTEngine.JSON);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response<Transaction> createTransaction(Transaction transaction) {
        LOGGER.info("Creating transaction for: " + transaction);
        return restEngine.post(TRANSACTION_URL, Transaction.class, gson.toJson(transaction), null, RESTEngine.JSON);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uploadFile(URL file) {
        throw new  NotImplementedException();
    }

}
