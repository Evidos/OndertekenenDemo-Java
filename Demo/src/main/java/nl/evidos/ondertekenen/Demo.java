package nl.evidos.ondertekenen;

import com.sun.org.apache.xpath.internal.operations.Mod;
import nl.evidos.ondertekenen.dao.OndertekenenClient;
import nl.evidos.ondertekenen.demo.Helper;
import nl.evidos.ondertekenen.objects.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;


/**
 * Created by Yuri Meiburg on 14-1-2015.
 */
public class Demo {
    private static final Logger LOGGER = LogManager.getLogger(Demo.class);

    /**
     * Show the API for Ondertekenen.nl
     * The aim is to show a complete flow for signing documents using the Ondertekenen.nl service. The first step is
     * to create a new transaction, and get the result from the server. If all went well, a PDF file is uploaded.
     * Then the recipient should be notified, this happens automatically by the service. The program will wait, and
     * poll at a given interval to see if the document has been signed. As soon as the document has been signed, the
     * signed document is retrieved, plus the receipt.
     * <b>Note: </b>This flow is not realistic in practice, as blocking until a user has signed is not exactly efficient
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
        transactionResponse = ondertekenenClient.getTransaction(transactionResponse.getResult().getId());
        LOGGER.info("Got the following transaction from server: " + (transactionResponse.getResult().getId()));

        /* Upload a file! */
        Response<ModelObject> fileResponse = ondertekenenClient.uploadFile(
                transactionResponse.getResult(),
                new File("C:\\test.pdf")
            );

        /* See if everything went OK: */
        if(fileResponse.isOk()){
            LOGGER.info("Successfully uploaded PDF for transaction " + transactionResponse.getResult().getId());
        }else{
            LOGGER.error("Error uploading PDF: " + fileResponse.getError());
            return;
        }

        /* Block until the user has signed: */
        while(  transactionResponse.getResult().getStatus() == TransactionStatus.WAITING_FOR_DOCUMENT.value() ||
                transactionResponse.getResult().getStatus() == TransactionStatus.WAITING_FOR_SIGNER.value() ||
                transactionResponse.getResult().getStatus() == TransactionStatus.IN_PROGRESS.value()){
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            transactionResponse = ondertekenenClient.getTransaction(transactionResponse.getResult().getId());
            LOGGER.info("Transaction is now in state: " + (transactionResponse.getResult().getStatus()));

        }

        /* The user has either Signed or Rejected the transaction! Get the signed document + receipt. */


        /* Delete the transaction */
        LOGGER.info("Deleted the following transaction: " + ondertekenenClient.deleteTransaction(transactionResponse.getResult(), true, "Testing."));

    }

    /**
     * Entry point
     * @param args Startup parameters -- Not used.
     */
    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        new Demo(context.getBean("ondertekenenClient", OndertekenenClient.class));

    }

}

