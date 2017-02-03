package nl.yurimeiburg.ondertekenen.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import nl.yurimeiburg.ondertekenen.objects.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.MediaType;
import java.io.*;
import java.text.MessageFormat;
import java.util.Properties;


/**
 * {@inheritDoc}
 * <p>
 * REST implementation of OndertekenenClient
 *
 * @see OndertekenenClient
 */
public class OndertekenenClientRestImpl implements OndertekenenClient {

    private static final String API_PROPERTIES_LOCATION = "api.properties";
    private static final String MEDIATYPE_APPLICATION_PDF = "application/pdf";
    /* Urls for the REST Api */
    private static String DOCUMENT_URL;
    private static String RECEIPT_URL;
    private static String TRANSACTION_URL;
    private static String FILE_URL;

    private static final Logger LOGGER = LogManager.getLogger(OndertekenenClientRestImpl.class);
    private RESTEngine restEngine = null;
    private Gson gson = new GsonBuilder().create();

    /**
     * Create a DAO object for Ondertekenen using REST implementation.
     *
     * @param restEngine The RESTEngine object used for the REST requests
     */
    public OndertekenenClientRestImpl(RESTEngine restEngine) {
        this.restEngine = restEngine;
        initProperties();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Document getSignedDocument(String transactionID, String fileID, boolean sendSignedRequest) {
        WebResource.Builder webResourceBuilder = restEngine.getWebResourceBuilder(MessageFormat.format(FILE_URL, transactionID, fileID));
        System.out.println(MessageFormat.format(FILE_URL, transactionID, fileID));
        try {
            ClientResponse clientResponse = webResourceBuilder.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
            return restEngine.handleBinaryResponse(clientResponse, Document.class);
        } catch (ClientHandlerException ce) {
            LOGGER.error("Cannot connect to the webservice, returning empty document.", ce);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Receipt getReceipt(String documentID, boolean sendSignedRequest) {
        WebResource.Builder webResourceBuilder = restEngine.getWebResourceBuilder(RECEIPT_URL + documentID);
        try {
            ClientResponse clientResponse = webResourceBuilder.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
            return restEngine.handleBinaryResponse(clientResponse, Receipt.class);
        } catch (ClientHandlerException ce) {
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
        } catch (ClientHandlerException ce) {
            LOGGER.error("Cannot connect to the webservice, returning empty document.", ce);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction deleteTransaction(Transaction transaction, boolean sendNotification, String reason) {
        if (transaction == null) {
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
        } catch (ClientHandlerException ce) {
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
        } catch (ClientHandlerException ce) {
            LOGGER.error("Cannot connect to the webservice, returning empty document.", ce);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean uploadFile(Transaction transaction, File file) {
        LOGGER.info("Uploading PDF from: " + file.getName());
        WebResource.Builder webResourceBuilder = restEngine.getWebResourceBuilder(MessageFormat.format(FILE_URL, transaction.getId(), file.getName()));
        try {
            InputStream inputStream = new FileInputStream(file);
            ClientResponse clientResponse = webResourceBuilder
                    .type(MEDIATYPE_APPLICATION_PDF)
                    .put(ClientResponse.class, inputStream);
        } catch (FileNotFoundException e) {
            LOGGER.error("Could not open PDF for uploading.", e);
            return false;
        } catch (ClientHandlerException ce) {
            LOGGER.error("Cannot connect to the webservice.", ce);
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean uploadFileMetaData(Transaction transaction, FileMetaData fileMetaData, String identifier) {
        LOGGER.info("Uploading File Metadata: " + fileMetaData + " for file: " + identifier);
        WebResource.Builder webResourceBuilder = restEngine.getWebResourceBuilder(MessageFormat.format(FILE_URL, transaction.getId(), identifier));
        try {
            webResourceBuilder
                    .type(MediaType.APPLICATION_JSON)
                    .put(ClientResponse.class, gson.toJson(fileMetaData));
        } catch (ClientHandlerException ce) {
            LOGGER.error("Cannot connect to the webservice.", ce);
            return false;
        }
        return true;
    }

    @Override
    public boolean startTransaction(String transactionId) {
        WebResource.Builder webResourceBuilder = restEngine.getWebResourceBuilder(TRANSACTION_URL + transactionId + "/start");
        try {
            ClientResponse clientResponse = webResourceBuilder
                    .accept(MediaType.APPLICATION_JSON)
                    .type(MediaType.APPLICATION_JSON)
                    .put(ClientResponse.class, "");
            return true;
        } catch (ClientHandlerException ce) {
            LOGGER.error("Cannot connect to the webservice, returning empty document.", ce);
            return false;
        }
    }


    /**
     * Read property files to get the API URI's
     */
    private void initProperties() {
        try {
            Properties properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream(API_PROPERTIES_LOCATION));
            DOCUMENT_URL = properties.getProperty("document.url");
            RECEIPT_URL = properties.getProperty("receipt.url");
            FILE_URL = properties.getProperty("file.url");
            TRANSACTION_URL = properties.getProperty("transaction.url");
        } catch (IOException e) {
            LOGGER.fatal("Could not read API locations from properties", e);
        }
    }
}
