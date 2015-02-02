package nl.evidos.ondertekenen;

import nl.evidos.ondertekenen.dao.OndertekenenClient;
import nl.evidos.ondertekenen.demo.Helper;
import nl.evidos.ondertekenen.objects.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 * Created by Yuri Meiburg on 14-1-2015.
 */
public class Demo {
    private static final Logger LOGGER = LogManager.getLogger(Demo.class);
    private static final long POLLING_INTERVAL = TimeUnit.SECONDS.toMillis(10);
    private static final File SIGNED_DOCUMENT_OUTPUT_LOCATION = new File("SignedDocument.pdf");
    private static final File RECEIPT_OUTPUT_LOCATION = new File("Receipt.pdf");

    /**
     * Show the API for Ondertekenen.nl
     * The aim is to show a complete flow for signing documents using the Ondertekenen.nl service. The first step is
     * to create a new transaction, and get the result from the server. If all went well, a PDF file is uploaded.
     * Then the recipient should be notified, this happens automatically by the service. The program will wait, and
     * poll at a given interval to see if the document has been signed. As soon as the document has been signed, the
     * signed document is retrieved, plus the receipt.
     * <b>Note: </b>This flow is not realistic in practice, as blocking until a user has signed is not exactly efficient
     * and a transaction cannot be deleted after the process is finished.
     * @param ondertekenenClient - The DAO for the Ondertekenen service.
     */
    public Demo(OndertekenenClient ondertekenenClient){


        Document signdDocument = ondertekenenClient.getSignedDocument("a1d1369d-f675-48d2-a1f2-adf79af56fe0", true);
        try(FileOutputStream fos = new FileOutputStream(SIGNED_DOCUMENT_OUTPUT_LOCATION)){
            fos.write(signdDocument.getData());
        } catch (IOException e) {
            LOGGER.error("Could not write signed document.", e);
        }

        Receipt reeipt = ondertekenenClient.getReceipt("f1690e6b-eb4c-48d3-a585-4cbba2dc097e", true);
        try(FileOutputStream fos = new FileOutputStream(RECEIPT_OUTPUT_LOCATION)){
            fos.write(reeipt.getData());
        } catch (IOException e) {
            LOGGER.error("Could not write receipt.", e);
        }

        //LOGGER.info(ondertekenenClient.getTransaction("f1690e6b-eb4c-48d3-a585-4cbba2dc097e"));
        //LOGGER.info(ondertekenenClient.getSignedDocument("a1d1369d-f675-48d2-a1f2-adf79af56fe0", true));
        //LOGGER.info(ondertekenenClient.getReceipt("f1690e6b-eb4c-48d3-a585-4cbba2dc097e", true));
        System.exit(0);











        /* Create a new transaction */
        Transaction transaction = ondertekenenClient.createTransaction(
                Helper.createDemoTransaction(
                        Helper.createDemoFileInfo(),
                        Helper.createDemoSigners()
                ));

        if(transaction == null)
            throw new RuntimeException("Could not create a new transaction.");

        LOGGER.info("Created a new transaction, with ID: " + transaction.getId());

        /* Get the newly created transaction */
        transaction = ondertekenenClient.getTransaction(transaction.getId());
        LOGGER.info("Got the following transaction from server: " + (transaction.getId()));

        /* Upload a file! */
        ondertekenenClient.uploadFile(transaction, new File("C:\\test.pdf"));
        LOGGER.info("Successfully uploaded PDF for transaction " + transaction.getId());

        /* Wait until the user has performed an action on the document */
        blockUntilUserAction(ondertekenenClient, transaction);

        /* The user has either Signed or Rejected the transaction! Get the signed document + receipt. */
        String fileID = transaction.getFile().getId();
        Document signedDocument = ondertekenenClient.getSignedDocument(fileID, true);
        try(FileOutputStream fos = new FileOutputStream(SIGNED_DOCUMENT_OUTPUT_LOCATION)){
            fos.write(signedDocument.getData());
        } catch (IOException e) {
            LOGGER.error("Could not write signed document.", e);
        }

        Receipt receipt = ondertekenenClient.getReceipt(transaction.getId(), true);
        try(FileOutputStream fos = new FileOutputStream(RECEIPT_OUTPUT_LOCATION)){
            fos.write(receipt.getData());
        } catch (IOException e) {
            LOGGER.error("Could not write receipt.", e);
        }

        /* Delete the transaction */
        LOGGER.info("Deleted the following transaction: " + ondertekenenClient.deleteTransaction(transaction, true, "Testing."));

    }

    /**
     * Entry point
     * @param args Startup parameters -- Not used.
     */
    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        new Demo(context.getBean("ondertekenenClient", OndertekenenClient.class));

    }

    /**
     * Wait until the user has performed an action on the document
     * An action could be to sign it, or to reject it.
     * The polling interval is set in the variable POLLING_INTERVAL.
     * @param ondertekenenClient The client used to poll the new status
     * @param transaction The transaction used to poll the status for.
     */
    public void blockUntilUserAction(OndertekenenClient ondertekenenClient, Transaction transaction){
        /* Block until the user has signed: */
        while(  transaction.getStatus() == TransactionStatus.WAITING_FOR_DOCUMENT ||
                transaction.getStatus() == TransactionStatus.WAITING_FOR_SIGNER ||
                transaction.getStatus() == TransactionStatus.IN_PROGRESS) {
            try {
                LOGGER.info("Waiting for recipient to sign/reject document. Trying again in "
                        + TimeUnit.MILLISECONDS.toSeconds(POLLING_INTERVAL) + " seconds.");
                Thread.sleep(POLLING_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            transaction = ondertekenenClient.getTransaction(transaction.getId());
            LOGGER.info("Transaction is now in state: " + (transaction.getStatus()));
        }
    }
}

