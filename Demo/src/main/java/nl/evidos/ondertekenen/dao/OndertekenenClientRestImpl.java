package nl.evidos.ondertekenen.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import nl.evidos.ondertekenen.objects.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * {@inheritDoc}
 *
 * REST implementation of OndertekenenClient
 * @see nl.evidos.ondertekenen.dao.OndertekenenClient
 */
public class OndertekenenClientRestImpl implements OndertekenenClient {

    /* Urls for the REST Api */
    public static final String DOCUMENT_URL = "https://api.signhost.com/api/file/document/";
    public static final String RECEIPT_URL = "https://api.signhost.com/api/receipt/";
    public static final String TRANSACTION_URL = "https://api.signhost.com/api/transaction/";
    public static final String FILE_URL = "https://api.signhost.com/api/file/";

    private static final Logger LOGGER = LogManager.getLogger(OndertekenenClientRestImpl.class);
    private RESTEngine restEngine = null;
    private Gson gson = new GsonBuilder().create();

    /**
     * Create a DAO object for Ondertekenen using REST implementation.
     * @param appName The application name, used in the HTTP Headers (authentication)
     * @param appKey The application key, used in the HTTP Headers (authentication)
     * @param apiKey The api key, used in the HTTP Headers (authentication)
     */
    public OndertekenenClientRestImpl(String appName, String appKey, String apiKey){
        Map<String, String> defaultHeaders = new HashMap<>();
        defaultHeaders.put("Application", "APPKey " + appName + " " + appKey);
        defaultHeaders.put("Authorization", "APIKey " + apiKey);
        restEngine = new RESTEngine(defaultHeaders);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Document getSignedDocument(String fileID, boolean sendSignedRequest) {
        WebResource.Builder webResourceBuilder = restEngine.getWebResourceBuilder(DOCUMENT_URL + fileID);
        try {
            ClientResponse clientResponse = webResourceBuilder.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
            return restEngine.handleBinaryResponse(clientResponse, Document.class);
        }catch(ClientHandlerException ce){
            LOGGER.error("Cannot connect to the webservice, returning empty document.", ce);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Receipt getReceipt(String documentID, boolean sendSignedRequest){
        WebResource.Builder webResourceBuilder = restEngine.getWebResourceBuilder(RECEIPT_URL + documentID);
        try {
            ClientResponse clientResponse = webResourceBuilder.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
            return restEngine.handleBinaryResponse(clientResponse, Receipt.class);
        }catch(ClientHandlerException ce){
            LOGGER.error("Cannot connect to the webservice, returning empty document.", ce);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction getTransaction(String transactionId) {
        WebResource.Builder webResourceBuilder = restEngine.getWebResourceBuilder(TRANSACTION_URL + transactionId);
        try {
            ClientResponse clientResponse = webResourceBuilder.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
            return restEngine.handleJSONResponse(clientResponse, Transaction.class);
        }catch(ClientHandlerException ce){
            LOGGER.error("Cannot connect to the webservice, returning empty document.", ce);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction deleteTransaction(Transaction transaction, boolean sendNotification, String reason){
        if(transaction == null){
            return null;
        }
        return deleteTransaction(transaction.getId(), sendNotification, reason);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction deleteTransaction(String transactionId, boolean sendNotification, String reason) {
        CancelTransaction cancelTransaction = new CancelTransaction(sendNotification, reason);
        WebResource.Builder webResourceBuilder = restEngine.getWebResourceBuilder(TRANSACTION_URL + transactionId);
        try {
            ClientResponse clientResponse = webResourceBuilder
                    .accept(MediaType.APPLICATION_JSON)
                    .type(MediaType.APPLICATION_JSON)
                    .delete(ClientResponse.class, gson.toJson(cancelTransaction));
            return restEngine.handleJSONResponse(clientResponse, Transaction.class);
        }catch(ClientHandlerException ce){
            LOGGER.error("Cannot connect to the webservice, returning empty document.", ce);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction createTransaction(Transaction transaction) {
        WebResource.Builder webResourceBuilder = restEngine.getWebResourceBuilder(TRANSACTION_URL);
        try {
            ClientResponse clientResponse = webResourceBuilder
                    .accept(MediaType.APPLICATION_JSON)
                    .type(MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class, gson.toJson(transaction));
            return restEngine.handleJSONResponse(clientResponse, Transaction.class);
        }catch(ClientHandlerException ce){
            LOGGER.error("Cannot connect to the webservice, returning empty document.", ce);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uploadFile(Transaction transaction, File file) {
        LOGGER.info("Uploading PDF from: " + file.getName());
        WebResource.Builder webResourceBuilder = restEngine.getWebResourceBuilder(FILE_URL + transaction.getFile().getId());
        try {
            InputStream inputStream = new FileInputStream(file);
            webResourceBuilder
                    .type(MediaType.APPLICATION_JSON)
                    .put(ClientResponse.class, inputStream);
        }catch (FileNotFoundException e) {
            LOGGER.error("Could not open PDF for uploading.", e);
        }catch(ClientHandlerException ce){
            LOGGER.error("Cannot connect to the webservice, returning empty document.", ce);
        }

    }

}
