package nl.yurimeiburg.ondertekenen.dao;

import java.util.Map;

public class OndertekenenClientFactory {

    public static OndertekenenClient build(Map<String, String> headerMap) {
        RESTEngine restEngine = new RESTEngine(headerMap);
        return new OndertekenenClientRestImpl(restEngine);
    }

}
