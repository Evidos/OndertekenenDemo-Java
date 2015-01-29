package nl.evidos.ondertekenen;

import nl.evidos.ondertekenen.dao.OndertekenenClient;
import nl.evidos.ondertekenen.demo.Helper;
import nl.evidos.ondertekenen.objects.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Created by Yuri Meiburg on 14-1-2015.
 */
public class Demo {
    private static final Logger LOGGER = LogManager.getLogger(Demo.class);
    /**
     * Construct a Demo object
     * @param ondertekenenClient - The DAO for the Ondertekenen service.
     */
    public Demo(OndertekenenClient ondertekenenClient){

        /* Create a new transaction */
        Response<Transaction> transactionResponse = ondertekenenClient.createTransaction(
                Helper.createDemoTransaction(
                        Helper.createDemoFileInfo(),
                        Helper.createDemoSigners()
                ));

        /* See if everything went OK: */
        if( transactionResponse.isOk()){
            LOGGER.info("Created a new transaction, with ID: " + transactionResponse.getResult());
        }else{
            LOGGER.error("Received error code: " + transactionResponse.getStatusCode() +", with message: " + transactionResponse.getError());
            return;
        }

        /* Get the newly created transaction */
        LOGGER.info("Got the following transaction from server: " + ondertekenenClient.getTransaction(transactionResponse.getResult().getId()));

        /* Delete the transaction */
        LOGGER.info("Deleted the following transaction: " + ondertekenenClient.deleteTransaction(transactionResponse.getResult(), true, "Testing."));
    }

    /**
     * Entry point
     * @param args
     */
    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        new Demo(context.getBean("ondertekenenClient", OndertekenenClient.class));

    }

}

