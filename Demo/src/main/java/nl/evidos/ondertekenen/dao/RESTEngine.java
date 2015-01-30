package nl.evidos.ondertekenen.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import nl.evidos.ondertekenen.objects.ErrorMessage;
import nl.evidos.ondertekenen.objects.ModelObject;
import nl.evidos.ondertekenen.objects.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.ws.rs.core.MediaType;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Yuri Meiburg on 29-1-2015.
 */
public class RESTEngine {

    private static final Logger LOGGER = LogManager.getLogger(RESTEngine.class);
    public static final String APPLICATION_PDF = "application/pdf";
    private Client client = createSSLClient();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Map<String, String> defaultHeaders;

    /**
     * Create a RESTEngine object, which has default headers appended to every call
     * @param defaultHeaders A Map of the headers
     */
    public RESTEngine (Map<String, String> defaultHeaders){
        this.defaultHeaders = defaultHeaders;
    }

    /**
     * Perform an SSL REST GET request to a given URL. If the request succeeds, parse the result
     * to type {@code returnType}, and return the result.
     * @param <T> Type of the JSON result object
     * @param targetUrl The url to perform the REST call on
     * @param returnType The target class for the resulting JSON object
     * @param acceptingTypes One or more valid return types
     * @return A ModelObject of class {@code returnType} upon success.
     */
    public <T extends ModelObject> Response<T> get(String targetUrl, Class<T> returnType, String ... acceptingTypes){
        WebResource.Builder webResourceBuilder = getWebResourceBuilder(targetUrl);
        try {
            ClientResponse clientResponse = webResourceBuilder.accept(acceptingTypes).get(ClientResponse.class);
            return handleResponse(clientResponse, returnType);
        }catch(ClientHandlerException ce){
            LOGGER.error("Cannot connect to the webservice, returning empty document.", ce);
            return Response.unavailable(ce.getMessage());
        }
    }

    /**
     * Perform an SSL REST POST request to a given URL. If the request succeeds, parse the result
     * to type {@code returnType}, and return the result.
     * @param <T> Type of the JSON result object
     * @param targetUrl The url to perform the REST call on
     * @param returnType The target class for the resulting JSON object
     * @param postData The data to be POSTed to the REST-service.
     * @param contentType The type of data contained by postData
     * @param acceptingTypes One or more valid return types
     * @return A ModelObject of class {@code returnType} upon success.
     */
    public <T extends ModelObject> Response<T> post(String targetUrl, Class<T> returnType,Object postData, String contentType, String ... acceptingTypes){
        WebResource.Builder webResourceBuilder = getWebResourceBuilder(targetUrl);
        if(contentType == null) contentType = MediaType.APPLICATION_JSON;
        try {
            ClientResponse clientResponse = webResourceBuilder
                    .accept(acceptingTypes)
                    .type(contentType)
                    .post(ClientResponse.class, postData);
            return handleResponse(clientResponse, returnType);
        }catch(ClientHandlerException ce){
            LOGGER.error("Cannot connect to the webservice, returning empty document.", ce);
            return Response.unavailable(ce.getMessage());
        }
    }

    /**
     * Perform an SSL REST POST request to a given URL. If the request succeeds, parse the result
     * @param <T> Type of the result object
     * @param targetUrl The url to perform the REST call on
     * @param returnType The target class for the resulting JSON object
     * @param postData The data to be POSTed to the REST-service.
     * @param contentType The type of data contained by postData
     * @param acceptingTypes One or more valid return types
     * @return A ModelObject of class {@code returnType} upon success.
     */
    public <T extends ModelObject> Response<T> delete(String targetUrl, Class<T> returnType,Object postData, String contentType, String ... acceptingTypes){
        WebResource.Builder webResourceBuilder = getWebResourceBuilder(targetUrl);
        if(contentType == null) contentType = MediaType.APPLICATION_JSON;
        try {
            ClientResponse clientResponse = webResourceBuilder
                    .accept(acceptingTypes)
                    .type(contentType)
                    .delete(ClientResponse.class, postData);
            return handleResponse(clientResponse, returnType);
        }catch(ClientHandlerException ce){
            LOGGER.error("Cannot connect to the webservice, returning empty document.", ce);
            return Response.unavailable(ce.getMessage());
        }
    }

    /**
     * Perform an SSL REST PUT request to a given URL. If the request succeeds, parse the result
     * to type {@code returnType}, and return the result.
     * @param <T> Type of the JSON result object
     * @param targetUrl The url to perform the REST call on
     * @param returnType The target class for the resulting JSON object
     * @param postData The data to be POSTed to the REST-service.
     * @param contentType The type of data contained by postData
     * @param acceptingTypes One or more valid return types
     * @return A ModelObject of class {@code returnType} upon success.
     */
    public <T extends ModelObject> Response<T> put(String targetUrl, Class<T> returnType,Object postData, String contentType, String ... acceptingTypes){
        WebResource.Builder webResourceBuilder = getWebResourceBuilder(targetUrl);
        if(contentType == null) contentType = MediaType.APPLICATION_JSON;
        try {
            ClientResponse clientResponse = webResourceBuilder
                    .accept(acceptingTypes)
                    .type(contentType)
                    .put(ClientResponse.class, postData);
            return handleResponse(clientResponse, returnType);
        }catch(ClientHandlerException ce){
            LOGGER.error("Cannot connect to the webservice, returning empty document.", ce);
            return Response.unavailable(ce.getMessage());
        }
    }

    /**
     * Parse ClientResponse
     * Extract the result from ClientResponse and try to parse it to type {@code returnType}
     * @param clientResponse The response from the REST service
     * @param returnType The desired return type -- null means no result.
     * @param <T> Type of the result object
     * @return A Response&lt;T&gt; with either a success-message and an object of {@code returnType}, or an error of type ErrorMessage
     */
    private <T extends ModelObject> Response<T> handleResponse(ClientResponse clientResponse, Class<T> returnType){
                /* Handle response */
        if(clientResponse.getStatus() != 200) {
            return Response.error(
                    clientResponse.getStatus(),
                    gson.fromJson(clientResponse.getEntity(String.class), ErrorMessage.class));
        }

        if(returnType != null) {
            return Response.success(gson.fromJson(clientResponse.getEntity(String.class), returnType));
        }else {
            return Response.success(null);
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
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getDefault();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Error initializing SSLContext, could not instantiate algorithm: ", e);
            return null;
        }
        clientConfig.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties((a,b)->true , sslContext));
        Client client = Client.create(clientConfig);
        client.addFilter(new LoggingFilter(System.out));
        return client;
    }

    /**
     * Get WebResource.Builder object for target-url, with default headers appended
     * @param targetUrl The URL the builder should point to
     * @return An initialized WebResource.Builder, with default headers and pointing to the targetURL.
     */
    private WebResource.Builder getWebResourceBuilder(String targetUrl){
        WebResource.Builder builder = client.resource(targetUrl).getRequestBuilder();
        for(String key : defaultHeaders.keySet()){
            builder = builder.header(key, defaultHeaders.get(key));
        }

        return builder;
    }

}
