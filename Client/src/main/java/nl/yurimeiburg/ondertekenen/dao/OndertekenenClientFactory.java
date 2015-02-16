package nl.yurimeiburg.ondertekenen.dao;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuri Meiburg on 16-2-2015.
 */
public class OndertekenenClientFactory{

    public static OndertekenenClient build(Map<String, String> headerMap) {
        RESTEngine restEngine = new RESTEngine(headerMap);
        return new OndertekenenClientRestImpl(restEngine);
    }

}
