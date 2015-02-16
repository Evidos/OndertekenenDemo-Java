package nl.yurimeiburg.ondertekenen.demo;

import nl.yurimeiburg.ondertekenen.dao.OndertekenenClient;
import nl.yurimeiburg.ondertekenen.objects.Document;
import nl.yurimeiburg.ondertekenen.objects.Receipt;
import nl.yurimeiburg.ondertekenen.objects.Transaction;
import nl.yurimeiburg.ondertekenen.objects.TransactionStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;


/**
 * Show the API for Ondertekenen.nl
 * The aim is to show a complete flow for signing documents using the Ondertekenen.nl service. The first step is
 * to create a new transaction, and get the result from the server. If all went well, a PDF file is uploaded.
 * Then the recipient should be notified, this happens automatically by the service. The program will wait, and
 * poll at a given interval to see if the document has been signed. As soon as the document has been signed, the
 * signed document is retrieved, plus the receipt.
 * <b>Note: </b>This flow is not realistic in practice, as blocking until a user has signed is not exactly efficient
 * and a transaction cannot be deleted after the process is finished.
 */
class Demo {
    private static final Logger LOGGER = LogManager.getLogger(Demo.class);
    private static final long POLLING_INTERVAL = TimeUnit.SECONDS.toMillis(10);
    private static final File SIGNED_DOCUMENT_OUTPUT_LOCATION = new File("SignedDocument.pdf");
    private static final File RECEIPT_OUTPUT_LOCATION = new File("Receipt.pdf");

    /**
     * Entry point
     * @param args Startup parameters -- Not used.
     */
    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        new Demo(context.getBean("ondertekenenClient", OndertekenenClient.class));
    }

    /**
     * Create a new Demo Object, and start the flow of the Ondertekenen service.
     * @param ondertekenenClient - The DAO for the Ondertekenen service.
     */
    private Demo(OndertekenenClient ondertekenenClient) {

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
        if(ondertekenenClient.uploadFile(transaction, Helper.getDemoFile())) {
            LOGGER.info("Successfully uploaded PDF for transaction " + transaction.getId());
        }else{
            LOGGER.error("Could not upload PDF. Deleting transaction: " + ondertekenenClient.deleteTransaction(transaction, true, "Testing."));
            return;
        }

        /* Wait until the user has performed an action on the document */
        blockUntilUserAction(ondertekenenClient, transaction);

        /* The user has either Signed or Rejected the transaction! Get the signed document + receipt. */
        LOGGER.info("Downloading proof.");
        Document signedDocument = ondertekenenClient.getSignedDocument(transaction.getFile().getId(), true);
        writeFile(signedDocument.getData(), SIGNED_DOCUMENT_OUTPUT_LOCATION);
        Receipt receipt = ondertekenenClient.getReceipt(transaction.getId(), true);
        writeFile(receipt.getData(), RECEIPT_OUTPUT_LOCATION);

    }

    /**
     * Wait until the user has performed an action on the document
     * An action could be to sign it, or to reject it.
     * The polling interval is set in the variable POLLING_INTERVAL.
     * @param ondertekenenClient The client used to poll the new status
     * @param transaction The transaction used to poll the status for.
     */
    void blockUntilUserAction(OndertekenenClient ondertekenenClient, Transaction transaction){
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

    /**
     * Small helper function to write a byte stream to a file.
     * @param data The byte stream to be written to file
     * @param file The output location
     */
    void writeFile(byte[] data, File file){
        try(FileOutputStream fos = new FileOutputStream(file)){
            fos.write(data);
        } catch (IOException e) {
            LOGGER.error("Could not write to file: " + file.getName(), e);
        }
    }
}

