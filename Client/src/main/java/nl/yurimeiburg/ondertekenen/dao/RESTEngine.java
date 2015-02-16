package nl.yurimeiburg.ondertekenen.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import nl.yurimeiburg.ondertekenen.gson.SignerActivityCodeDeserializer;
import nl.yurimeiburg.ondertekenen.gson.TransactionStatusDeserializer;
import nl.yurimeiburg.ondertekenen.objects.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;
import java.util.Map;


/**
 * Helper class which takes care of parsing data from the webservice.
 */
public class RESTEngine {

    private static final Logger LOGGER = LogManager.getLogger(RESTEngine.class);
    private static final String DEBUG_WSCALLS_PROPERTY = "nl.evidos.debug.webservice";
    private Client client;
    private Gson gson;
    private Map<String, String> defaultHeaders;


    /**
     * Create a RESTEngine object, which has default headers appended to every call.
     * Initialize a default SSL Client
     * @param defaultHeaders A Map of the headers
     */
    public RESTEngine (Map<String, String> defaultHeaders){
        this(defaultHeaders, createSSLClient());
    }

    /**
     * Create a RESTEngine object, which has default headers appended to every call
     * @param defaultHeaders A Map of the headers
     * @param client The client that will be used for all REST calls
     */
    public RESTEngine (Map<String, String> defaultHeaders, Client client){
        this.client = client;
        this.defaultHeaders = defaultHeaders;
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(TransactionStatus.class, new TransactionStatusDeserializer())
                .registerTypeAdapter(SignerActivityCode.class, new SignerActivityCodeDeserializer())
                .create();
    }

    /**
     * Fetch binary data from ClientResponse
     * Extract the result from ClientResponse and try to parse it to type {@code returnType}
     * @param clientResponse The response from the REST service
     * @param returnType The desired return type -- null means no result.
     * @param <T> Type of the result object
     * @return A Response&lt;T&gt; with either a success-message and an object of {@code returnType}, or an error of type ErrorMessage
     */
    public <T extends BinaryModelObject> T handleBinaryResponse(ClientResponse clientResponse, Class<T> returnType){
        /* Handle response */
        if(clientResponse.getStatus() == 200) {
            try {
                T result = returnType.newInstance();
                result.setData(clientResponse.getEntity(byte[].class));
                return result;
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("Unable to instantiate object of type: " + returnType.getName(), e);
                return null;
            }
        }else{
            try {
                T returnT = returnType.newInstance();
                returnT.setError(gson.fromJson(clientResponse.getEntity(String.class), ErrorMessage.class));
                return returnT;
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("Unable to instantiate object of type: " + returnType.getName(), e);
                return null;
            }

        }
    }

    /**
     * Parse JSON from ClientResponse
     * Extract the JSON-result from ClientResponse and try to parse it to type {@code returnType}
     * @param clientResponse The response from the REST service
     * @param returnType The desired return type -- null means no result.
     * @param <T> Type of the result object, should extend JSONModelObject
     * @return A Response&lt;T&gt; with either a success-message and an object of {@code returnType}, or an error of type ErrorMessage
     */
    public <T extends JSONModelObject> T handleJSONResponse(ClientResponse clientResponse, Class<T> returnType){
        /* Handle response */
        if(clientResponse.getStatus() == 200) {
            return gson.fromJson(clientResponse.getEntity(String.class), returnType);
        }else{
            try {
                T returnT = returnType.newInstance();
                returnT.setError(gson.fromJson(clientResponse.getEntity(String.class), ErrorMessage.class));
                return returnT;
            } catch ( InstantiationException | IllegalAccessException e) {
                LOGGER.error("Unable to instantiate object of type: " + returnType.getName(), e);
                return null;
            }
        }
    }

    /**
     * Create a REST Client with an initialized SSL Context.
     * This enables the Client to perform REST calls to https sites. This implementation uses the default SSL Context, with the default trust-store.
     * <p><b>Note:</b> This implementation does <em>not</em> provide certificate pinning. If additional security is required, implement an SSLContext
     * which provides this feature.</p>
     * @return  REST Client with SSL Properties, null on error
     */
    private static Client createSSLClient(){
        ClientConfig clientConfig = new DefaultClientConfig();
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getDefault();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Error initializing SSLContext, could not instantiate algorithm: ", e);
            return null;
        }
        clientConfig.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties((a,b)->true , sslContext));
        Client client = Client.create(clientConfig);

        if("true".equalsIgnoreCase(System.getProperty(DEBUG_WSCALLS_PROPERTY))) {
            client.addFilter(new LoggingFilter(System.out));
        }
        return client;
    }

    /**
     * Get WebResource.Builder object for target-url, with default headers appended
     * @param targetUrl The URL the builder should point to
     * @return An initialized WebResource.Builder, with default headers and pointing to the targetURL.
     */
    public WebResource.Builder getWebResourceBuilder(String targetUrl){
        WebResource.Builder builder = client.resource(targetUrl).getRequestBuilder();
        for(String key : defaultHeaders.keySet()){
            builder = builder.header(key, defaultHeaders.get(key));
        }

        return builder;
    }

}
